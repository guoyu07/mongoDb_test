package com.hfjy.mongoTest.service.impl;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hfjy.mongoTest.bean.Condition;
import com.hfjy.mongoTest.entity.RoomEventDetail;
import com.hfjy.mongoTest.entity.RoomEventEntity;
import com.hfjy.mongoTest.entity.RtcEventDetail;
import com.hfjy.mongoTest.entity.RtcEventEntity;
import com.hfjy.mongoTest.mongodb.MongoDBManager;
import com.hfjy.mongoTest.service.MongoDBService;
import com.hfjy.mongoTest.utils.DateUtils;
import com.hfjy.mongoTest.utils.StringUtils;

@Service(value="MongoDBService")
public class MongoDBServiceImpl implements MongoDBService{
	/**
	 * 默认数据库
	 */
	private static final String dataBase ="admin";
	
	private MongoDBManager mongoDBManager=null;
	private static final Logger log=Logger.getLogger(MongoDBServiceImpl.class);
	@Override
	public List<RtcEventDetail> queryRtcEventDetail(Map<String, Object> condition,String collectionName) throws Exception {
		//校验
		if(StringUtils.isEmpty(collectionName)){
			throw new Exception("没有表信息");
		}
		mongoDBManager =new MongoDBManager(dataBase, collectionName);
		//
		Condition cond = Condition.init();
		if(condition.size()>0){
			
			for (Map.Entry<String, Object> entry: condition.entrySet()) {
				cond.is(entry.getKey(), entry.getValue());
			}
		}
		return (List<RtcEventDetail>) mongoDBManager.find(cond,RtcEventDetail.class);
	}

	@Override
	public List<Object> groupByLessonCount(Map<String, Object> condition,
			String collectionName) throws Exception {
		//校验
		if(StringUtils.isEmpty(collectionName)){
			throw new Exception("没有表信息");
		}
		mongoDBManager =new MongoDBManager(dataBase, collectionName);
		StringBuffer sb  =new StringBuffer();
		if(condition.size()>0){
			if ( condition.get("key") == null
					|| condition.get("initial") == null
					|| condition.get("cond") == null) {
				throw new Exception("参数不完整");
			}
			if(condition.get("reduce")!=null){
				sb.append(condition.get("reduce"));
			}else{
				sb.append("function(doc,prev){")
					.append(" if(doc.teacher in prev.teachers　) {")
						.append("  prev.teachers[doc.teacher] ++;")
							.append("  }else{ prev.teachers[doc.teacher] =1; }")
								.append(" if(doc.stu in prev.students){ prev.students[doc.stu] ++; }else{ prev.students[doc.stu] =1; } }");
			}
		}
		return (List<Object>) mongoDBManager.group(condition, sb.toString(), Object.class);
	}

	@Override
	public List<RoomEventEntity> groupRoomEvent(Map<String, Object> condition,
			String collectionName) throws Exception {
		//校验
		if(StringUtils.isEmpty(collectionName)){
			throw new Exception("没有表信息");
		}
		mongoDBManager =new MongoDBManager(dataBase, collectionName);
		//条件和参数
		StringBuffer sb =new StringBuffer();
		
		if(null!=condition&&condition.size()>0){
			
			if(condition.get("key")==null){
				condition.put("key", "roomId");
			}
			if(condition.get("initial")==null){
				Map<String,Object> initial = new HashMap<String, Object>();
				initial.put("lessonTimes", new HashMap<String,Object>());
				//承载用户的userId
				initial.put("userIds", new HashMap<String,Object>());//key-userType,value-userId
				initial.put("studentId","");
				initial.put("teacherId", "");
				initial.put("studentName", "");
				initial.put("teacherName", "");
				initial.put("courseName", new String());
				initial.put("teaEnterTimes", 0);
				initial.put("teaExitTimes", 0);
				initial.put("teaForceExitTimes", 0);
				initial.put("teaReConnectExitTimes", 0);
				initial.put("stuEnterTimes", 0);
				initial.put("stuExitTimes", 0);
				initial.put("stuForceExitTimes", 0);
				initial.put("stuReConnectExitTimes", 0);
				condition.put("initial", initial);
			}
			//根据roomId查询，cond：条件查询map集合
			Map<String,Object> cond = new HashMap<String, Object>();
			if(condition.get("roomId")!=null){
				cond.put("roomId",condition.get("roomId"));
			}
			//根据userId维度查询
			if (condition.get("userId")!=null) {
				cond.put("userId",condition.get("userId"));
			}
			//状态为1表示全部
			if(!condition.get("weekStatus").equals("1")){
				Map<String,Object> dates = new HashMap<String, Object>();
				Date[] weeks ={};
				//当前周
				if(condition.get("weekStatus").equals("0")){
					 weeks =DateUtils.getWeeks(0);
				//上一周	
				}else if(condition.get("weekStatus").equals("-1")){
					 weeks =DateUtils.getWeeks(-1);
				//上上周	
				}else if(condition.get("weekStatus").equals("-2")){
					 weeks =DateUtils.getWeeks(-2);
				}
				if(weeks.length<=7&&weeks.length>0){
					dates.put("$gte",weeks[0].getTime());
					//默认时间是星期天凌晨 所以不对
					dates.put("$lte", weeks[weeks.length-1].getTime()+DateUtils.DAY_MILLIS);
				}
				cond.put("insertTime",dates);
			}
			condition.put("cond", cond);
			//reduce
			sb.append("function(doc,prev){")
				.append("var time =doc.insertTime+'';")
					.append(" if(!(time in prev.lessonTimes)){ prev.lessonTimes[time] = 1; }else{ prev.lessonTimes[time] ++;  }")
						.append(" prev.courseName=doc.courseName;")
							.append(" if(doc.userType=='1'){ prev.userIds[doc.userType]=doc.userName+\"(\"+doc.userId+\")\";prev.teacherId=doc.userId; prev.teacherName=doc.userName; ")
								.append(" if(doc.event=='进入'){ prev.teaEnterTimes++; }else if(doc.event=='退出'){ prev.teaExitTimes++; }else if(doc.event=='强制退出'){ prev.teaForceExitTimes++; }else if(doc.event=='断线重连退出'){ prev.teaReConnectExitTimes++; }")
									.append("  }else if(doc.userType=='0'){prev.userIds[doc.userType]=doc.userName+\"(\"+doc.userId+\")\";prev.studentId=doc.userId; prev.studentName=doc.userName; ")
									 .append(" if(doc.event=='进入'){ prev.stuEnterTimes++; }else if(doc.event=='退出'){ prev.stuExitTimes++; }else if(doc.event=='强制退出'){ prev.stuForceExitTimes++; }else if(doc.event=='断线重连退出'){ prev.stuReConnectExitTimes++; } ")
									 	.append(" } }");
			//执行
			List<RoomEventEntity> data=(List<RoomEventEntity>) mongoDBManager.group(condition, sb.toString(), RoomEventEntity.class);
			//遍历集合，获取每个房间的语音信息
			Map<String, Object> coMap=new HashMap<>();
			for (RoomEventEntity roomEventEntity : data) {
				coMap.put("roomId", roomEventEntity.getRoomId());
				List<RtcEventEntity> rtcEventEntitys = this.queryRtcEvent(coMap, "RtcEvent");
				if (rtcEventEntitys!=null&&rtcEventEntitys.size()>0) {
					RtcEventEntity rtcEventEntity=rtcEventEntitys.get(0);
					roomEventEntity.setOpenCount(rtcEventEntity.getOpenCount());
					roomEventEntity.setCancelCount(rtcEventEntity.getCancelCount());
					roomEventEntity.setChannelInfo(rtcEventEntity.getChannelInfo());
					roomEventEntity.setChannelSwitchCount(rtcEventEntity.getChannelSwitch().length);
					/*if (StringUtils.isNotEmpty(roomEventEntity.getUserIds().get("0"))) {
						roomEventEntity.setStudentName(roomEventEntity.getUserIds().get("0").toString());
					}
					if (StringUtils.isNotEmpty(roomEventEntity.getUserIds().get("1"))) {
						roomEventEntity.setTeacherName(roomEventEntity.getUserIds().get("1").toString());
					}*/
					log.debug(roomEventEntity.getRoomId()+"--------->OpenCount-------->"+rtcEventEntity.getOpenCount()+"--------->CancelCount-------->"+rtcEventEntity.getCancelCount()+"------->ChannelInfo---->"+rtcEventEntity.getChannelInfo());
				}
			}
			coMap=null;//置空，垃圾回收
			
			return attachedEntity(data);
		}
		return null;
	}
	/**
	 * 拼接字段
	 * @param root
	 * @return
	 */
	private List<RoomEventEntity> attachedEntity(List<RoomEventEntity> root){
		List<RoomEventEntity> data = new ArrayList<RoomEventEntity>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
		if(null!=root&&root.size()>0){
			for (RoomEventEntity roomEventEntity : root) {
				Set<String> set =roomEventEntity.getLessonTimes().keySet();
				List<String> list =new ArrayList<String>(set);
				//排序
				Collections.sort(list);
				long startTime = Long.parseLong(list.get(0));
				long endTime = Long.parseLong(list.get(list.size()-1));
				roomEventEntity.setLessionTimeRegion(sdf.format(new Date(startTime))/*+"（"+DateUtils.formatDuring(endTime-startTime)+"）"*/);
				roomEventEntity.setLessonTimes(null);
				data.add(roomEventEntity);
			}
		}
	return data;
	}

	@Override
	public List<RtcEventEntity> queryRtcEvent(Map<String, Object> condition,
			String collectionName) throws Exception {
		//校验
		if(StringUtils.isEmpty(collectionName)){
			throw new Exception("没有表信息");
		}
		mongoDBManager =new MongoDBManager(dataBase, collectionName);
		//条件和参数
		StringBuffer sb =new StringBuffer();
		if(null!=condition && condition.size()>0){
			if(condition.get("key")==null){
				condition.put("key", "roomId");
			}
			if(condition.get("initial")==null){
				Map<String,Object> initial = new HashMap<String, Object>();
				initial.put("openCount", 0);
				initial.put("cancelCount", 0);
				initial.put("channelSwitch", new String[]{});
				initial.put("channelSwitchTimes",new Double[]{});
				initial.put("lastTime", 0);
				initial.put("fristTime", 0);
				condition.put("initial", initial);
			}
			//根据roomId查询
			Map<String,Object> cond = new HashMap<String, Object>();
			if(condition.get("roomId")!=null){
				cond.put("roomId",condition.get("roomId"));
			}
			condition.put("cond", cond);
			//reduce
			sb.append("function(doc,prev){")
				.append("prev.fristTime++;if(prev.fristTime==1){ prev.last_time=doc.insertTime; }")
					.append(" if(doc.status=='1' &&!doc.isChange){ prev.openCount++;")
						.append(" }else if(doc.status=='0' &&!doc.isChange){ prev.cancelCount++;")
							.append(" }else if(doc.status=='1' &&doc.isChange){  prev.channelSwitchTimes.push((doc.insertTime-prev.last_time)/(1000*60)); ")
									 	.append(" prev.last_time=doc.insertTime;  prev.channelSwitch.push(doc.source);  } }");
			//执行
			List<RtcEventEntity> data=(List<RtcEventEntity>) mongoDBManager.group(condition, sb.toString(), RtcEventEntity.class);
			
			return attachedRtcEventEntity(data);
		}
		return null;
	}
	/**
	 * 设置rtcEventEntity
	 * @param data
	 * @return
	 */
	private List<RtcEventEntity> attachedRtcEventEntity(
			List<RtcEventEntity> data) {
		//保留一位小数
		List<RtcEventEntity> result = new ArrayList<RtcEventEntity>();
		if(null !=data &&data.size()>0){
			for (RtcEventEntity rtcEventEntity : data) {
				String[] sources =rtcEventEntity.getChannelSwitch();
				Double[] sourceTimes =rtcEventEntity.getChannelSwitchTimes();
				//这是频道
				if((null!=sources&&sources.length>0)&&(null!=sourceTimes && sourceTimes.length>0)){
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < sources.length; i++) {
						if(i==0){
							sb.append(sources[i]+"("+formatDouble(sourceTimes[i],1)+")");
						}else{
							sb.append(","+sources[i]+"("+formatDouble(sourceTimes[i],1)+")");
						}
						rtcEventEntity.setChannelInfo(sb.toString());
					}
				}
				result.add(rtcEventEntity);
			}
		}
		return result;
	}
	/**
	 * 四舍五入后 保留n 位小数
	 * @param d
	 * @return
	 */
	 private String formatDouble(double d,int n) {
	        NumberFormat nf = NumberFormat.getNumberInstance();
	        // 保留两位小数
	        nf.setMaximumFractionDigits(n); 	        
	        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
	        nf.setRoundingMode(RoundingMode.UP);

	        return nf.format(d);
	    }

	@Override
	public List<RoomEventDetail> queryRoomEventDetail(Map<String, Object> condition, String collectionName) throws Exception {
		if (StringUtils.isEmpty(collectionName)) {
			throw new RuntimeException("表信息不能为空！");
		}
		mongoDBManager = new MongoDBManager(dataBase, collectionName);
		//初始化condition
		Condition cond = Condition.init();
		for(Map.Entry<String, Object> entity:condition.entrySet()){
			cond.is(entity.getKey(), entity.getValue());
		}
		List<RoomEventDetail> roomEventDetails = (List<RoomEventDetail>) mongoDBManager.find(cond, RoomEventDetail.class);
		return roomEventDetails;
	}

	@Override
	public List<String> distinctQueryRoomId(Map<String, Object> condition, String collectionName) throws Exception {
		//校验
		if(StringUtils.isEmpty(collectionName)){
			throw new Exception("没有表信息");
		}
		mongoDBManager =new MongoDBManager(dataBase, collectionName);
		Collection<String> distinctQuery = mongoDBManager.distinctQuery("roomId", condition,String.class);
		return (List<String>) distinctQuery;
	}
}
