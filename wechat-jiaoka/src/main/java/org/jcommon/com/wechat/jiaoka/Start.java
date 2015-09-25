package org.jcommon.com.wechat.jiaoka;

import org.apache.log4j.Logger;
import org.jcommon.com.util.system.SystemListener;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.*;
import org.jcommon.com.wechat.jiaoka.service.Service;

public class Start implements SystemListener {
	private Logger logger = Logger.getLogger(getClass());
	private WechatSession session;
	
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		if(session!=null){
			session.shutdown();
			session = null;
		}
		logger.info("wechat system down...");
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		String access_token = "j2uBLa7hGOcpWlgwYD9JYJoGYAYFGUW6I0Vjm7XVIhzLnChd9YJSh56hisPGIpbkqOntLKw-cFbJQI65w13fVaUxc1ykLeVhh2x8__FGkjg";
		String wechatID     = "gh_f49bb9a333b3";
		String Token        = "spotlight-wechat";
		
		String appId = "wx742941360129cd17";
		String secret= "37492ad273076440c0f123716865e1da";
		App app = new App(access_token, appId, secret, Token);
		session = new Session(wechatID,app,null);
		//session.startup();
		
		logger.info("wechat system start...");
		
		//create menus
		Service service = new Service();
		Menus menus     = new DefaultMenus();
		service.createMenus(session, menus);
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return true;
	}

}
