package org.jcommon.com.wechat.router.server;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.data.Token;
import org.jcommon.com.wechat.router.Router;
import org.jcommon.com.wechat.router.RouterHandler;
import org.jcommon.com.wechat.router.RouterType;
import org.jcommon.com.wechat.router.WechatRouter;
import org.jcommon.com.wechat.utils.WechatUtils;

public class HttpHandler implements RouterHandler, HttpListener{
	private Logger logger = Logger.getLogger(getClass());
	private WechatRouter wechat_router;
	private Set<HRouter> routers = new HashSet<HRouter>();
	
	public HttpHandler(WechatRouter wechat_router) {
		this.setWechat_router(wechat_router);
	}
	
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
	}
	
	public void onFailure(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
	}
	
	public void onTimeout(HttpRequest request){
	    logger.error(request.getUrl());
	}

	public void onException(HttpRequest request, Exception e){
	    logger.error(request.getUrl(), e);
	}

	private void callback(String callback,String signature,String timestamp,String nonce,String xml){
		logger.info(callback);
		String[] keys   = { "signature","timestamp","nonce" };
		String[] values = { signature,timestamp,nonce };
		String    url   = JsonUtils.toRequestURL(callback, keys, values);
		HttpRequest request = new HttpRequest(url,xml,"POST",this);
		ThreadManager.instance().execute(request);
	}
	
	private void callback(String callback,String wechatID,String signature,String timestamp,String nonce,String xml){
		logger.info(callback);
		String[] keys   = { "signature","timestamp","nonce","wechatID" };
		String[] values = { signature,timestamp,nonce,wechatID };
		String    url   = JsonUtils.toRequestURL(callback, keys, values);
		HttpRequest request = new HttpRequest(url,xml,"POST",this);
		ThreadManager.instance().execute(request);
	}
	
	@Override
	public void onRouter(Router router) {
		// TODO Auto-generated method stub
		if(router!=null){
			String signature = router.getSignature();
			String timestamp = router.getTimestamp();
			String nonce     = router.getNonce();
			String xml       = router.getXml();
			
			String wechatID  = router.getWechatID();
			Set<HRouter> hs  = getHRouter(RouterType.Callback,wechatID);
			for(HRouter h : hs){
				callback(h.url,signature,timestamp,nonce,xml);
			}
		}
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onToken(Token token) {
		// TODO Auto-generated method stub
		if(token!=null){
			String Token     = token.getToken();
			String wechatID  = token.getWechatID();
			
			String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()); 
		    String nonce     = org.jcommon.com.util.BufferUtils.generateRandom(6);
		    String signature = WechatUtils.createSignature(Token, timestamp, nonce); 
			String xml       = new Token(token.getAccess_token(),token.getExpires_in()).toJson();
			
			Set<HRouter> hs  = getHRouter(RouterType.Token,wechatID);
			for(HRouter h : hs){
				callback(h.url,wechatID,signature,timestamp,nonce,xml);
			}
		}
	}

	@Override
	public void addRouter(Object... args) {
		// TODO Auto-generated method stub
		RouterType type = RouterType.getType(String.valueOf(args[0]));
		String wechatID = String.valueOf(args[1]);
		String url      = String.valueOf(args[2]);
		
		if(type==null || wechatID==null || url==null){
			logger.warn( String.format("%s %s : %s", wechatID,type,url));
			return;
		}
		
		HRouter router = getHRouter(type,wechatID,url);
		if(router==null){
			routers.add(new HRouter(type,wechatID,url));
		}else{
			logger.warn(String.format("%s %s : %s", wechatID,type,url) + "have exist!");
		}
	}

	@Override
	public void removeRouter(Object... args) {
		// TODO Auto-generated method stub
		RouterType type = RouterType.getType(String.valueOf(args[0]));
		String wechatID = String.valueOf(args[1]);
		String url      = String.valueOf(args[2]);
		
		if(type==null || wechatID==null || url==null){
			logger.warn( String.format("%s %s : %s", wechatID,type,url));
			return;
		}
		
		HRouter router = getHRouter(type,wechatID,url);
		if(router!=null){
			routers.remove(router);
		}else{
			logger.warn(String.format("%s %s : %s", wechatID,type,url) + "is not exist!");
		}
	}

	public WechatRouter getWechat_router() {
		return wechat_router;
	}

	public void setWechat_router(WechatRouter wechat_router) {
		this.wechat_router = wechat_router;
	}
	
	public Set<HRouter> getHRouter(RouterType type,String wechatID){
		Set<HRouter> hs = new HashSet<HRouter>();
		synchronized(routers){
			for(HRouter h : routers){
				if(h.equals(type,wechatID))
					hs.add(h);
			}
		}
	
		return hs;
	}
	
	public HRouter getHRouter(RouterType type,String wechatID,String url){
		synchronized(routers){
			for(HRouter h : routers){
				if(h.equals(type,wechatID,url))
					return h;
			}
		}
	
		return null;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(HRouter h : routers){
			sb.append(h.toString()).append("\n");
		}
		return sb.toString();
	}
	
	class HRouter{
		public RouterType type;
		public String wechatID;
		public String url;
		
		HRouter(RouterType type,String wechatID,String url){
			this.type     = type;
			this.wechatID = wechatID;
			this.url      = url;
		}
		
		public String toString(){
			return String.format("%s %s : %s", wechatID,type,url);
		}
		
		public boolean equals(RouterType type,String wechatID){
			if(type==null || wechatID==null)
				return false;
			return type == this.type && wechatID.equals(this.wechatID);
		}
		
		public boolean equals(RouterType type,String wechatID,String url){
			if(type==null || wechatID==null || url==null)
				return false;
			return type == this.type && wechatID.equals(this.wechatID) && url.equals(this.url);
		}
	}
}
