package org.jcommon.com.wechat.jiaoka;

import java.io.IOException;

import org.jcommon.com.util.system.SystemManager;
import org.jcommon.com.wechat.router.client.NoIOClient;
import org.jcommon.com.wechat.test.TestBase;


public class JiaoKaTest extends TestBase{
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new SystemManager().contextInitialized(null);
		SystemManager.instance().addListener(new JiaoKaTest());
//		
//		System.in.read();
//		long time = 1443078776L;
//		System.out.println(org.jcommon.com.util.DateUtils.getNowSinceYear(new Date(time*1000)));
	}
	
	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
		String wechatID = "gh_f49bb9a333b3";
		String Token    = "spotlight-wechat";
		
		NoIOClient client = new NoIOClient("192.168.1.37",5010);
		client.addCRouter(wechatID, Token,null,null);
		
		client.startup();

	}
}
