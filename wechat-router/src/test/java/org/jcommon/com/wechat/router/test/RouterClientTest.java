package org.jcommon.com.wechat.router.test;
import java.io.IOException;

import org.jcommon.com.util.system.SystemManager;
import org.jcommon.com.wechat.test.TestBase;


public class RouterClientTest extends TestBase{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new SystemManager().contextInitialized(null);
		SystemManager.instance().addListener(new RouterClientTest());
		
		//System.in.read();
	}
}
