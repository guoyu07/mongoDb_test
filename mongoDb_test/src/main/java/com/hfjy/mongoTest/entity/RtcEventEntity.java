package com.hfjy.mongoTest.entity;

import java.io.Serializable;

public class RtcEventEntity implements Serializable{
	/**
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 房间id
	 */
	private String roomId;
	/**
	 * 打开频道次数
	 */
	private Integer openCount;
	/**
	 * 关闭频道次数
	 */
	private Integer cancelCount;
	/**
	 * 切换的频道
	 */
	private String[] channelSwitch;
	/**
	 * 切换频道使用时间
	 */
	private Double[] channelSwitchTimes;
	/**
	 * 最后一次切换时间
	 */
	private Long lastTime;
	/**
	 * 第一次开启时间
	 */
	private Double fristTime;
	/**
	 * 频道切换信息
	 */
	private String channelInfo;
	
	public String getChannelInfo() {
		return channelInfo;
	}
	public void setChannelInfo(String channelInfo) {
		this.channelInfo = channelInfo;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
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
	public String[] getChannelSwitch() {
		return channelSwitch;
	}
	public void setChannelSwitch(String[] channelSwitch) {
		this.channelSwitch = channelSwitch;
	}
	public Double[] getChannelSwitchTimes() {
		return channelSwitchTimes;
	}
	public void setChannelSwitchTimes(Double[] channelSwitchTimes) {
		this.channelSwitchTimes = channelSwitchTimes;
	}
	public Long getLastTime() {
		return lastTime;
	}
	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}
	public Double getFristTime() {
		return fristTime;
	}
	public void setFristTime(Double fristTime) {
		this.fristTime = fristTime;
	}
}
