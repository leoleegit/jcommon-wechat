package org.jcommon.com.wechat.test;

import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.IP;

public class IPTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//IP ip = new IP("{\"ip_list\":[\"127.0.0.1\",\"127.0.0.2\"]}");
		
		Group group = new Group(new Group("108",null).getId(), null){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String toJson(){
				return "{\"group\":"+super.toJson()+"}";
			}
		};
		System.out.println(group.toJson());
	}

}
