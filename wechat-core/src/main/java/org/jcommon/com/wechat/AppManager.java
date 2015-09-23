package org.jcommon.com.wechat;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.TimerTaskManger;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.IP;
import org.jcommon.com.wechat.data.Token;

public class AppManager extends ResponseHandler{
	private Logger logger = Logger.getLogger(getClass());
	private WechatSession session;
	private Timer app_keepalive;
	private Set<IP> ips;
	
	public AppManager(WechatSession session){
		this.session = session;
	}
	
	public void startup(){
		if(session !=null && session.getApp()!=null)
			keepAlive();
	}
	
	public void shutdown(){
		try {
	       if(app_keepalive!=null)
	        	this.app_keepalive.cancel();
	       this.app_keepalive = null; 
	       logger.info(session.logStr(""));
	    } catch (Exception e) {}
	}
	
	private void keepAlive() {
		logger.info(session.logStr("start"));
	    if (session.getApp().getAppid()==null | session.getApp().getSecret()==null) {
	        this.logger.warn(String.format("app id:%s;app secret:%s", 
	        		session.getApp().getAppid(),session.getApp().getSecret()));
	        return;
	    }
	    
	    this.app_keepalive = TimerTaskManger.instance().schedule("app-keepalive", new TimerTask(){
	      	public void run(){
	      		
	      		session.execute(RequestFactory.accessTokenReqeust(new HttpListener(){

	      			  public void onSuccessful(HttpRequest reqeust, StringBuilder sResult){
	      				  logger.info(sResult.toString());
	      				  session.onToken(new Token(sResult.toString()));
	      				  keepAlive();
	      			  }

	      			  public void onFailure(HttpRequest reqeust, StringBuilder sResult){
	      			      logger.warn(sResult.toString());
	      			      session.getApp().setStatus("app is error:onFailure");
	      			      keepAlive();
	      			  }

	      			  public void onTimeout(HttpRequest reqeust){
	      			      logger.error("timeout");
	      			      session.getApp().setStatus("app is error:onTimeout");
	      			      keepAlive();
	      			  }

	      			  public void onException(HttpRequest reqeust, Exception e){
	      			      logger.error("", e);
	      			      session.getApp().setStatus("app is error:onException");
	      			      keepAlive();
	      			  }
	      			 
	      		 },session.getApp()));
	      	}
	    }, session.getApp().getDelay(), session.getApp().getExpires() > 0L ? session.getApp().getExpires()*1000 - 100L : 7200000L);
	}

	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		if(paramHttpRequest.getAttibute(paramHttpRequest)!=null){
			AppManagerListener listener = (AppManagerListener) paramHttpRequest.getAttibute(paramHttpRequest);
			listener.onError(paramHttpRequest,paramError);
		}	
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		if(paramObject instanceof IP){
			IP ip = (IP) paramObject;
			logger.info(ip.getIp_list().size());
			if(paramHttpRequest.getAttibute(paramHttpRequest)!=null){
				AppManagerListener listener = (AppManagerListener) paramHttpRequest.getAttibute(paramHttpRequest);
				listener.onIPs(paramHttpRequest,ip.getIp_list());
			}	
		}
	}

	public void setIps(Set<IP> ips) {
		this.ips = ips;
	}

	public Set<IP> getIps(AppManagerListener listener) {
		if(ips==null){
			HttpRequest request = RequestFactory.callbackIpsReqeust(this, session.getApp().getAccess_token());
			if(listener!=null)
				request.setAttribute(request, listener);
			super.addHandlerObject(request, IP.class);
			session.execute(request);
		}
		return ips;
	}
}
