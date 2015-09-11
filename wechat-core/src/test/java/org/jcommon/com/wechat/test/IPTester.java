package org.jcommon.com.wechat.test;

import org.jcommon.com.wechat.data.IP;

public class IPTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IP ip = new IP("{\"ip_list\":[\"127.0.0.1\",\"127.0.0.2\"]}");
		System.out.println(ip.getIp_list().size());
	}

}
