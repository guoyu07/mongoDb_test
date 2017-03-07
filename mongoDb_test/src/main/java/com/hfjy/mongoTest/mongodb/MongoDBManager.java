package com.hfjy.mongoTest.mongodb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hfjy.base.core.Log;
import com.hfjy.mongoTest.bean.Condition;
import com.hfjy.mongoTest.bean.OperationType;
import com.hfjy.mongoTest.utils.BeanConverterUtils;
import com.hfjy.mongoTest.utils.StringUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

/**
 * mongoDB 连接类
 * 
 * @author leo-zeng
 *
 */
public class MongoDBManager {

	// private static final Logger Log = Logger.getLogger(MongoDBManager.class);

	private DBCollection dbCollection;

	private MongoCollection<Document> mongoColl;

	public MongoDBManager(String dbName, String collName) {
		dbCollection = MongoDBServer.getDBCollection(dbName, collName);
		mongoColl = MongoDBServer.getMongoCollection(dbName, collName);
	}

	public MongoCollection<Document> getMongoColl() {
		return mongoColl;
	}

	/**
	 * 方法名：delete 描述：删除数据库dbName中，指定keys和相应values的值
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param keys
	 * @param values
	 * @return
	 */
	public boolean delete(String[] keys, Object[] values) {
		try {
			if (keys != null && values != null) {
				// 如果keys和values不对等，直接返回false
				if (keys.length != values.length) {
					return false;
				} else {
					try {
						// 构建删除条件
						BasicDBObject doc = new BasicDBObject();
						// 删除返回结果
						WriteResult result = null;

						for (int i = 0; i < keys.length; i++) {
							// 添加删除的条件
							doc.put(keys[i], values[i]);
						}
						// 执行删除操作
						result = dbCollection.remove(doc);

						// 根据删除执行结果进行判断后返回结果
						return (result.getN() > 0) ? false : true;
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					} finally {
						MongoDBServer.poolClose();
					}
				}
			}
		} catch (Exception e) {
			Log.error(e, e.getMessage());
		}
		return false;
	}

	// 根据条件获取指定字段
	public <T> Collection<T> find(BasicDBObject condition, BasicDBObject key, Class<T> cls) {
		ArrayList<T> resultList = new ArrayList<T>();
		DBCursor cursor = null;
		try {
			cursor = dbCollection.find(condition, key);
			if (cursor == null) {
				return null;
			}
			while (cursor.hasNext()) {
				resultList.add(JSONObject.parseObject(cursor.next().toString(), cls));
			}
			return resultList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			MongoDBServer.poolClose();
		}

	}

	/**
	 * 
	 * 方法名：find 描述：从数据库dbName中查找指定keys和相应values的值
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param keys
	 * @param values
	 * @param num
	 * @return
	 */
	public ArrayList<DBObject> find(String[] keys, Object[] values, int num) {
		ArrayList<DBObject> resultList = new ArrayList<DBObject>();
		DBCursor cursor = null;
		try {
			if (keys != null && values != null) {
				if (keys.length != values.length) {
					// 如果传来的查询参数对不对，直接返回空的结果集
					return resultList;
				} else {
					try {
						// 构建查询条件
						BasicDBObject queryObj = new BasicDBObject();
						// 填充查询条件
						for (int i = 0; i < keys.length; i++) {
							queryObj.put(keys[i], values[i]);
						}
						// 查询获取数据
						cursor = dbCollection.find(queryObj);
						int count = 0;
						// 判断是否是返回全部数据，num=-1返回查询全部数据，num!=-1则返回指定的num数据
						if (num != -1) {
							while (count < num && cursor.hasNext()) {
								resultList.add(cursor.next());
								count++;
							}
							return resultList;
						} else {
							while (cursor.hasNext()) {
								resultList.add(cursor.next());
							}
							return resultList;
						}
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					} finally {
						if (null != cursor) {
							cursor.close();
						}
						MongoDBServer.poolClose();
					}
				}
			}
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 方法名：inSert 描述：向指定的数据库中添加给定的keys和相应的values
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param keys
	 * @param values
	 * @return
	 */
	public boolean inSert(String dbName, String collectionName, String[] keys, Object[] values) {
		WriteResult result = null;
		try {
			if (keys != null && values != null) {
				if (keys.length != values.length) {
					return false;
				} else {
					BasicDBObject insertObj = new BasicDBObject();
					for (int i = 0; i < keys.length; i++) {
						insertObj.put(keys[i], values[i]);
					}
					try {
						result = dbCollection.insert(insertObj);
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					} finally {
						MongoDBServer.poolClose();
					}
					return (result.getN() > 0) ? false : true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 方法名：isExit 描述：判断给定的keys和相应的values在指定的dbName的collectionName集合中是否存在
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param keys
	 * @param values
	 * @return
	 */
	public boolean isExit(String key, Object value) {
		if (key != null && value != null) {
			try {
				BasicDBObject obj = new BasicDBObject();
				obj.put(key, value);

				if (dbCollection.count(obj) > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return false;
	}

	/**
	 * 方法名：update 描述：更新数据库dbName，用指定的newValue更新oldValue
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public boolean update(DBObject oldValue, DBObject newValue) {
		WriteResult result = null;
		if (oldValue.equals(newValue)) {
			return true;
		} else {
			try {
				result = dbCollection.update(oldValue, newValue);
				return (result.getN() > 0) ? false : true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				MongoDBServer.poolClose();
			}
		}
		return false;
	}

	/**
	 * 分组方法
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param params
	 *            {['key':object],['initial':Map<String,Object>
	 *            obj],['cond':Map<String,Object> obj]}
	 * @param reduceFun
	 *            : reduce
	 * @param cls
	 * @return
	 */
	public <T> Collection<T> group(Map<String, Object> params, String reduceFun, Class<T> cls) {

		try {

			// 校验
			if (params == null || params.get("key") == null || params.get("initial") == null || params.get("cond") == null) {
				throw new Exception("参数不够，key,initial,cond 都必填");
			}
			// 执行分组查询
			DBObject dbObj = dbCollection.group(createDBObject(params.get("key"), true), createDBObject(params.get("cond"), false), createDBObject(params.get("initial"), false), reduceFun);
			if (dbObj != null) {
				Collection<T> colls = new ArrayList<T>();
				if (dbObj instanceof BasicDBList) {
					Iterator<Object> iterator = ((BasicDBList) dbObj).iterator();
					while (iterator.hasNext()) {
						colls.add(JSONObject.parseObject(iterator.next().toString(), cls));
					}
				}
				return colls;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}

	/**
	 * 创建DBObject
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static BasicDBObject createDBObject(Object object, boolean isKey) {
		BasicDBObject obj = new BasicDBObject();
		if (object instanceof String) {
			if (isKey) {
				obj.append(object.toString(), true);
			}
		} else if (object instanceof Map) {
			if (((Map<String, Object>) object).size() == 1) {
				for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
					if (entry.getValue() instanceof Map) {
						obj.append(entry.getKey(), createDBObject(entry.getValue(), false));
					} else {
						obj.append(entry.getKey(), entry.getValue());
					}
				}
			} else {
				// 塞进去的值
				for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
					obj.append(entry.getKey(), entry.getValue());
				}
			}
		}
		return obj;
	}

	/**
	 * 新增方法
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param t
	 * @param cls
	 * @return
	 */
	public <T> boolean insert(T t) {
		try {
			mongoColl.insertOne(Document.parse(JSON.toJSONString(t)));
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return true;
	}

	/**
	 * 根据要求的condition 进行查询
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param condition
	 * @param cls
	 * @return
	 */
	public <T> Collection<T> find(Condition condition, Class<T> cls) {
		try {
			Collection<T> t = new LinkedList<T>();
			Bson[] bons = createCondition(condition);
			FindIterable<Document> doc = mongoColl.find(Filters.and(bons)).sort(new BasicDBObject("insertTime", -1));
			MongoCursor<Document> cursor = doc.iterator();
			while (cursor.hasNext()) {
				T bean = cls.newInstance();
				// 泛型实例化
				t.add((T) BeanConverterUtils.document2Bean(cursor.next(), bean));
			}
			return t;
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("all")
	public <T> Collection<T> distinctQuery(String distinctName, Map<String, Object> condition, Class<T> cls) {
		try {
			if (StringUtils.isEmpty(distinctName)) {
				throw new Exception(" distinctName 不能为空！");
			}
			Collection<T> t = new LinkedList<T>();
			List list = new ArrayList<>();
			if (condition != null && !condition.isEmpty()) {
				list = dbCollection.distinct(distinctName, new BasicDBObject(condition));
			} else {
				list = dbCollection.distinct(distinctName);
			}
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				T bean = cls.newInstance();
				t.add((T) iterator.next());
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据条件查询size
	 * 
	 * @param dbName
	 * @param collectionName
	 * @param condition
	 * @return
	 */
	public long size(String dbName, String collectionName, Condition condition) {
		// 获取对应的类型
		try {
			return mongoColl.count(Filters.and(createCondition(condition)));
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return 0;
	}

	/**
	 * 根据条件查询第一条
	 * 
	 * @param condtion
	 * @param cls
	 * @return
	 */

	public <T> T findOne(Condition condtion, Class<T> cls) {
		try {

			// 拼接查询条件
			Bson[] bons = createCondition(condtion);
			Document doc = mongoColl.find(Filters.and(bons)).first();
			if (doc != null) {
				T bean = cls.newInstance();
				return BeanConverterUtils.document2Bean(doc, bean);
			}
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * mapReduce
	 * 
	 * @param fun
	 *            ：{[map,Object],[reduce,Object]}
	 * @param cls
	 * @return 得到的bean 实体 只有两个属性 id 和value
	 */
	public <T> Collection<T> mapReduce(Map<String, Object> fun, Class<T> cls) {
		try {
			Collection<T> coll = new ArrayList<T>();

			if (fun.get("Map") == null || fun.get("Reduce") == null) {
				return null;
			}
			if (fun.get("Map") instanceof String && fun.get("Reduce") instanceof String) {
				// mapReduce
				MapReduceIterable<Document> ite = mongoColl.mapReduce(fun.get("Map").toString(), fun.get("Reduce").toString());

				MongoCursor<Document> cursor = ite.iterator();
				while (cursor.hasNext()) {
					String json = cursor.next().toJson();
					// mapReduce 中对应的分组是_id
					json = json.replaceAll("_id", "id");
					// 添加进去
					coll.add((T) JSONObject.parseObject(json, cls));
				}
				return coll;
			}
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 拼接查询条件
	 * 
	 * @param condition
	 * @return
	 */
	private static Bson[] createCondition(Condition condition) {
		List<Condition> conditions = condition.getConditions();
		Bson[] docs = new Bson[conditions.size()];
		for (int i = 0; i < conditions.size(); i++) {
			Condition c = conditions.get(i);
			if (c.getType() == OperationType.OR) {
				docs[i] = Filters.or(createCondition(c));
			} else if (c.getType() == OperationType.NOT) {
				docs[i] = Filters.not(Filters.and(createCondition(c)));
			} else {
				docs[i] = getType(c);
			}
		}
		return docs;
	}

	/**
	 * 获取filters 的运算类型
	 * 
	 * @param condition
	 * @return
	 */
	private static Bson getType(Condition condition) {
		Bson bson = null;
		switch (condition.getType()) {
		case EQUAL:
			bson = Filters.eq(condition.getKey(), condition.getValue());
			break;
		case NOT_EQUAL:
			bson = Filters.ne(condition.getKey(), condition.getValue());
			break;
		case GREATER_THAN:
			bson = Filters.gt(condition.getKey(), condition.getValue());
			break;
		case GREATER_THAN_EQUAL:
			bson = Filters.gte(condition.getKey(), condition.getValue());
			break;
		case LESS_THAN:
			bson = Filters.lt(condition.getKey(), condition.getValue());
			break;
		case LESS_THAN_EQUAL:
			bson = Filters.lte(condition.getKey(), condition.getValue());
			break;
		case LIKE:
			bson = Filters.regex(condition.getKey(), condition.getValue().toString());
			break;
		default:
			break;
		}
		return bson;
	}

}
