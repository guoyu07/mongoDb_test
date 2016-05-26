package com.hfjy.mongoTest.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * http 验证工具类
 * @author leo-zeng
 *
 */
public class HttpUtils {
	/** 
	 * @author :leo-zeng 
	 * @Description: 验证传入的是属于http
	 * @param uri
	 * @return boolean    
	 */
	public static boolean validateUri(String uri) {
		if (uri != null && !"".equals(uri) && uri.toLowerCase().startsWith("http://")) {
			return true;
		} else {
			return false;
		}
	}
	/** 
	 * @author :leo-zeng 
	 * @Description: 验证该端口 是否可以ping 通
	 * @param host
	 * @param port
	 * @return boolean    
	 */
	public static boolean ping(String host,int port){
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(host, port));
			socket.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	/**
	 * @author :leo-zeng 
	 * @Description: 获取本地地址
	 * @return
	 */
	public static String getLocalAddress() {
		String hostName = "127.0.0.1";
		try {
			InetAddress ia = InetAddress.getLocalHost();
			hostName = StringUtils.unite(ia.getHostName(), "(", ia.getHostAddress(), ")");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage());
		}
		return hostName;
	}
}
