package com.fruit.portal.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtil {

	static private final char COLON = ':';

	/**
	 * 
	 * 获取当前机器ip地址
	 * 
	 * 据说多网卡的时候会有问题.
	 */

	public static String getNetworkAddress() {
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(COLON) == -1) {
						return ip.getHostAddress();
					}
				}
			}
			return "127.0.0.1";
		} catch (Exception e) {
			return "127.0.0.1";
		}

	}
}
