package org.jcommon.com.wechat.router.client;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.utils.WechatUtils;

public class TokenHttpClient implements HttpListener{
	private Logger logger = Logger.getLogger(getClass());
	private String access_url;
	private String wechatID;
	private String token;
	private boolean stop    = false;
	private final long retry= 30000;
	
	private Timer timer;
	
	public TokenHttpClient(String wechatID,String token,String access_url){
		this.access_url = access_url;
		this.wechatID   = wechatID;
		this.token      = token;
	}
	
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
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
}
