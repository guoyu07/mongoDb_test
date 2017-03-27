package com.hfjy.mongoTest.service;

import java.util.List;
import java.util.Map;

import com.hfjy.mongoTest.entity.RoomEventDetail;
import com.hfjy.mongoTest.entity.RoomEventEntity;
import com.hfjy.mongoTest.entity.RtcEventDetail;
import com.hfjy.mongoTest.entity.RtcEventEntity;

public interface MongoDBService {
	/**
	 * 
	 * @param condition
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	List<RtcEventDetail> queryRtcEventDetail(Map<String, Object> condition, String collectionName) throws Exception;

	/**
	 * TODO(查询RoomEvent详情信息)
	 * 
	 * @author: no_relax
	 * @Title: queryRoomEventDetail
	 * @param condition
	 * @param collectionName
	 * @return List<RoomEventDetail>
	 * @throws Exception
	 * @since Vphone1.3.0
	 */
	List<RoomEventDetail> queryRoomEventDetail(Map<String, Object> condition, String collectionName) throws Exception;

	/**
	 * 
	 * @param condition
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	List<Object> groupByLessonCount(Map<String, Object> condition, String collectionName) throws Exception;

	/**
	 * 根据RoomId 查询 数据
	 * 
	 * @param condition：需要查询使用的
	 *            roomId 和 status
	 * @param collectionName
	 *            表名
	 * @return
	 * @throws Exception
	 */
	List<RoomEventEntity> groupRoomEvent(Map<String, Object> condition, String collectionName) throws Exception;

	/**
	 * 根据RoomId查询数据
	 * 
	 * @param condition
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	List<RtcEventEntity> queryRtcEvent(Map<String, Object> condition, String collectionName) throws Exception;

	/**
	 * TODO(去重查询roomId集合)
	 * 
	 * @author: no_relax
	 * @Title: distinctQueryRoomId
	 * @param condition
	 * @param collectionName
	 * @return List<String>
	 * @throws Exception
	 * @since Vphone1.3.0
	 */
	List<String> distinctQueryRoomId(Map<String, Object> condition, String collectionName) throws Exception;

	/**
	 * TODO(获取房间中所有的用户信息)
	 * 
	 * @author: no_relax
	 * @Title: findUsersInfoByRoomId
	 * @param condition
	 * @param collectionName
	 * @return RoomEventEntity
	 * @throws Exception
	 * @since Vphone1.3.0
	 */
	RoomEventEntity findUsersInfoByRoomId(Map<String, Object> condition, String collectionName) throws Exception;

	/**
	 * TODO(上课情况统计)
	 * 
	 * @author: no_relax
	 * @Title: studyConditionReport
	 * @param condition
	 * @param collectionName
	 * @return RoomEventEntity
	 * @throws Exception
	 * @since Vphone1.3.0
	 */
	List<RoomEventEntity> studyConditionReport(Map<String, Object> condition, String status, String date, String collectionName) throws Exception;

	void saveUserRoomEvent() throws Exception;

	String exportReport(String startTime, String endTime) throws Exception;
}
