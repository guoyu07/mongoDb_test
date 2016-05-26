<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ page language="java" import="java.util.*,java.sql.*" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>详细信息</title>
<script src="//cdn.bootcss.com/jquery/2.2.0/jquery.min.js"></script>
<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet">
<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link
	href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
	rel="stylesheet">
<style type="text/css">
td {
	padding: 3px;
}
</style>
</head>
<body>
	<h1 align="center">上课详情</h1>
	<table border="1" align="center">
		<tr align="center">
			<td>编号</td>
			<td>roomId</td>
			<td>insertTime</td>
			<td>userType</td>
			<td>source</td>
			<td>postfix</td>
			<td>label</td>
			<td>userName</td>
			<td>userId</td>
			<td>isChange</td>
			<td>status</td>
			<td>desc</td>
		</tr>

		<c:forEach var="rctEvent" items="${rctEvents}" varStatus="i">
			<tr align="center">
				<td>${i.count}</td>
				<td>${rctEvent.roomId}</td>
				<td>${rctEvent.insertTime }</td>
				<td>${rctEvent.userType}</td>
				<td>${rctEvent.source}</td>
				<td>${rctEvent.postfix}</td>
				<td>${rctEvent.label}</td>
				<td>${rctEvent.userName}</td>
				<td>${rctEvent.userId}</td>
				<td>${rctEvent.isChange}</td>
				<td>${rctEvent.status}</td>
				<td>${rctEvent.desc}</td>
			</tr>
		</c:forEach>

	</table>
	<hr>
	
	<h1 align="center">语音详情</h1>
	<table border="1" align="center">
		<tr align="center">
			<td>编号</td>
			<td>roomId</td>
			<td>userName</td>
			<td>userId</td>
			<td>userType</td>
			<td>insertTime</td>
			<td>courseName</td>
			<td>clientName</td>
			<td>osName</td>
			<td>clientVersion</td>
			<td>browserName</td>
			<td>messageSize</td>
			<td>event</td>
			<td>status</td>
			<td>statusDesc</td>
		</tr>

		<c:forEach var="roomEvent" items="${roomEvents}" varStatus="i">
			<tr align="center">
				<td>${i.count}</td>
				<td>${roomEvent.roomId}</td>
				<td>${roomEvent.userName}</td>
				<td>${roomEvent.userId}</td>
				<td>${roomEvent.userType}</td>
				<td>${roomEvent.insertTime}</td>
				<td>${roomEvent.courseName}</td>
				<td>${roomEvent.clientName}</td>
				<td>${roomEvent.osName}</td>
				<td>${roomEvent.clientVersion}</td>
				<td>${roomEvent.browserName}</td>
				<td>${roomEvent.messageSize}</td>
				<td>${roomEvent.event}</td>
				<td>${roomEvent.status}</td>
				<td>${roomEvent.statusDesc}</td>
			</tr>
		</c:forEach>

	</table>
</body>
</html>
