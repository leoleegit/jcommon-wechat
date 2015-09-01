package org.jcommon.com.wechat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.util.collections.MapStoreListener;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.Router;
import org.jcommon.com.wechat.utils.EventType;
import org.jcommon.com.wechat.utils.WechatUtils;

public class WechatSessionRouter extends WechatSession implements MapStoreListener{
	private List<RouterHandler> handlers = new ArrayList<RouterHandler>();
	
	public WechatSessionRouter(String wechatID, App app) {
		super(wechatID, app, null);
		// TODO Auto-generated constructor stub
	}

	public boolean appVerify(String signature, String timestamp, String nonce){
		return true;
	}

	public void startup(){
		SessionCache.instance().addWechatSession(this);
		SessionCache.instance().addMapStoreListener(this);
		appKeepAlive(this.app);
	}
	
	public void shutdown(){
		super.shutdown();
		SessionCache.instance().removeMapStoreListener(this);
	}
	
	public void addRouterHandler(RouterHandler handler){
		synchronized (handlers) {
			if(!handlers.contains(handler))
				handlers.add(handler);
		}
	}
	
	public void removeRouterHandler(RouterHandler handler){
		synchronized (handlers) {
			if(handlers.contains(handler))
				handlers.remove(handler);
		}
	}
	
	public void clearRouterHandler(){
		synchronized (handlers) {
			handlers.clear();
		}
	}
	
	public void onRouter(Router router) {
		// TODO Auto-generated method stub
		synchronized (handlers) {
			for(RouterHandler handler : handlers){
				handler.onRouter(router);
			}
		}
	}
	
	private void addSessioin(WechatSession value) {
		// TODO Auto-generated method stub
		Router router = createRouter(value.getApp());
	    if(router==null){
	    	logger.warn("router is null");
	    	return;
	    }
	    onRouter(router);
	}
	
	private Router createRouter(App app){
		if(app==null)
			return null;
		String access_token = app.getAccess_token();
	    long expires_in     = app.getExpires();
	    Event event = new Event(null);
	    event.setAccess_token(access_token);
	    event.setExpires_in(expires_in);
	    event.setMsgType(EventType.access_token.toString());
	    
	    
	    String timestamp = new Timestamp(System.currentTimeMillis()).toString(); 
	    String nonce     = org.jcommon.com.util.BufferUtils.generateRandom(6);
	    String token     = app.getToken();
	    String signature = WechatUtils.createSignature(token, timestamp, nonce); 
	    
	    return new Router(signature, timestamp, nonce, event.toXml()).setRouter_type(Router.EVENT);
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
}
