package org.jcommon.com.wechat.manage.test;

import org.jcommon.com.util.system.SystemListener;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionListener;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.utils.EventType;

public class ServiceTest implements SystemListener , WechatSessionListener{

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		String wechatID     = "gh_f49bb9a333b3";
		String Token        = "spotlight-wechat";
		
		//String appId = null;
		String appId = "wx742941360129cd17";
		String secret= "37492ad273076440c0f123716865e1da";
		App app = new App(appId, secret, Token);
		
//		String wechatID     = "gh_0fffc43724da";
//		String Token        = "6z7nmp9qu";
//		
//		String appId = "wx71e85c2421d9b1d9";
//		String secret= "4e5913a95c9d585b56fa7d2437f16157";
//		App app = new App(appId, secret, Token);
		
		
		WechatSession session = new WechatSession(wechatID,app,this);
		session.startup();
	}

	@Override
	public void onEvent(Event paramEvent) {
		// TODO Auto-generated method stub
		System.out.println(paramEvent.toXml());
		if(EventType.getType(paramEvent.getEvent()) == EventType.LOCATION){
			//org.jcommon.com.wechat.manage.db.bean.Event event = new org.jcommon.com.wechat.manage.db.bean.Event(paramEvent.toJson());
		}
	}

	@Override
	public void onMessage(InMessage paramInMessage) {
		// TODO Auto-generated method stub
		System.out.println(paramInMessage.toXml());
	}

}
