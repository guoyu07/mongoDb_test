package com.hfjy.mongoTest.entity;

import java.io.Serializable;
import java.util.HashMap;
/**
 * 
 * @author leo-zeng
 *
 */
public class RoomEventEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 房间id
	 */
	private String roomId;
	/**
	 * 课程区间
	 */
	private String lessionTimeRegion; 
	/**
	 * 课程时间
	 */
	private HashMap<String, Object> lessonTimes;
	/*
	 * 上课的用户id集合key-userType,value-userId
	 */
	private HashMap<String, Object> userIds;
	/**
	 *学生名称
	 */
	private String studentName;
	/*
	 * 学生的userId
	 */
	private String studentId;
	/**
	 * 老师名称
	 */
	private String teacherName;
	/*
	 * 老师的userId
	 */
	private String teacherId;
	/**
	 * 课程名称
	 */
	private String courseName;
	/**
	 * 老师进入次数
	 */
	private Integer teaEnterTimes;
	/**
	 * 老师登出次数
	 */
	private Integer teaExitTimes;
	/**
	 * 老师强制登出次数
	 */
	private Integer teaForceExitTimes;
	/**
	 * 老师断线重连登出次数
	 */
	private Integer teaReConnectExitTimes;
	/**
	 * 学生登录次数
	 */
	private Integer stuEnterTimes;
	/**
	 * 学生登出次数
	 */
	private Integer stuExitTimes;
	/**
	 * 学生强制登出次数
	 */
	private Integer stuForceExitTimes;
	/**
	 * 学生断线重连登出次数
	 */
	private Integer stuReConnectExitTimes;
	
	/*
	 * 语音打开次数
	 */
	private Integer openCount;
	/**
	 * 关闭频道次数
	 */
	private Integer cancelCount;
	/*
	 * 切换频道次数
	 */
	private Integer channelSwitchCount;
	/*
	 *切换频道详情 
	 */
	private String channelInfo;
	
	/**
	 * 回顾的次数
	 */
	private HashMap<String, Object> reviewTimes;
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public HashMap<String, Object> getLessonTimes() {
		return lessonTimes;
	}
	public void setLessonTimes(HashMap<String, Object> lessonTimes) {
		this.lessonTimes = lessonTimes;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getLessionTimeRegion() {
		return lessionTimeRegion;
	}
	public void setLessionTimeRegion(String lessionTimeRegion) {
		this.lessionTimeRegion = lessionTimeRegion;
	}
	public Integer getTeaEnterTimes() {
		return teaEnterTimes;
	}
	public void setTeaEnterTimes(Integer teaEnterTimes) {
		this.teaEnterTimes = teaEnterTimes;
	}
	public Integer getTeaExitTimes() {
		return teaExitTimes;
	}
	public void setTeaExitTimes(Integer teaExitTimes) {
		this.teaExitTimes = teaExitTimes;
	}
	public Integer getTeaForceExitTimes() {
		return teaForceExitTimes;
	}
	public void setTeaForceExitTimes(Integer teaForceExitTimes) {
		this.teaForceExitTimes = teaForceExitTimes;
	}
	public Integer getTeaReConnectExitTimes() {
		return teaReConnectExitTimes;
	}
	public void setTeaReConnectExitTimes(Integer teaReConnectExitTimes) {
		this.teaReConnectExitTimes = teaReConnectExitTimes;
	}
	public Integer getStuEnterTimes() {
		return stuEnterTimes;
	}
	public void setStuEnterTimes(Integer stuEnterTimes) {
		this.stuEnterTimes = stuEnterTimes;
	}
	public Integer getStuExitTimes() {
		return stuExitTimes;
	}
	public void setStuExitTimes(Integer stuExitTimes) {
		this.stuExitTimes = stuExitTimes;
	}
	public Integer getStuForceExitTimes() {
		return stuForceExitTimes;
	}
	public void setStuForceExitTimes(Integer stuForceExitTimes) {
		this.stuForceExitTimes = stuForceExitTimes;
	}
	public Integer getStuReConnectExitTimes() {
		return stuReConnectExitTimes;
	}
	public void setStuReConnectExitTimes(Integer stuReConnectExitTimes) {
		this.stuReConnectExitTimes = stuReConnectExitTimes;
	}
	public Integer getOpenCount() {
		return openCount;
	}
	public void setOpenCount(Integer openCount) {
		this.openCount = openCount;
	}
	public Integer getCancelCount() {
		return cancelCount;
	}
	public void setCancelCount(Integer cancelCount) {
		this.cancelCount = cancelCount;
	}
	public Integer getChannelSwitchCount() {
		return channelSwitchCount;
	}
	public void setChannelSwitchCount(Integer channelSwitchCount) {
		this.channelSwitchCount = channelSwitchCount;
	}
	public String getChannelInfo() {
		return channelInfo;
	}
	public void setChannelInfo(String channelInfo) {
		this.channelInfo = channelInfo;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public HashMap<String, Object> getUserIds() {
		return userIds;
	}
	public HashMap<String, Object> getReviewTimes() {
		return reviewTimes;
	}
	public void setReviewTimes(HashMap<String, Object> reviewTimes) {
		this.reviewTimes = reviewTimes;
	}
	public void setUserIds(HashMap<String, Object> userIds) {
		this.userIds = userIds;
	}
	
}
