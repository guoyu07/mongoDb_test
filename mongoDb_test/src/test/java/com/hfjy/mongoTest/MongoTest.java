/**
 * Project Name:mongoDb_test
 * File Name:MongoTest.java
 * Package Name:com.hfjy.mongoTest
 * Date:2016年5月23日下午3:18:25
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */
/**
 * 海风app在线学习平台
 * @author: no_relax
 * @Title: MongoTest.java 
 * @Package: com.hfjy.mongoTest
 * @date: 2016年5月23日-下午3:18:25
 * @version: Vphone1.3.0
 * @copyright: 2016上海风创信息咨询有限公司-版权所有
 * 
 */

package com.hfjy.mongoTest;

/** 
 * @ClassName: MongoTest 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author no_relax 
 * @date 2016年5月23日 下午3:18:25 
 *  
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hfjy.mongoTest.bean.Condition;
import com.hfjy.mongoTest.bean.DateType;
import com.hfjy.mongoTest.entity.RoomEventDetail;
import com.hfjy.mongoTest.entity.RoomEventEntity;
import com.hfjy.mongoTest.entity.RtcEventDetail;
import com.hfjy.mongoTest.entity.RtcEventEntity;
import com.hfjy.mongoTest.mongodb.MongoDBManager;
import com.hfjy.mongoTest.service.MongoDBService;
import com.hfjy.mongoTest.utils.DateUtils;
import com.hfjy.mongoTest.utils.StringUtils;
import com.hfjy.service.xue.mail.SendCloudService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/hfjy/mongoTest/spring.xml")
public class MongoTest {
	@Autowired
	private MongoDBService mongoDBService;

	// @Test
	public void groupRoomEvent() {
		Map<String, Object> coMap = new HashMap<String, Object>();
		coMap.put("weekStatus", "0");
		// coMap.put("roomId", "16126");
		try {
			List<RoomEventEntity> data = mongoDBService.groupRoomEvent(coMap, "RoomEvent");
			int experienceLessons = 0;
			int diagnosisLessons = 0;
			int paidLessons = 0;
			for (RoomEventEntity roomEventEntity : data) {
				String courseName = roomEventEntity.getCourseName();
				String lessionTimeRegion = roomEventEntity.getLessionTimeRegion();
				if (lessionTimeRegion.startsWith("07/14")) {
					if (courseName.indexOf("体验") > -1) {
						experienceLessons++;
					} else if (courseName.indexOf("诊断") > -1) {
						diagnosisLessons++;
					} else {
						paidLessons++;
					}
				}
			}
			System.out.println("experienceLessons:" + experienceLessons + ">>>>>>diagnosisLessons:" + diagnosisLessons + ">>>>>>>>paidLessons:" + paidLessons);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Test
	public void distinctQueryRoomId() {
		Map<String, Object> coMap = new HashMap<String, Object>();
		coMap.put("userId", "22171");
		try {
			List<String> roomIds = mongoDBService.distinctQueryRoomId(coMap, "RoomEvent");
			System.out.println(JSON.toJSONString(roomIds, true));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Test
	public void queryRtcEvent() {
		Map<String, Object> coMap = new HashMap<String, Object>();
		coMap.put("weekStatus", "0");
		// coMap.put("roomId", "16126");
		try {
			List<RtcEventEntity> queryRtcEvent = mongoDBService.queryRtcEvent(coMap, "RtcEvent");
			System.out.println(JSON.toJSONString(queryRtcEvent, true));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Test
	public void queryRoomUsersInfo() throws Exception {
		Map<String, Object> condition = new HashMap<>();
		Map<String, Object> cond = new HashMap<String, Object>();
		Map<String, Object> initial = new HashMap<String, Object>();
		// 承载用户的userId
		initial.put("userIds", new HashMap<String, Object>());// key-userType,value-userId
		initial.put("studentId", new String());
		initial.put("teacherId", new String());
		initial.put("studentName", new String());
		initial.put("teacherName", new String());
		cond.put("roomId", "13512");
		condition.put("key", "roomId");
		condition.put("initial", initial);
		condition.put("cond", cond);
		// System.out.println(JSON.toJSONString(condition, true));
		// RoomEventEntity queryRoomUsersInfo =
		// mongoDBService.queryRoomUsersInfo(condition, "RoomEvent");
		// System.out.println(JSON.toJSONString(queryRoomUsersInfo, true));
	}

	// @Test
	public void testFindUsersInfoByRoomId() throws Exception {
		Map<String, Object> condition = new HashMap<>();
		condition.put("roomId", "13255");
		RoomEventEntity roomEventEntity = (RoomEventEntity) mongoDBService.findUsersInfoByRoomId(condition, "RoomEvent");
		System.out.println(roomEventEntity.getStudentId() + ":" + roomEventEntity.getStudentName());
	}

	public int reportStudyInterrupt(String roomId) throws Exception {
		int studyInterruptTimes = 0;
		Map<String, Object> con = new HashMap<String, Object>();
		con.put("roomId", roomId);
		List<RoomEventDetail> roomEventDetails = mongoDBService.queryRoomEventDetail(con, "RoomEvent");
		long endTime = 0;
		long startTime = 0;
		List<String> list = new ArrayList<>();
		for (RoomEventDetail roomEventDetail : roomEventDetails) {
			String event = roomEventDetail.getEvent();
			list.add(event);
		}
		if (list.contains("结束上课")) {
			int i = list.indexOf("结束上课");
			endTime = Long.parseLong(roomEventDetails.get(i).getInsertTime());
		} else if (list.contains("退出")) {
			int i = list.indexOf("退出");
			endTime = Long.parseLong(roomEventDetails.get(i).getInsertTime());
		}
		if (list.contains("开始上课")) {
			int i = list.indexOf("开始上课");
			startTime = Long.parseLong(roomEventDetails.get(i).getInsertTime());
		}

		if ((endTime - startTime) > 3600000) {
			studyInterruptTimes++;
		}
		return studyInterruptTimes;
	}

	/**
	 * TODO(统计回顾时间)
	 * 
	 * @author: no_relax
	 * @Title: reportReviewTimes
	 * @param eventDescs
	 * @param eventTimes
	 * @return List<Object>
	 * @since Vphone1.3.0
	 */
	public List<Object> reportReviewTimes(List<String> eventDescs, List<String> eventTimes) {
		List<Object> reviewTimes = new ArrayList<>();
		for (int i = 0; i < eventDescs.size(); i++) {
			if (i + 1 < eventDescs.size()) {
				if (eventDescs.get(i).equals("进入") && eventDescs.get(i + 1).equals("退出")) {
					String enterTime = eventTimes.get(i);
					String existTime = eventTimes.get(i + 1);
					reviewTimes.add((Long.parseLong(existTime) - Long.parseLong(enterTime)) / (1000 * 60));
				}
			}
		}
		return reviewTimes;
	}

	public void sendMailReport(String date) throws Exception {
		Map<String, Object> studyConditionReport = getStudyConditionReport(date);
		System.out.println(JSONObject.toJSONString(studyConditionReport, true));
		// 准备邮件格式
		StringBuffer sb = new StringBuffer();
		sb.append("<table border=\"1\" >");
		sb.append("<tr>");
		sb.append("<td>时间</td>");
		sb.append("<td>体验课</td>");
		sb.append("<td>诊断课</td>");
		sb.append("<td>正式课</td>");
		sb.append("<td>回顾数量</td>");
		sb.append("<td>疑似转QQ语音（超出10分钟</td>");
		sb.append("<td>疑似转屏幕分享（系统上课不足1小时）</td>");
		sb.append("<td>没有使用语音</td>");
		sb.append("<td>回顾上课时间</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td>" + date + "</td>");
		sb.append("<td>" + studyConditionReport.get("experienceLessons") + "</td>");
		sb.append("<td>" + studyConditionReport.get("diagnosisLessons") + "</td>");
		sb.append("<td>" + studyConditionReport.get("paidLessons") + "</td>");
		sb.append("<td>" + studyConditionReport.get("reviewCount") + "</td>");
		sb.append("<td>" + studyConditionReport.get("userQqVoice") + "</td>");
		sb.append("<td>" + studyConditionReport.get("studyInterruptTimes") + "</td>");
		sb.append("<td>" + studyConditionReport.get("noUseVoice") + "</td>");
		sb.append("<td>" + studyConditionReport.get("reviewTimes") + "</td>");
		sb.append("</tr>");
		sb.append("</table>");
		// 调用发送邮件方法
		System.out.println(SendCloudService.sendStudyConditionReport(sb.toString()));
	}

	private Map<String, Object> getStudyConditionReport(String date) throws Exception {
		HashMap<String, Object> condition = new HashMap<>();
		int experienceLessons = 0;
		int diagnosisLessons = 0;
		int paidLessons = 0;
		int reviewCount = 0;
		// 回顾的时间间隔
		List<Object> reviewTimes = new ArrayList<>();
		// 使用QQ语音超过10分钟课程数量
		int userQqVoice = 0;
		// 上课不超过一个小时次数
		int studyInterruptTimes = 0;
		int noUseVoice = 0;
		HashMap<String, Object> result = new HashMap<>();
		// 正在上课的
		List<RoomEventEntity> studyConditionReport = mongoDBService.studyConditionReport(condition, "2", date, "RoomEvent");
		Map<String, Object> coMap = new HashMap<String, Object>();
		for (RoomEventEntity roomEventEntity : studyConditionReport) {
			if (roomEventEntity.getTeacherName().indexOf("测试") > -1 || roomEventEntity.getStudentName().indexOf("测试") > -1) {
				continue;
			}
			if (roomEventEntity.getCourseName().indexOf("体验") > -1) {
				experienceLessons++;
			} else if (roomEventEntity.getCourseName().indexOf("诊断") > -1) {
				diagnosisLessons++;
			} else {
				paidLessons++;
				// 获取roomId
				String roomId = roomEventEntity.getRoomId();
				coMap.put("roomId", roomId);
				List<RtcEventEntity> queryRtcEvents = mongoDBService.queryRtcEvent(coMap, "RtcEvent");
				if (queryRtcEvents.size() > 0) {
					RtcEventEntity rtcEventEntity = queryRtcEvents.get(0);
					if (StringUtils.validateCollectionItemsIsSameOrNot(Arrays.asList(rtcEventEntity.getOperateDesc()), "关闭")) {
						noUseVoice++;
						continue;
					}
					String channelInfo = rtcEventEntity.getChannelInfo();
					String[] channelInfos = channelInfo.split(",");
					String lastChannelInfo = channelInfos[channelInfos.length - 1];
					if (lastChannelInfo.contains("QQ")) {
						// 取出最后一个语音信息
						String qqTime = lastChannelInfo.substring(3, lastChannelInfo.length() - 1);
						if (Double.parseDouble(qqTime) > 10) {
							userQqVoice++;
						}
					}
				}
				// 上课在一小时以内统计
				studyInterruptTimes = reportStudyInterrupt(roomId);
			}
		}
		List<RoomEventEntity> reviewRoomEventEntity = mongoDBService.studyConditionReport(condition, "3", date, "RoomEvent");
		for (RoomEventEntity roomEventEntity : reviewRoomEventEntity) {
			if (roomEventEntity.getTeacherName().indexOf("测试") > -1 || roomEventEntity.getStudentName().indexOf("测试") > -1) {
				continue;
			}
			List<String> eventDescs = Arrays.asList(roomEventEntity.getEventDescs());
			List<String> eventTimes = Arrays.asList(roomEventEntity.getEventTimes());
			if (eventDescs.contains("进入")) {
				reviewCount++;
				// System.out.println(">>>>>>>>>>>>>" +
				// roomEventEntity.getRoomId());
				List<Object> reviewTime = reportReviewTimes(eventDescs, eventTimes);
				reviewTimes.addAll(reviewTime);
			}
		}
		reviewTimes.sort(null);
		result.put("experienceLessons", experienceLessons);
		result.put("diagnosisLessons", diagnosisLessons);
		result.put("paidLessons", paidLessons);
		result.put("reviewCount", reviewCount);
		result.put("userQqVoice", userQqVoice);
		result.put("studyInterruptTimes", studyInterruptTimes);
		result.put("noUseVoice", noUseVoice);
		result.put("reviewTimes", reviewTimes);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < reviewTimes.size(); i++) {
			sb.append(reviewTimes.get(i) + ";");
		}
		System.out.println(sb.toString());
		System.out.println(JSON.toJSONString(result, true));
		return result;
	}

	@Test
	public void test() {
		MongoDBManager mongoDBManager = new MongoDBManager("admin", "RtcEvent");
		Condition cond = Condition.init();
		Condition cond2 = Condition.init();
		Pattern pattern = Pattern.compile("^.*测试.*$", Pattern.CASE_INSENSITIVE);
		cond.like("userName", pattern.toString());
		cond2.not(cond);
		cond2.is("status", "2");
		Collection<RtcEventDetail> rtcEventDetails = mongoDBManager.find(cond2, RtcEventDetail.class);
		for (RtcEventDetail rtcEventDetail : rtcEventDetails) {
			System.out.println(rtcEventDetail.getUserName());
		}
	}

	// 老师回顾情况统计
	@Test
	public void saveUserRoomEvent() throws Exception {
		// MongoDBManager mongoDBManager = new MongoDBManager("admin",
		// "RoomEvent");
		// BasicDBObject condition = new BasicDBObject();// 条件
		// condition.append("roomId", "144501");
		// BasicDBObject key = new BasicDBObject("userId", 1);// 指定需要显示列
		// key.append("roomId", 1);
		// key.append("insertTime", 1);
		// key.append("event", 1);
		// key.append("userType", 1);
		// key.append("_id", 0);
		// Collection<JSONObject> roomEventDetails =
		// mongoDBManager.find(condition, key, JSONObject.class);
		// if (roomEventDetails.isEmpty()) {
		// Log.info("lesson_plan_id:" + "在mongodb中无RoomEvent信息！");
		// return;
		// }
		// List<Object[]> insertlist = new ArrayList<Object[]>();
		// for (JSONObject jsonObject : roomEventDetails) {
		// Integer roomId = Integer.parseInt(jsonObject.getString("roomId"));
		// Integer userId = Integer.parseInt(jsonObject.getString("userId"));
		// Integer userType =
		// Integer.parseInt(jsonObject.getString("userType"));
		// Date insertTime = new Date(jsonObject.getLong("insertTime"));
		// String event = jsonObject.getString("event");
		// insertlist.add(new Object[] { roomId, userId, userType, insertTime,
		// event });
		//
		// }
		// DB db = DB.getDB();
		// db.batchUpdate("insert into
		// analysis_user_room_event(lesson_plan_id,user_id,user_type,insert_time,event)
		// values(?,?,?,?,?)", insertlist);
		Map<String, Object> resMap = new HashMap<>();
		try {
			mongoDBService.saveUserRoomEvent();
			resMap.put("desc", "保存用户RoomEvent成功！");
		} catch (Exception e) {
			resMap.put("desc", "保存用户RoomEvent失败！" + e.getMessage());
		}
		System.out.println(JSON.toJSONString(resMap, true));

	}

	@Test
	public void exportReport() {
		try {
			System.out.println(mongoDBService.exportReport(null, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 周一运行此方法，发送上周每天上课情况邮件
	@Test
	public void testDate() throws Exception {
		for (int i = -7; i < 0; i++) {
			String formatDate = DateUtils.formatDate(DateUtils.nextDate(new Date(), DateType.DAY, i), "yyyy-MM-dd");
			sendMailReport(formatDate);
		}
	}
}
