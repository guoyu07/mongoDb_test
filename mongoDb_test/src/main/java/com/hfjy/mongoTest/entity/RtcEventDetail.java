package com.hfjy.mongoTest.entity;

import java.io.Serializable;

public class RtcEventDetail implements Serializable{
	
	/**"insertTime" : NumberLong(1461749342854),
    "userType" : "1",
    "source" : "acrtc",
    "postfix" : "749342853",
    "label" : "频道1",
    "userName" : "测试老师",
    "userId" : "22125",
    "roomId" : "13540",
    "isChange" : false,
    "status" : "1",
    "desc" : "打开"
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private java.lang.String insertTime;
	private java.lang.Long userType;
	private java.lang.String source;
	private java.lang.String postfix;
	private java.lang.String label;
	private java.lang.String userName;
	private java.lang.String userId;
	private java.lang.String roomId;
	private java.lang.String isChange;
	private java.lang.String status;
	private java.lang.String desc;
	
	public java.lang.String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(java.lang.String insertTime) {
		this.insertTime = insertTime;
	}
	public java.lang.Long getUserType() {
		return userType;
	}
	public void setUserType(java.lang.Long userType) {
		this.userType = userType;
	}
	public java.lang.String getSource() {
		return source;
	}
	public void setSource(java.lang.String source) {
		this.source = source;
	}
	public java.lang.String getPostfix() {
		return postfix;
	}
	public void setPostfix(java.lang.String postfix) {
		this.postfix = postfix;
	}
	public java.lang.String getLabel() {
		return label;
	}
	public void setLabel(java.lang.String label) {
		this.label = label;
	}
	public java.lang.String getUserName() {
		return userName;
	}
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}
	public java.lang.String getUserId() {
		return userId;
	}
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	public java.lang.String getRoomId() {
		return roomId;
	}
	public void setRoomId(java.lang.String roomId) {
		this.roomId = roomId;
	}
	public java.lang.String getIsChange() {
		return isChange;
	}
	public void setIsChange(java.lang.String isChange) {
		this.isChange = isChange;
	}
	public java.lang.String getStatus() {
		return status;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	public java.lang.String getDesc() {
		return desc;
	}
	public void setDesc(java.lang.String desc) {
		this.desc = desc;
	}


}