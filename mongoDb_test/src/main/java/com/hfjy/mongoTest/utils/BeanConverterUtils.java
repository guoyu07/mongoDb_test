package com.hfjy.mongoTest.utils;

import java.lang.reflect.Field;

import org.bson.Document;

/**
 * javaBean 转换器工具类
 * @author leo-zeng
 *
 */
public class BeanConverterUtils {
	/**
	 * 将mongodb对象转换成java
	 * @param docObject
	 * @param bean
	 * @return
	 */
	public static <T> T document2Bean(Document docObject,T bean){
		if(bean ==null){
			return null;
		}
		//获取bean的字段
		Field[] fields =bean.getClass().getDeclaredFields();
		//
		for (Field field : fields) {
			//允许可访问行
			field.setAccessible(true);
			String name =field.getName();
			Object obj =docObject.get(name);
			if(obj!=null){
				try {
					if(field.getType() == String.class){
						field.set(bean,String.valueOf(obj));
					}else if(field.getType() == Long.class){
						field.set(bean, Long.valueOf(obj.toString()));
					}else if(field.getType() == Integer.class){
						field.set(bean, Integer.valueOf(obj.toString()));
					}else{
						field.set(bean,obj);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			field.setAccessible(false);
		}
		return bean;
	}
}
