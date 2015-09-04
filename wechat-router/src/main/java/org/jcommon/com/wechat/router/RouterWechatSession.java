package org.jcommon.com.wechat.router;

import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionListener;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;

public class RouterWechatSession extends WechatSession {

	public RouterWechatSession(String wechatID, App app,
			WechatSessionListener listener) {
		super(wechatID, app, listener);
		// TODO Auto-generated constructor stub
	}

	public void startup() {
		logger.info(super.getWechatID());
		SessionCache.instance().addWechatSession(this);
		appKeepAlive(super.getApp()); 
	}
}
