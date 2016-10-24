package com.hfjy.mongoTest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hfjy.base.core.Log;
import com.hfjy.mongoTest.entity.RoomEventDetail;
import com.hfjy.mongoTest.entity.RoomEventEntity;
import com.hfjy.mongoTest.entity.RtcEventDetail;
import com.hfjy.mongoTest.entity.RtcEventEntity;
import com.hfjy.mongoTest.service.MongoDBService;
import com.hfjy.mongoTest.utils.DateUtils;
import com.hfjy.mongoTest.utils.StringUtils;
import com.hfjy.service.xue.mail.SendCloudService; 

/**
 * mongoDB 查询的controller
 * 
 * @author no_relax
 *
 */
@Controller
@RequestMapping("mongo")
public class MongoDBController {
	@Autowired
	private MongoDBService mongoDBService;
	//private static final Logger log = Logger.getLogger(MongoDBController.class);

	/**
	 * 查询维度，根据roomId查询房间具体日志信息
	 * 
	 * @return
	 */
	@RequestMapping("queryDetailInfo")
	public String queryDetailInfo(@RequestParam(required = false, value = "roomId") String roomId, Model model) {
		Map<String, Object> coMap = new HashMap<String, Object>();
		coMap.put("roomId", roomId);
		try {
			List<RtcEventDetail> rctEvents = mongoDBService.queryRtcEventDetail(coMap, "RtcEvent");
			for (RtcEventDetail rtcEventDetail : rctEvents) {
				rtcEventDetail.setInsertTime(DateUtils.formatDate(new Date(Long.parseLong(rtcEventDetail.getInsertTime())), "MM/dd HH:mm:ss"));
			}
			// 获取roomEvent详细信息
			List<RoomEventDetail> roomEvents = mongoDBService.queryRoomEventDetail(coMap, "RoomEvent");
			for (RoomEventDetail roomEventDetail : roomEvents) {
				roomEventDetail.setInsertTime(DateUtils.formatDate(new Date(Long.parseLong(roomEventDetail.getInsertTime())), "MM/dd HH:mm:ss"));
			}
			model.addAttribute("rctEvents", rctEvents);
			model.addAttribute("roomEvents", roomEvents);
		} catch (Exception e) {
			e.printStackTrace();
			Log.warn(e,e.getMessage());
		}
		return "modules/bi/detailInfoLogs";
	}

	/**
	 * TODO(group分组查询)
	 * 
	 * @author: no_relax
	 * @Title: group
	 * @return Object
	 * @since Vphone1.3.0
	 */
	@RequestMapping("group")
	@ResponseBody
	public Object group() {
		Map<String, Object> coMap = new HashMap<String, Object>();
		Map<String, Object> initial = new HashMap<String, Object>();
		coMap.put("key", "roomId");
		initial.put("teachers", new HashMap<String, Object>());
		initial.put("students", new HashMap<String, Object>());
		coMap.put("initial", initial);
		coMap.put("cond", new HashMap<String, Object>());
		List<Object> data = new ArrayList<>();
		try {
			data = mongoDBService.groupByLessonCount(coMap, "lessonCountLog");
		} catch (Exception e) {
			Log.warn(e,e.getMessage());
			e.getMessage();
		}
		return data;
	}

	/**
	 * 查询
	 * 
	 * @param roomId
	 * @return
	 */
	@RequestMapping("groupRoomEvent")
	public String groupRoomEvent(@RequestParam(required = false, value = "roomId") String roomId, Model model, @RequestParam(required = false, value = "weekStatus") String weekStatus,
			@RequestParam(required = false, value = "userId") String userId, @RequestParam(required = false, value = "optType") String optType) {
		Map<String, Object> coMap = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(roomId)) {
			coMap.put("roomId", roomId);
		}
		// 为空时，设置为默认值为-1，上周
		weekStatus = StringUtils.isNotEmpty(weekStatus) ? weekStatus : "-1";
		coMap.put("weekStatus", weekStatus);
		List<RoomEventEntity> data = new ArrayList<>();
		try {
			// 如果userId不为空，表示以userId维度查询，需要另外处理
			if (StringUtils.isNotEmpty(userId)) {
				coMap.put("userId", userId);
				coMap.remove("weekStatus");
				// 根据用户的userId获取所有的roomId
				List<String> roomIds = mongoDBService.distinctQueryRoomId(coMap, "RoomEvent");
				for (String roomIdStr : roomIds) {
					coMap.put("roomId", roomIdStr);
					coMap.put("weekStatus", weekStatus);
					coMap.remove("userId");
					List<RoomEventEntity> events = mongoDBService.groupRoomEvent(coMap, "RoomEvent");
					if (events != null && events.size() > 0) {
						RoomEventEntity roomEventEntity = events.get(0);
						data.add(roomEventEntity);
					}
				}
			} else {
				coMap.put("conditions", true);
				data = mongoDBService.groupRoomEvent(coMap, "RoomEvent");
			}
			model.addAttribute("roomEvents", data);
		} catch (Exception e) {
			e.printStackTrace();
			Log.warn(e,e.getMessage());
		}
		return "modules/bi/bi";
	}

	/**
	 * TODO(group查询语音情况)
	 * 
	 * @author: no_relax
	 * @Title: groupRtcEvent
	 * @param roomId
	 *            以roomId维度查询
	 * @return Object
	 * @since BI
	 */
	@RequestMapping("groupRtcEvent")
	@ResponseBody
	public Object groupRtcEvent(@RequestParam(required = false, value = "roomId") String roomId) {
		Map<String, Object> coMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<>();
		if (StringUtils.isNotEmpty(roomId)) {
			coMap.put("roomId", roomId);
		}
		coMap.put("conditions", true);
		List<RtcEventEntity> data = new ArrayList<RtcEventEntity>();
		try {
			data = mongoDBService.queryRtcEvent(coMap, "RtcEvent");
			if (data != null && data.size() > 0) {
				dataMap.put("code", 1);
				dataMap.put("data", data);
			} else {
				dataMap.put("code", 0);
			}
		} catch (Exception e) {
			Log.warn(e,e.getMessage());
			e.printStackTrace();
		}
		return dataMap;
	}

	@RequestMapping("sendStudyConditionReport")
	@ResponseBody
	public Object sendStudyConditionReport() throws Exception {
		Map<String, Object> studyConditionReport = getStudyConditionReport();
		// 准备邮件格式
		StringBuffer sb = new StringBuffer();
		sb.append("<table border=\"1\" >");
		sb.append("<tr>");
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
		Map<String, Object> resMap=new HashMap<>();
		resMap.put("desc", SendCloudService.sendStudyConditionReport(sb.toString()));
		return resMap;
	}

	
	private Map<String, Object> getStudyConditionReport() throws Exception {
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
		List<RoomEventEntity> studyConditionReport = mongoDBService.studyConditionReport(condition, "2", "RoomEvent");
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
		List<RoomEventEntity> reviewRoomEventEntity = mongoDBService.studyConditionReport(condition, "3", "RoomEvent");
		for (RoomEventEntity roomEventEntity : reviewRoomEventEntity) {
			if (roomEventEntity.getTeacherName().indexOf("测试") > -1 || roomEventEntity.getStudentName().indexOf("测试") > -1) {
				continue;
			}
			List<String> eventDescs = Arrays.asList(roomEventEntity.getEventDescs());
			List<String> eventTimes = Arrays.asList(roomEventEntity.getEventTimes());
			if (eventDescs.contains("进入")) {
				reviewCount++;
				System.out.println(">>>>>>>>>>>>>" + roomEventEntity.getRoomId());
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
//		System.out.println(sb.toString());
//		System.out.println(JSON.toJSONString(result, true));
		return result;
	}

	private int reportStudyInterrupt(String roomId) throws Exception {
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
		} else {
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
	private List<Object> reportReviewTimes(List<String> eventDescs, List<String> eventTimes) {
		List<Object> reviewTimes = new ArrayList<>();
		for (int i = 0; i < eventDescs.size(); i++) {
			if (i + 1 < eventDescs.size()) {
				if (eventDescs.get(i).equals("进入") && eventDescs.get(i + 1).equals("退出")) {
					String enterTime = eventTimes.get(i);
					String existTime = eventTimes.get(i + 1);
//					System.out.println(">>>>>>>" + (Long.parseLong(existTime) - Long.parseLong(enterTime)) / (1000 * 60));
					reviewTimes.add((Long.parseLong(existTime) - Long.parseLong(enterTime)) / (1000 * 60));
				}
			}
		}
		return reviewTimes;
	}

}
