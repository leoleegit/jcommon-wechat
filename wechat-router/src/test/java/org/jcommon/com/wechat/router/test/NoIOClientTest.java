package org.jcommon.com.wechat.router.test;
import java.io.IOException;

import org.jcommon.com.wechat.router.client.NoIOClient;
import org.jcommon.com.wechat.test.TestBase;


public class NoIOClientTest extends TestBase{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String wechatID = "gh_f49bb9a333b3";
		String Token    = "spotlight-wechat";
		NoIOClient client = new NoIOClient("127.0.0.1",5010,wechatID,Token);
		client.start();
		
		System.in.read();
	}

}
