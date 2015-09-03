package org.jcommon.com.wechat.router.test;
import java.io.IOException;

import org.jcommon.com.wechat.router.client.NoIOClient;


public class NoIOClientTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		NoIOClient client = new NoIOClient("192.168.2.104",5010);
		client.start();
		
		System.in.read();
	}

}
