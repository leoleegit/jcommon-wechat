package org.jcommon.com.wechat.router.client;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.WechatSessionManager;
import org.jcommon.com.wechat.data.Token;
import org.jcommon.com.wechat.router.WechatRouter;
import org.jcommon.com.wechat.utils.WechatUtils;

public class TokenHttpClient implements HttpListener{
	private Logger logger = Logger.getLogger(getClass());
	private String access_url;
	private String wechatID;
	private String token;
	private String callback_url;
	private boolean stop    = false;
	private final long retry= 30000;
	
	private Timer timer;
	
	public TokenHttpClient(String wechatID,String token,String access_url,String callback_url){
		this.access_url = access_url;
		this.wechatID   = wechatID;
		this.token      = token;
		this.callback_url = callback_url;
		if(callback_url!=null)
			WechatRouter.instance().addTokenHttpRouter(wechatID, callback_url);
	}
	
	public TokenHttpClient(String wechatID,String token,String access_url){
		this(wechatID,token,access_url,null);
	}
	
	public String toKey(){
		return String.format("TokenHttpClient:%s", wechatID);
	}
	
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
		String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()); 
	    String nonce     = org.jcommon.com.util.BufferUtils.generateRandom(6);
	    String signature = WechatUtils.createSignature(this.token, timestamp, nonce); 
	    
		Token token = new Token(sResult.toString());
		token.setWechatID(wechatID);
		token.setTimestamp(timestamp);
		token.setNonce(nonce);
		token.setSignature(signature);
		WechatSessionManager.instance().onToken(
				token.getSignature(), token.getTimestamp(), token.getNonce(), token.toJson());
	}
	
	public String toString(){
		return String.format("%s:%s; access_url:%s; callback_url:%s", wechatID,token,access_url,callback_url);
	}
	
	public void onFailure(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
		retry();
	}
	
	public void onTimeout(HttpRequest request){
	    logger.error(access_url);
	    retry();
	}

	public void onException(HttpRequest request, Exception e){
	    logger.error(access_url, e);
	    retry();
	}
	
	public void retry(){
	    if(stop || timer!=null)
			return;
		timer = org.jcommon.com.util.thread.TimerTaskManger.instance().schedule("TokenHttpClient", new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				go();
				timer = null;
			}
			
		}, retry);
	}
	
	public void stop(){
		stop = true;
	}
	
	public void go(){
		if(access_url!=null){
			String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()); 
		    String nonce     = org.jcommon.com.util.BufferUtils.generateRandom(6);
		    String token     = this.token;
		    String signature = WechatUtils.createSignature(token, timestamp, nonce); 
			
			String[] keys   = { "signature","timestamp","nonce","wechatID" };
			String[] values = { signature,timestamp,nonce,this.wechatID };
			String    url   = JsonUtils.toRequestURL(access_url, keys, values);
			HttpRequest request = new HttpRequest(url,this);
			ThreadManager.instance().execute(request);
		}
	}

	public String getCallback_url() {
		return callback_url;
	}

	public void setCallback_url(String callback_url) {
		this.callback_url = callback_url;
	}
}
