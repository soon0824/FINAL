<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/moonspam/NanumSquare/master/nanumsquare.css">
<style>
body {
    font-family: 'NanumSquare', sans-serif !important;
}

.space {
	width: 900px;
	margin-left: auto;
	margin-right: auto;
	margin-top: 80px;
}

#wrapper {
	display: table;
	margin-bottom: 80px;
	position: relative;
	padding-top: 110px;
	color: #202020;
}

th {
	font-size: 15px;
}

td {
	font-size: 16px;
}

.list {
	width: 35px;
	line-height: 40px;
}

.qnaList {
	display: table-cell;
	vertical-align: middle;
	font-size: 20px;
}

.back {
	float: right;
    margin-left: 703px;
    box-shadow: none;
    border-radius: 10px;
    height: 30px;
    border: 1px;
}

.info {
	width: 900px;
	border-color: #ccc;
	border-collapse: collapse;
	border-spacing: 0;
	margin-left: auto;
	margin-right: auto;
}

.replyArea {
	display: inline-block;
	width: 900px;
}

.title {
	width: 630px;
	height: 50px;
	text-align: center;
}

.noId {
	text-align: center;
	width: 130px;
}

.qnaNo {
	border: none;
	text-align: center;
	outline: none;
	width: 20px;
}

.qnaCon {
	text-align: center;
	height: 250px;
}

.con {
	width: 700px;
    margin: auto;
}
</style>
<script>
function back() {
	location.href="notice";
}

jQuery(function($) {
    $("body").css("display", "none");
    $("body").fadeIn(800);
    $("a.transition").click(function(event){
        event.preventDefault();
        linkLocation = this.href;
        $("body").fadeOut(1000, redirectPage);
    });
    function redirectPage() {
    window.location = linkLocation;
    }
});
</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/admin/adminHeader.jsp"></jsp:include>
<div class="space">
	<div id="wrapper">
		<img src="resources/img/admin/list.png" class="list">&nbsp;
		<p class="qnaList">?????? ??????</p>
		<button class="back" onclick="back()">????????????</button>
	</div>
</div> 
<table border="1" class="info">
	<c:forEach var="notice" items="${notice }" varStatus="status">
	    <tr>
	        <td class="noId"><input type="text" class="qnaNo" id="qnaNo" value=${notice.boardNo } readonly></td>
	        <td class="title">${notice.boardTitle }</td>
	        <td class="noId">${notice.boardId }</td>
	    </tr>
	    <tr>
	    	<td class="qnaCon">??????</td>
	        <td colspan="2" class="content">
	        	<div class="con">${notice.boardContent }</div>
	        </td>
	    </tr>
	</c:forEach>
</table><br>

</body>
</html>