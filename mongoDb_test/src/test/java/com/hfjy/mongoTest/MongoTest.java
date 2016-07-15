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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hfjy.mongoTest.entity.RoomEventEntity;
import com.hfjy.mongoTest.entity.RtcEventEntity;
import com.hfjy.mongoTest.service.MongoDBService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/hfjy/mongoTest/spring.xml")
public class MongoTest {
	@Autowired
	private MongoDBService mongoDBService;

	@Test
	public void groupRoomEvent() {
		Map<String, Object> coMap = new HashMap<String, Object>();
		coMap.put("weekStatus", "0");
//		coMap.put("roomId", "16126");
		try {
			List<RoomEventEntity> data = mongoDBService.groupRoomEvent(coMap, "RoomEvent");
			int experienceLessons=0;
			int diagnosisLessons=0;
			int paidLessons=0;
			for (RoomEventEntity roomEventEntity : data) {
				String courseName = roomEventEntity.getCourseName();
				String lessionTimeRegion = roomEventEntity.getLessionTimeRegion();
				if (lessionTimeRegion.startsWith("07/14")) {
					if (courseName.indexOf("体验")>-1) {
						experienceLessons++;
					}else if (courseName.indexOf("诊断")>-1) {
						diagnosisLessons++;
					}else {
						paidLessons++;
					}
				}
			}
			System.out.println("experienceLessons:"+experienceLessons+">>>>>>diagnosisLessons:"+diagnosisLessons+">>>>>>>>paidLessons:"+paidLessons);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//@Test
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

	//@Test
	public void queryRtcEvent() {
		Map<String, Object> coMap = new HashMap<String, Object>();
		//coMap.put("weekStatus", "-1");
		coMap.put("roomId", "16126");
		try {
			List<RtcEventEntity> queryRtcEvent = mongoDBService.queryRtcEvent(coMap, "RtcEvent");
			System.out.println(JSON.toJSONString(queryRtcEvent, true));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//@Test
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
	
	//@Test
	public void testFindUsersInfoByRoomId() throws Exception{
		Map<String, Object> condition = new HashMap<>();
		condition.put("roomId", "13255");
		RoomEventEntity roomEventEntity  = (RoomEventEntity)mongoDBService.findUsersInfoByRoomId(condition, "RoomEvent");
		System.out.println(roomEventEntity.getStudentId()+":"+roomEventEntity.getStudentName());
	}
	
	@Test
	public void testStudyConditionReport() throws Exception{
		HashMap<String, Object> condition = new HashMap<>();
		int experienceLessons=0;
		int diagnosisLessons=0;
		int paidLessons=0;
		int reviewCount=0;
		HashMap<String, Object> result = new HashMap<>();
		//正在上课的
		List<RoomEventEntity> studyConditionReport = mongoDBService.studyConditionReport(condition,"2", "RoomEvent");
		for (RoomEventEntity roomEventEntity : studyConditionReport) {
			if (roomEventEntity.getTeacherName().indexOf("测试") > -1 || roomEventEntity.getStudentName().indexOf("测试") > -1) {
				continue;
			}
			if (roomEventEntity.getCourseName().indexOf("体验")>-1) {
				experienceLessons++;
			}else if (roomEventEntity.getCourseName().indexOf("诊断")>-1) {
				System.out.println("roomId>>>>>>>>>>>"+roomEventEntity.getRoomId());
				diagnosisLessons++;
			}else {
				paidLessons++;
			}
		}
		List<RoomEventEntity> reviewRoomEventEntity = mongoDBService.studyConditionReport(condition, "3", "RoomEvent");
		for (RoomEventEntity roomEventEntity : reviewRoomEventEntity) {
			if (roomEventEntity.getTeacherName().indexOf("测试") > -1 || roomEventEntity.getStudentName().indexOf("测试") > -1) {
				continue;
			}
			if (!roomEventEntity.getReviewTimes().isEmpty()) {
				reviewCount++;
			}
		}
		result.put("experienceLessons", experienceLessons);
		result.put("diagnosisLessons", diagnosisLessons);
		result.put("paidLessons", paidLessons);
		result.put("reviewCount", reviewCount);
		System.out.println(JSON.toJSONString(result, true));
	}

}
