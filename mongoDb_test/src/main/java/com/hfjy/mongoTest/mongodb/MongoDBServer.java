package com.hfjy.mongoTest.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;




import org.apache.log4j.Logger;
import org.bson.Document;

import com.hfjy.mongoTest.utils.Config;
import com.hfjy.mongoTest.utils.HttpUtils;
import com.hfjy.mongoTest.utils.StringUtils;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBServer {
	private static String prefix = "MONGODB_";
	
	private static final Logger Log = Logger.getLogger(MongoDBManager.class);
	
	private static MongoClient mongoClient = null;
	// 类初始化时，自行实例化，饿汉式单例模式
	// 从配置文件中获取属性值
	private static String hosts = Config.get(prefix + "HOST", "192.168.0.206").trim();
	private static String ports = Config.get(prefix + "PORT", "27017").trim();
	private static String userNames = Config.get(prefix + "USER_NAME", "root").trim();
	private static String pwds = Config.get(prefix + "PASSWORD", "hfmongotest123").trim();
	private static String dataBases = Config.get(prefix + "DATABASE", "admin").trim();
	private static String replSetName = Config.get(prefix + "REPL_SET_NAME", "").trim();
	
	
	private static final Map<String, MongoDatabase> mongoDatabase = new ConcurrentHashMap<>();

	private static final Map<String, MongoCollection<Document>> mongoCollectionMap = new ConcurrentHashMap<>();
	
	static{
		if (mongoClient == null) {
			MongoClientOptions.Builder build = new MongoClientOptions.Builder();
			// 与目标数据库能够建立的最大connection数量为50
			build.connectionsPerHost(50);
			// 如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
			build.threadsAllowedToBlockForConnectionMultiplier(50);
			/*
			 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
			 * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
			 * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
			 */
			build.maxWaitTime(1000 * 60 * 2);
			// 与数据库建立连接的timeout设置为1分钟
			build.connectTimeout(1000 * 60 * 1);
			build.socketTimeout(1000 * 60 * 1);
			// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
			build.threadsAllowedToBlockForConnectionMultiplier(5000);
			if(StringUtils.isNotEmpty(replSetName)){
				build.requiredReplicaSetName(replSetName);
				build.connectionsPerHost(1);
			}
			MongoClientOptions myOptions = build.build();
			/**
			 * 用户授权
			 */
			List<MongoCredential> credentials = new ArrayList<MongoCredential>();
			List<ServerAddress> addressList = new ArrayList<ServerAddress>();
			String[] hostArr = hosts.split(";");
			String[] portArr = ports.split(";");
			for (int i = 0; i < hostArr.length; i++) {
				if (StringUtils.isEmpty(portArr[i])) {
					break;
				}
				// ping 端口 是否可以通
				if (!HttpUtils.ping(hostArr[i], Integer.parseInt(portArr[i]))) {
					throw new MongoException(hostArr[i] + ":"
							+ Integer.parseInt(portArr[i]) + "地址没法通信!");
				}
				addressList.add(new ServerAddress(hostArr[i], Integer
						.parseInt(portArr[i])));
			}

			String[] userNameArr = userNames.split(";");
			String[] pwdArr = pwds.split(";");
			String[] dbArr = dataBases.split(";");
			for (int i = 0; i < userNameArr.length; i++) {
				if (StringUtils.isEmpty(pwdArr[i])
						|| StringUtils.isEmpty(dbArr[i])) {
					break;
				}
				credentials.add(MongoCredential.createScramSha1Credential(
						userNameArr[i], dbArr[i], pwdArr[i].toCharArray()));
			}
			try {
				// 数据库连接实例
				mongoClient = new MongoClient(addressList, credentials,
						myOptions);
			} catch (MongoException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	} 
	public static MongoClient getMongoClient() {
		return mongoClient;
	}
	/**
	 * 获取db
	 * @param dbName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static DB getDB(String dbName){
		return mongoClient.getDB(dbName);
	}
	/**
	 * 获取dbCollection
	 * @param dbName
	 * @param collectionName
	 * @return
	 */
	public static DBCollection getDBCollection(String dbName, String collectionName){
		DB db = getDB(dbName);
		return db.getCollection(collectionName);
	}
	/**
	 * 获取数据库
	 * 
	 * @param dbName
	 * @return
	 */
	public static MongoDatabase getMongoDatabase(String dbName) {
		// map中找 是否存在
		MongoDatabase md =null;
		try {
			md =mongoDatabase.get(dbName);
			if (md == null) {
				md = getMongoClient().getDatabase(dbName);
				mongoDatabase.put(dbName, md);
			}
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		return md;
	}

	/**
	 * 获取集合
	 * 
	 * @param dbName
	 * @param collectionName
	 * @return
	 */
	public static <T> MongoCollection<Document> getMongoCollection(
			String dbName, String collectionName) {
		// map中找 是否存在
		MongoCollection<Document> mc = mongoCollectionMap.get(collectionName);
		if (mc == null) {
			mc = getMongoDatabase(dbName).getCollection(collectionName);
			mongoCollectionMap.put(collectionName, mc);
		}
	
		return mc;
	}
	/**
	 * 关闭资源
	 */
	public static void poolClose(){
		if(mongoClient !=null){
			getMongoClient().close();
		}
	}
}
