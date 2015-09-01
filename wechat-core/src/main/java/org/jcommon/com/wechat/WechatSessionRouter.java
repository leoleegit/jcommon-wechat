package org.jcommon.com.wechat;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.util.collections.MapStoreListener;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Router;

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
		synchronized (handlers) {
			for(RouterHandler handler : handlers){
				handler.onAccessTokenUpdate(value.getApp());
			}
		}
	}
	
	public boolean addOne(Object key, Object value){
	    if (value == null) return false;
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
