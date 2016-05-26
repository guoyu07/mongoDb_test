/**
 * Project Name:mongoDb_test
 * File Name:RoomEventDetail.java
 * Package Name:com.hfjy.mongoTest.entity
 * Date:2016年5月24日上午10:26:39
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
*/
/**
 * 海风app在线学习平台
 * @author: no_relax
 * @Title: RoomEventDetail.java 
 * @Package: com.hfjy.mongoTest.entity
 * @date: 2016年5月24日-上午10:26:39
 * @version: Vphone1.3.0
 * @copyright: 2016上海风创信息咨询有限公司-版权所有
 * 
 */

package com.hfjy.mongoTest.entity;

import java.io.Serializable;

/** 
* @ClassName: RoomEventDetail 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author no_relax 
* @date 2016年5月24日 上午10:26:39
* "_id" : ObjectId("571f0e371cb7116206c0e260"),
    "statusDesc" : "已备课",
    "clientName" : "",
    "userName" : "测试老师B",
    "osName" : "Macintosh; Intel Mac OS X 10_10_5",
    "clientVersion" : "",
    "userId" : "22171",
    "roomId" : "13512",
    "insertTime" : NumberLong(1461653047615),
    "courseName" : "高三地理同步提分（线上）",
    "browserName" : "Chrome/47.0.2526.106 Safari/537.36",
    "userType" : "1",
    "messageSize" : 0,
    "event" : "进入",
    "status" : "4" 
*  
*/
public class RoomEventDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private String statusDesc;
	private String clientName;
	private String userName;
	private String osName;
	private String clientVersion;
	private String userId;
	private String roomId;
	private String insertTime;
	private String courseName;
	private String browserName;
	private String userType;
	private String messageSize;
	private String event;
	private String status;
	
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	public String getClientVersion() {
		return clientVersion;
	}
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getBrowserName() {
		return browserName;
	}
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMessageSize() {
		return messageSize;
	}
	public void setMessageSize(String messageSize) {
		this.messageSize = messageSize;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}

