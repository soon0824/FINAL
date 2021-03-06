package com.funding.sprout.user.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.funding.sprout.HomeController;
import com.funding.sprout.vo.User;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.funding.sprout.user.service.UserLoginService;


@Controller
@RequestMapping("/user")
public class UserLoginCtrl {
	
	@Autowired
	private UserLoginService userLoginService;
	@Autowired
	NaverLoginBO naverLoginBO;
	@Autowired
	KakaoLoginBO kakaoLoginBO;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) {
		String serverUrl = request.getScheme()+"://"+request.getServerName();
		if(request.getServerPort() != 80) {
			serverUrl = serverUrl + ":" + request.getServerPort();
		}
		
		String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session, serverUrl);
		model.addAttribute("naverAuthUrl", naverAuthUrl);
		
		String kakaoAuthUrl = kakaoLoginBO.getAuthorizationUrl(session, serverUrl);
		model.addAttribute("kakaoAuthUrl", kakaoAuthUrl);
		
		return "/user/login";
	}
	
	@RequestMapping(value = "/loginPost", method = RequestMethod.POST)  
	public void loginPost (User user, HttpSession session, Model model, HttpServletResponse response) throws Exception {
		logger.info("loginPost?????????");
		User loginUser=userLoginService.login(user);
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter wr=response.getWriter();
		
		if(loginUser!=null) {
			String id1=user.getUserId();
			String id2=loginUser.getUserId();
			String pw1=user.getUserPwd();
			String pw2=loginUser.getUserPwd();
			String authority1 = String.valueOf(user.getAuthority());
			String authority2 = String.valueOf(loginUser.getAuthority());
			String stop = "3";
			String drop = "4";
			
			if(id2.equals("")&&pw2.equals("")) {
				wr.println("<script type='text/javascript'>"); 
				wr.println("alert('???????????? ??????????????? ??????????????????.'); window.location = document.referrer;"); 
				wr.println("</script>");
				wr.flush();
			} else if(!id2.equals("")&&!pw2.equals("")) {
				if(id1.equals(id2)&&pw1.equals(pw2)) {	
					if (authority2.equals(stop)) {
						wr.println("<script type='text/javascript'>"); 
						wr.println("alert('????????? ???????????????.'); window.location = document.referrer;"); 
						wr.println("</script>");
						wr.flush();
						System.out.println("?????? ?????? ?????????");
					} else if (authority2.equals(drop)) {
						wr.println("<script type='text/javascript'>"); 
						wr.println("alert('????????? ???????????????.'); window.location = document.referrer;"); 
						wr.println("</script>");
						wr.flush();
						System.out.println("?????? ?????? ?????????");
					} else {
						session.setAttribute("user", loginUser);
						wr.println("<script type='text/javascript'>"); 
						wr.println("window.location.href = '/sprout/funselect';"); 
						wr.println("</script>");
						wr.flush();
						System.out.println("?????? ?????????");
					}
				} else {
					wr.println("<script type='text/javascript'>"); 
					wr.println("alert('???????????? ??????????????? ??????????????????.'); window.location = document.referrer;"); 
					wr.println("</script>");
					wr.flush();
				}
			}
		} else if(loginUser==null) {
			wr.println("<script type='text/javascript'>"); 
			wr.println("alert('???????????? ??????????????? ??????????????????.'); window.location = document.referrer;"); 
			wr.println("</script>");
			wr.flush();
		}
		
	}
	
	// ????????? ????????? ????????? callback
	@RequestMapping(value = "/naverCallback", method = { RequestMethod.GET, RequestMethod.POST })
	public String naverOauth2ClientCallback(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
		
		String serverUrl = request.getScheme()+"://"+request.getServerName();
		if(request.getServerPort() != 80) {
			serverUrl = serverUrl + ":" + request.getServerPort();
		}

		OAuth2AccessToken oauthToken;
		oauthToken = naverLoginBO.getAccessToken(session, code, state, serverUrl);
		if(oauthToken == null) {
			model.addAttribute("msg", "????????? ????????? access ?????? ?????? ?????? ?????????.");
			model.addAttribute("url", "/");
		}
		
		// ????????? ????????? ????????? ????????????
		String apiResult = naverLoginBO.getUserProfile(oauthToken, serverUrl);
		
		JSONParser jsonParser = new JSONParser();
		Object obj = jsonParser.parse(apiResult);
		JSONObject jsonObj = (JSONObject) obj;
		
		JSONObject response_obj = (JSONObject) jsonObj.get("response");

		// ????????? ??????
		String userName = (String) response_obj.get("name");
		String userEmail = (String) response_obj.get("email");
		
		Map<String, String> vo=new HashMap<String, String>();
		vo.put("userName", userName);
		vo.put("userEmail", userEmail);
		
		User loginUser=userLoginService.socialLogin(vo);
		
		// ????????? ????????? ?????? ??????
		session.setAttribute("user", loginUser);
		
		return "redirect:../funselect";
	}
	
	// ????????? ????????? ????????? callback
	@RequestMapping(value = "/kakaoCallback", method = { RequestMethod.GET, RequestMethod.POST })
	public String kakaoOauth2ClientCallback(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {

		String serverUrl = request.getScheme() + "://" + request.getServerName();
		if (request.getServerPort() != 80) {
			serverUrl = serverUrl + ":" + request.getServerPort();
		}

		OAuth2AccessToken oauthToken;
		oauthToken = kakaoLoginBO.getAccessToken(session, code, state, serverUrl);
		if (oauthToken == null) {
			model.addAttribute("msg", "????????? ????????? access ?????? ?????? ?????? ?????????.");
			model.addAttribute("url", "/");
			return "redirect:/";
		}
		System.out.println("???????????????");

		// ????????? ????????? ????????? ????????????
		String apiResult = kakaoLoginBO.getUserProfile(oauthToken, serverUrl);

		JSONParser jsonParser = new JSONParser();
		Object obj = jsonParser.parse(apiResult);
		JSONObject jsonObj = (JSONObject) obj;

		JSONObject response_obj = (JSONObject) jsonObj.get("kakao_account");
		JSONObject profile_obj = (JSONObject)response_obj.get("profile");

		// ????????? ??????
		String userName = (String) profile_obj.get("nickname");
		String userEmail = (String) response_obj.get("email");
		
		Map<String, String> vo=new HashMap<String, String>();
		vo.put("userName", userName);
		vo.put("userEmail", userEmail);
		User loginUser=userLoginService.socialLogin(vo);
		
		// ????????? ????????? ?????? ??????
		session.setAttribute("user", loginUser);
		
		return "redirect:../funselect";
	}
	
	// ?????? ????????? ????????? callback
	@RequestMapping(value = "/googleCallback", method = { RequestMethod.GET, RequestMethod.POST })
	public String googleOauth2ClientCallback(User user, HttpSession session, Model model, HttpServletResponse response) throws Exception {
		
		if(user!=null) {
			String userName=user.getUserName();
			String userEmail=user.getUserEmail();
			Map<String, String> vo=new HashMap<String, String>();
			vo.put("userName", userName);
			vo.put("userEmail", userEmail);
			User loginUser = userLoginService.socialLogin(vo);
			System.out.println("???????????????"+loginUser);
			session.setAttribute("user", loginUser);
			return "redirect:../funselect"; 
		} else {
			System.out.println("????????? ??????");
		}
		return "redirect:../funselect"; 
	}
	
	// ????????????
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		// ?????? ??????
		System.out.println("????????????");
		session.removeAttribute("user");
		
		return "redirect:../funselect";
		
	}
	
	@RequestMapping(value = "/inputJoin", method = RequestMethod.GET)  
	public String inputJoin(User user, HttpSession session, ModelAndView mv) throws Exception {
		logger.info("????????????");
		mv.setViewName("user/inputJoin");
		return "user/inputJoin";
	}
	
	
}
