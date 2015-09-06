package org.jcommon.com.wechat.router.test;
import java.io.IOException;

import org.jcommon.com.wechat.router.WechatRouter;
import org.jcommon.com.wechat.router.client.NoIOClient;
import org.jcommon.com.wechat.test.TestBase;


public class NoIOClientTest extends TestBase{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		WechatRouter.instance().startup();
		String wechatID = "gh_f49bb9a333b3";
		String Token    = "spotlight-wechat";
		NoIOClient client = new NoIOClient("127.0.0.1",5010);
		client.startup();
		
		client.addCRouter(wechatID, Token,"","http://192.168.2.72/chat/index.jsp");
		
		//System.in.read();
	}

}
