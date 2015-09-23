package org.jcommon.com.wechat.router;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Token;

public class CallbackRouter extends WechatSession{
	private Logger logger = Logger.getLogger(getClass());
	private final static String WECHATID = "*";
	private final static String APPID    = "wechat-app-id";
	private Set<Token> tokens = new HashSet<Token>();
	
	private WechatRouter router;
	
	public CallbackRouter(WechatRouter router){
		super(WECHATID, new App(APPID,null,null), null);
		this.setRouter(router);
	}
	
	public CallbackRouter(){
		super(WECHATID, null, null);
	}

	public void startup(){
		SessionCache.instance().addWechatSession(this);
	}
	
	public void shutdown(){
		SessionCache.instance().addWechatSession(this);
	}
	
	public void onEvent(Event event){
	    this.logger.info("IN:"+event.getXml());
	    if(router!=null){
	    	Router r = new Router(event.getSignature(),event.getTimestamp(),event.getNonce(),event.getXml());
	    	r.setWechatID(event.getToUserName());
	    	router.onRouter(r);
	    }
	}

	public void onMessage(InMessage message){
	    this.logger.info("IN:"+message.getXml());
	    if(router!=null){
	    	Router r = new Router(message.getSignature(),message.getTimestamp(),message.getNonce(),message.getXml());
	    	r.setWechatID(message.getToUserName());
	    	router.onRouter(r);
	    }
	}
	
	public void onToken(Token token) {
		logger.info(token.toJson());
		super.onToken(token);
		if(router!=null)
			router.onToken(token);
		
		Token temp = getToken(token.getWechatID());
		if(temp!=null)
			tokens.remove(temp);
		tokens.add(token);
	}
	
	public Token getToken(String wechatID) {
		for(Token token : tokens){
			if(wechatID!=null && wechatID.equals(token.getWechatID()))
				return token;
		}
		return null;
	}

	public WechatRouter getRouter() {
		return router;
	}

	public void setRouter(WechatRouter router) {
		this.router = router;
	}
}
