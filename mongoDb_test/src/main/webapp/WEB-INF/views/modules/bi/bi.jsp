<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="shortcut icon" type="image/ico" href="http://www.datatables.net/favicon.ico">
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">
	<title>BI统计</title>
	<script src="//cdn.bootcss.com/jquery/2.2.0/jquery.min.js"></script>
	<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
	<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" rel="stylesheet">
	<style type="text/css">
	.popover {
             position: absolute;
             top: 0;
             left: 0;
             z-index: 1060;
             display: none;
             max-width: 1000000px;//我改为了1000000px,增大宽度
             padding: 1px;
             font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
             font-size: 14px;
             font-style: normal;
             font-weight: 400;
             line-height: 1.42857143;
             text-align: left;
             text-align: start;
             text-decoration: none;
             text-shadow: none;
             text-transform: none;
             letter-spacing: normal;
             word-break: normal;
             word-spacing: normal;
             word-wrap: normal;
             white-space: normal;
             background-color: #fff;
             -webkit-background-clip: padding-box;
             background-clip: padding-box;
             border: 1px solid #ccc;            
			 border: 1px solid rgba(0,0,0,.2);
             border-radius: 6px;
             -webkit-box-shadow: 0 5px 10px rgba(0,0,0,.2);
             box-shadow: 0 5px 10px rgba(0,0,0,.2);
             line-break: auto;
         }
        td{
        	padding: 3px;
        }
	</style>
</head>
	<h1 align="center">BI上课统计</h1>
	<p style="text-align: center;">
	时间筛选：<a href="${pageContext.request.contextPath}/mongo/groupRoomEvent?weekStatus=1" >全部</a>&nbsp;&nbsp;&nbsp;
	<a href="${pageContext.request.contextPath}/mongo/groupRoomEvent?weekStatus=-2" >上上周</a>&nbsp;&nbsp;&nbsp;
	<a href="${pageContext.request.contextPath}/mongo/groupRoomEvent?weekStatus=-1" >上周</a>&nbsp;&nbsp;&nbsp;
	<a href="${pageContext.request.contextPath}/mongo/groupRoomEvent?weekStatus=0" >本周</a>
	</p>
	<hr/>

	<table border="1" align="center">
	
	<tr align="center">
	<td rowspan="2">编号</td>
	<td rowspan="2">房间号</td>
	<td rowspan="2">开始时间</td>
	<td rowspan="2">课程名称</td>
	<td rowspan="2">老师姓名/编号</td>
	<td rowspan="2">学生姓名/编号</td>
	<td colspan="4">老师（次数）</td>
	<td colspan="4">学生（次数）</td>
	<td colspan="4">语音</td>
	</tr>
	
	<tr align="center">
	<td>进入</td>
	<td>退出</td>
	<td>强制退出</td>
	<td>断线退出</td>
	<td>进入</td>
	<td>退出</td>
	<td>强制退出</td>
	<td>断线退出</td>
	<td>打开</td>
	<td>关闭</td>
	<td>切换</td>
	<td>详情</td>
	</tr>
	
	<c:forEach var="roomEvent" items="${roomEvents}" varStatus="i">
	<tr align="center">
	<td>${i.count}</td>
    <!--td style="width:5%;"><button style="display:inline-block;width:100%"  data-toggle="popover" id="room${roomEvent.roomId}" value="${roomEvent.roomId}" onclick="test(${roomEvent.roomId})">${roomEvent.roomId}</button></td-->
    <td style="width:5%;"><a href="queryDetailInfo?roomId=${roomEvent.roomId}" target="_blank">${roomEvent.roomId}</a></td>
    <td>${roomEvent.lessionTimeRegion}</td>
    <td>${roomEvent.courseName}</td>
    <td><c:if test="${not empty roomEvent.teacherId}"><a href="groupRoomEvent?userId=${roomEvent.teacherId}&weekStatus=1" target="_blank">${roomEvent.teacherName}（${roomEvent.teacherId}）</a></c:if></td>
    <td><c:if test="${not empty roomEvent.studentId}"><a href="groupRoomEvent?userId=${roomEvent.studentId}&weekStatus=1" target="_blank">${roomEvent.studentName}（${roomEvent.studentId}）</a></c:if></td>
    <td>${roomEvent.teaEnterTimes}</td>
    <td>${roomEvent.teaExitTimes}</td>
    <td>${roomEvent.teaForceExitTimes}</td>
    <td>${roomEvent.teaReConnectExitTimes}</td>
    <td>${roomEvent.stuEnterTimes}</td>
    <td>${roomEvent.stuExitTimes}</td>
    <td>${roomEvent.stuForceExitTimes}</td>
    <td>${roomEvent.stuReConnectExitTimes}</td>
    <td>${roomEvent.openCount}</td>
    <td>${roomEvent.cancelCount}</td>
    <td>${roomEvent.channelSwitchCount}</td>
    <td><div style="width:400px;word-wrap:break-word;">${roomEvent.channelInfo}</div></td>
    </tr>
	</c:forEach>
	</table>
	<script>
    function test(roomId){
    	//通过ajax向后端请求数据
    	var text;
		  $.ajax({
	        url: '../../test/mongo/groupRtcEvent',
	        type: 'post',
	        data:{"roomId":roomId},
	        dataType: 'json',
	        async: false,
	        success: function (data){
	        if(data.code==1){
	      	  $.each(data.data,function (){
	      		  var info=this;
	      		  info.channelInfo=info.channelInfo==null?"暂无数据！":info.channelInfo;
	      			var html='<table class="table table-bordered"><tr><td>语音打开次数</td><td>语音关闭次数</td><td>频道切换（使用时间：分钟）</td></tr>'
	  	      		  text =html+ '<tr><td>'+info.openCount+'</td><td>'+info.cancelCount+'</td><td>'+info.channelInfo+'</td></tr>' +
	  			              '</table>'; 
	      		  })}else{
	      			  text="暂无数据！";
	      		  }
	      		  
	        	$('#room'+roomId).popover({title:'房间'+roomId,placement: 'top',html: 'true',content:text});
        }});
    }
    
  </script>
  
</html>