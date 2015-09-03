package org.jcommon.com.wechat.router;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jcommon.com.util.collections.MapStoreListener;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.Token;

public class TokenRouter implements MapStoreListener{
	private Logger logger = Logger.getLogger(getClass());
	private Set<Token> tokens = new HashSet<Token>();
	private WechatRouter router;
	
	public TokenRouter(WechatRouter router){
		this.setRouter(router);
	}
	
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
	
	public void startup(){
		SessionCache.instance().addMapStoreListener(this);
	}
	
	public void shutdown(){
		SessionCache.instance().removeMapStoreListener(this);
	}
	
	private void addSessioin(WechatSession value) {
		// TODO Auto-generated method stub
	    if(value!=null && value.getApp()!=null){
	    	Token token = new Token(value.getApp().getAccess_token(),value.getApp().getExpires());
    		token.setWechatID(value.getWechatID());
    		token.setToken(value.getApp().getToken());
    		addToken(token);
	    }else{
	    	logger.warn("router is null");
	    }
	}

	public void addToken(Token token) {
		Token temp = getToken(token.getWechatID());
		if(temp!=null)
			tokens.remove(temp);
		tokens.add(token);
		if(router!=null)
			router.onToken(token);
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
