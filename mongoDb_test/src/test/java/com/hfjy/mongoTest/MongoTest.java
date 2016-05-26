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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hfjy.mongoTest.entity.RtcEventDetail;
import com.hfjy.mongoTest.service.MongoDBService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/hfjy/mongoTest/spring.xml")
public class MongoTest {
	@Autowired
	private MongoDBService mongoDBService;
	@Test
	public void groupRoomEvent(){
		Map<String,Object> coMap = new HashMap<String, Object>();
		coMap.put("roomId", "12663");
		//coMap.put("weekStatus", "0");
		try {
			List<RtcEventDetail> rtcEntitys = mongoDBService.queryRtcEventDetail(coMap, "RtcEvent");
			System.out.println(JSON.toJSONString(rtcEntitys, true));
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}

}

