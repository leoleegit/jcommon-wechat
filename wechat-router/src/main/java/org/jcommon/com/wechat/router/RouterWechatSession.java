package org.jcommon.com.wechat.router;

import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionListener;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Token;

public class RouterWechatSession extends WechatSession {

	public RouterWechatSession(String wechatID, App app,
			WechatSessionListener listener) {
		super(wechatID, app, listener);
		// TODO Auto-generated constructor stub
	}
	
	public void onToken(Token token) {
		// TODO Auto-generated method stub
		WechatRouter.instance().onToken(token);
		super.onToken(token);
	}
}
