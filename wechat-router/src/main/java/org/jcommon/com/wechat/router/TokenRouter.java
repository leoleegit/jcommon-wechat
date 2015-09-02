package org.jcommon.com.wechat.router;

import org.apache.log4j.Logger;
import org.jcommon.com.util.collections.MapStoreListener;
import org.jcommon.com.wechat.TokenHandler;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.Token;

public class TokenRouter implements MapStoreListener{
	private Logger logger = Logger.getLogger(getClass());
	private TokenHandler handler;
	private Token token;
	
	public boolean addOne(Object key, Object value){
	    if (value == null) return false;
	    addSessioin((WechatSession)value);
	    return false;
	}

	public boolean updateOne(Object key, Object value){
		if (value == null) return false;
		  addSessioin((WechatSession)value);
	    return false;
	}

	public Object removeOne(Object key){
	    if (key == null) return key;
	    return key;
	}
	
	public void start(){
		SessionCache.instance().addMapStoreListener(this);
	}
	
	public void stop(){
		SessionCache.instance().removeMapStoreListener(this);
	}
	
	private void addSessioin(WechatSession value) {
		// TODO Auto-generated method stub
	    if(value!=null && value.getApp()!=null){
	    	if(handler!=null){
	    		token = new Token(value.getApp().getAccess_token(),value.getApp().getExpires());
	    		token.setWechatID(value.getWechatID());
	    		handler.onToken(token);
	    	}
	    }else{
	    	logger.warn("router is null");
	    }
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public Token getToken() {
		return token;
	}
}
