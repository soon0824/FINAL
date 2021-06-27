<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.2/sockjs.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<title>Insert title here</title>
<style type="text/css">

#textArea1{
	border: 1px solid black;
	width:400px;
	height:auto;
}
#textArea2{
	border: 1px solid black;
	width:400px;
	height:auto;
	display:inline-block;
	text-align:right;
}
.table{
	width:400px;
	height:30px;
}
</style>
</head>
<body>
	<c:set var="userId" value="${param.receiverId}"></c:set>
	<c:forEach var="msg" items="${msg}" varStatus="status">
	<c:if test="${msg.senderId eq userId}">
		<div id="textArea1">
		<table class="table">
			<tr>
				<td>
				${msg.msgContent}<br>
				${msg.senderName}<br>
				<fmt:formatDate value="${msg.msgDate}" pattern="yy-MM-dd"/></td>
				<td><button type="button" id="msgDelete">X</button>
				<input type="hidden" name="msgNo" id="msgNo" value="${msg.msgNo}"></td>
			</tr>
		</table>
	</div></c:if>
	<c:if test="${msg.senderId ne userId}">
		<div id="textArea2">
		<table class="table" border="1">
			<tr>
				<td>
				${msg.msgContent}<br>
				<fmt:formatDate value="${msg.msgDate}" pattern="yy-MM-dd"/></td>
				<td><button type="button" id="msgDelete">X</button>
				<input type="hidden" name="msgNo" id="msgNo" value="${msg.msgNo}"></td>
			</tr>
		</table>
	</div></c:if>
	</c:forEach>
<form id="frm">
	<input id="userId2" name="receiverId" value="${param.receiverId}" type="text">
	<input id="txtMsg" name="msgContent" type="text">
	<input id="sendBtn" value="Send" type="button"><br>
</form>
<script>
	var webSocket = new WebSocket("ws://localhost:8090/sprout/msgRead");
	//콘솔 텍스트 에리어 오브젝트

	$('#sendBtn').click(function(){
		var text=$('#txtMsg').val();
		if(text==''){
			alert("내용을 입력해주세요.");
			$('#txtMsg').focus();
			event.preventDefault();
			return;
		} else {
			$('#frm').attr('action','msgInsert2');
			$('#frm').attr('method','post');
			$('#frm').submit();
		}
	})
	$(document).on('click','#msgDelete',function(){
		var num=$(this).next().val();
		console.log(num);
		$.ajax({
			url:'msgDelete',
			type: 'get',
			data: {'num': num},
			success: function(){
				alert('전송성공');
				location.reload();
			},
			error: function(request, status, error){
				alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
	})
	})

</script>
</body>
</html>