// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.wechat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.collections.MapStoreListener;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.util.thread.TimerTaskManger;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.data.BroadcastMessage;
import org.jcommon.com.wechat.data.CustomService;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.Image;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Menus;
import org.jcommon.com.wechat.data.Mpnews;
import org.jcommon.com.wechat.data.Music;
import org.jcommon.com.wechat.data.News;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.Video;
import org.jcommon.com.wechat.data.Voice;
import org.jcommon.com.wechat.data.filter.Filter;
import org.jcommon.com.wechat.utils.ErrorType;
import org.jcommon.com.wechat.utils.EventType;
import org.jcommon.com.wechat.utils.MsgType;
import org.jcommon.com.wechat.utils.WechatUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WechatSession extends ResponseHandler
  implements WechatSessionListener{
  protected Logger logger = Logger.getLogger(getClass());
  private String wechatID;
  private App app;
  private WechatSessionListener listener;
  public final static String  RequestCallback = "RequestCallback";
  public final static String  RequestAction   = "RequestAction";
  public final static String  WECHATMEDIAPATH = "wechat-media-path";
  public final static String  WECHATMEDIAURL  = "wechat-media-url";
  
  private UserManager  userManager;
  private MsgManager   msgManager;
  private MediaManager mediaManager;
  
  private Timer app_keepalive;
  private boolean startup = false;

  public WechatSession(String wechatID, App app, WechatSessionListener listener){
    this.wechatID = wechatID;
    this.app = app;
    this.listener = listener;
    
    userManager  = new UserManager(this);
    msgManager   = new MsgManager(this);
    mediaManager = new MediaManager(this);
  }

  public void startup() {
	logger.info(wechatID);
	if(app.getAccess_token()!=null){
		userManager.startup();
	}
	
    SessionCache.instance().addWechatSession(this);
    SessionCache.instance().addMapStoreListener(new MapStoreListener(){

		@Override
		public boolean addOne(Object arg0, Object arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object removeOne(Object arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean updateOne(Object arg0, Object arg1) {
			// TODO Auto-generated method stub
			if(arg1 == WechatSession.this){
				App app = WechatSession.this.getApp();
				if("app is ok:onRunning".equals(app.getStatus()) && !startup){
					userManager.startup();
					startup = true;
				}
			}
			return false;
		}
    	
    });
    
    appKeepAlive(this.app); 
  }

  public void shutdown() {
    SessionCache.instance().removeWechatSession(this);
    try {
      if(app_keepalive!=null)
    	  this.app_keepalive.cancel();
      this.app_keepalive = null; } catch (Exception e) {
    }
    userManager.shutdown();
    startup = false;
    logger.info(wechatID);
  }

  protected void appKeepAlive(final App app) {
    if (app == null || app.getAppid()==null | app.getSecret()==null) {
      this.logger.warn("app can't be null");
      return;
    }
    try {
        if(app_keepalive!=null)
      	  this.app_keepalive.cancel();
        this.app_keepalive = null; 
    } catch (Exception e) {}
    this.app_keepalive = TimerTaskManger.instance().schedule("app-keepalive", new TimerTask(){
    	public void run(){
    		
    		 execute(RequestFactory.createAccessTokenReqeust(new HttpListener(){

    			 public void onSuccessful(HttpRequest reqeust, StringBuilder sResult){
    				logger.info(sResult.toString());
    			    JSONObject json = JsonUtils.getJSONObject(sResult.toString());
    			    if (json != null){
    			      try {
    			        String access_token = json.has("access_token") ? json.getString("access_token") : null;
    			        long expires_in = json.has("expires_in") ? json.getLong("expires_in") : 0L;
    			        if(access_token!=null){
    			        	WechatSession.this.app.setAccess_token(access_token);
    			        	WechatSession.this.app.setStatus("app is ok:onRunning");
    			        }else{
    			        	WechatSession.this.app.setStatus("app is error:"+sResult.toString());
    			        }
    			        if (expires_in!= WechatSession.this.app.getExpires() && expires_in!=0) {
    			          WechatSession.this.app.setExpires(expires_in);
    			          WechatSession.this.app.setDelay(app.getExpires()* 1000L-100);
    			          WechatSession.this.appKeepAlive(WechatSession.this.app);
    			        }
    			      } catch (JSONException e) {
    			        logger.error("", e);
    			      }
    			    }
    			    else
    			    	 WechatSession.this.app.setStatus("app is error:onError");
    			    SessionCache.instance().updateWechatSession(WechatSession.this);
    			  }

    			  public void onFailure(HttpRequest reqeust, StringBuilder sResult)
    			  {
    			    logger.warn(sResult.toString());
    			    WechatSession.this.app.setStatus("app is error:onFailure");
    			    SessionCache.instance().updateWechatSession(WechatSession.this);
    			    WechatSession.this.appKeepAlive(WechatSession.this.app);
    			  }

    			  public void onTimeout(HttpRequest reqeust)
    			  {
    			    logger.error("timeout");
    			    WechatSession.this.app.setStatus("app is error:onTimeout");
    			    SessionCache.instance().updateWechatSession(WechatSession.this);
    			    WechatSession.this.appKeepAlive(WechatSession.this.app);
    			  }

    			  public void onException(HttpRequest reqeust, Exception e)
    			  {
    			    logger.error("", e);
    			    WechatSession.this.app.setStatus("app is error:onException");
    			    SessionCache.instance().updateWechatSession(WechatSession.this);
    			    WechatSession.this.appKeepAlive(WechatSession.this.app);
    			  }
    			 
    		 },app));
    	}
    }, app.getDelay(), app.getExpires() > 0L ? app.getExpires()*1000 - 100L : 7200000L);
  }

  public App getApp(){
    return this.app;
  }

  public String getWechatID() {
    return this.wechatID;
  }

  public void setListener(WechatSessionListener listener) {
    this.listener = listener;
  }

  public void onEvent(Event event){
    this.logger.info("IN:"+event.getXml());
    if(EventType.access_token.equals(event.getMsgType())){
    	String access_token = event.getAccess_token();
	    long expires_in = event.getExpires_in();
	    if(access_token!=null){
        	WechatSession.this.app.setAccess_token(access_token);
        	WechatSession.this.app.setStatus("app is ok:onRunning");
        }else{
        	WechatSession.this.app.setStatus("app is error:"+event.getXml());
        }
        if (expires_in!= WechatSession.this.app.getExpires() && expires_in!=0) {
          WechatSession.this.app.setExpires(expires_in);
          WechatSession.this.app.setDelay(app.getExpires()* 1000L-100);
        }
    	return;
    }
    if (this.listener != null)
      this.listener.onEvent(event);
    userManager.onEvent(event);
  }

  public void onMessage(InMessage message){
    this.logger.info("IN:"+message.getXml());
    MsgType type = message.getMsgType();
    if ((type == MsgType.image) || (type == MsgType.video) || (type == MsgType.voice))
    {
      if ((message.getMediaId() != null) && (message.getMedia() == null)) {
    	  HttpRequest request = mediaManager.downloadMedia(message);
      	  if(request!=null){
          	  request.setAttribute(WechatSession.RequestAction, "onMessage");
          	  return;
      	  }
      }
    }
    if(message.getFrom()==null && message.getFromUserName()!=null){
    	HttpRequest request = userManager.getUser(message);
    	if(request!=null){
        	request.setAttribute(WechatSession.RequestAction, "onMessage");
        	return;
    	}
    }
    if (this.listener != null)
      this.listener.onMessage(message);
  }

  public HttpRequest sendText(RequestCallback callback, Text text, String touser) {
    OutMessage msg = new OutMessage(MsgType.text, touser);
    msg.setText(text);
    return msgManager.sendText(callback, msg);
  }
  
  public HttpRequest sendText(RequestCallback callback, Text text, String touser, CustomService customservice) {
	    OutMessage msg = new OutMessage(MsgType.text, touser);
	    msg.setText(text);
	    msg.setCustomservice(customservice);
	    return msgManager.sendText(callback, msg);
  }
  
  public HttpRequest sendNews(RequestCallback callback, News news, String touser) {
	    OutMessage msg = new OutMessage(MsgType.news, touser);
	    msg.setNews(news);
	    return msgManager.sendNews(callback, msg);
  }
  
  public HttpRequest sendNews(RequestCallback callback, News news, String touser, CustomService customservice) {
	    OutMessage msg = new OutMessage(MsgType.news, touser);
	    msg.setNews(news);
	    msg.setCustomservice(customservice);
	    return msgManager.sendNews(callback, msg);
  }
  
  public HttpRequest sendImage(RequestCallback callback, Image image, String touser) {
    OutMessage msg = new OutMessage(MsgType.image, touser);
    msg.setImage(image);
    
    HttpRequest msg_re = msgManager.sendMediaMsg(callback, msg);
    if(msg_re!=null)
    	return msg_re;
    
    msg_re = msgManager.getMsgRequest(callback, msg);
    if (msg.getMedia().getMedia() != null) {
    	HttpRequest request = mediaManager.uploadMedia(msg);
        request.setAttribute(RequestCallback, msg_re);
        request.setAttribute(RequestAction, "sendMediaMsg");
    }else{
    	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
    }
    return msg_re;
  }
  
  public HttpRequest sendImage(RequestCallback callback, Image image, String touser, CustomService customservice) {
	    OutMessage msg = new OutMessage(MsgType.image, touser);
	    msg.setImage(image);
	    msg.setCustomservice(customservice);
	    
	    HttpRequest msg_re = msgManager.sendMediaMsg(callback, msg);
	    if(msg_re!=null)
	    	return msg_re;
	    
	    msg_re = msgManager.getMsgRequest(callback, msg);
	    if (msg.getMedia().getMedia() != null) {
	    	HttpRequest request = mediaManager.uploadMedia(msg);
	        request.setAttribute(RequestCallback, msg_re);
	        request.setAttribute(RequestAction, "sendMediaMsg");
	    }else{
	    	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
	    }
	    return msg_re;
  }

  public HttpRequest sendMusic(RequestCallback callback, Music music, String touser) {
    OutMessage msg = new OutMessage(MsgType.music, touser);
    msg.setMusic(music);
    HttpRequest msg_re = msgManager.sendMediaMsg(callback, msg);
    if(msg_re!=null)
    	return msg_re;
    
    msg_re = msgManager.getMsgRequest(callback, msg);
    if (msg.getMedia().getMedia() != null) {
    	HttpRequest request = mediaManager.uploadMedia(msg);
        request.setAttribute(RequestCallback, msg_re);
        request.setAttribute(RequestAction, "sendMediaMsg");
    }else{
    	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
    }
    return msg_re;
  }
  
  public HttpRequest sendMusic(RequestCallback callback, Music music, String touser, CustomService customservice) {
	    OutMessage msg = new OutMessage(MsgType.music, touser);
	    msg.setMusic(music);
	    msg.setCustomservice(customservice);
	    HttpRequest msg_re = msgManager.sendMediaMsg(callback, msg);
	    if(msg_re!=null)
	    	return msg_re;
	    
	    msg_re = msgManager.getMsgRequest(callback, msg);
	    if (msg.getMedia().getMedia() != null) {
	    	HttpRequest request = mediaManager.uploadMedia(msg);
	        request.setAttribute(RequestCallback, msg_re);
	        request.setAttribute(RequestAction, "sendMediaMsg");
	    }else{
	    	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
	    }
	    return msg_re;
  }

  public HttpRequest sendVideo(RequestCallback callback, Video video, String touser) {
	  OutMessage msg = new OutMessage(MsgType.video, touser);
	  msg.setVideo(video);
	  HttpRequest msg_re = msgManager.sendMediaMsg(callback, msg);
	  if(msg_re!=null)
		  return msg_re;
	    
	  msg_re = msgManager.getMsgRequest(callback, msg);
	  if (msg.getMedia().getMedia() != null) {
		  HttpRequest request = mediaManager.uploadMedia(msg);
	      request.setAttribute(RequestCallback, msg_re);
	      request.setAttribute(RequestAction, "sendMediaMsg");
	  }else{
		  callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
	  }
	  return msg_re;
  }
  
  public HttpRequest sendVideo(RequestCallback callback, Video video, String touser, CustomService customservice) {
	  OutMessage msg = new OutMessage(MsgType.video, touser);
	  msg.setVideo(video);
	  msg.setCustomservice(customservice);
	  HttpRequest msg_re = msgManager.sendMediaMsg(callback, msg);
	  if(msg_re!=null)
		  return msg_re;
	    
	  msg_re = msgManager.getMsgRequest(callback, msg);
	  if (msg.getMedia().getMedia() != null) {
		  HttpRequest request = mediaManager.uploadMedia(msg);
	      request.setAttribute(RequestCallback, msg_re);
	      request.setAttribute(RequestAction, "sendMediaMsg");
	  }else{
		  callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
	  }
	  return msg_re;
  }

  public HttpRequest sendVoice(RequestCallback callback, Voice voice, String touser) {
      OutMessage msg = new OutMessage(MsgType.voice, touser);
      msg.setVoice(voice);
      HttpRequest msg_re = msgManager.sendMediaMsg(callback, msg);
      if(msg_re!=null)
      	return msg_re;
      
      msg_re = msgManager.getMsgRequest(callback, msg);
      if (msg.getMedia().getMedia() != null) {
      	HttpRequest request = mediaManager.uploadMedia(msg);
          request.setAttribute(RequestCallback, msg_re);
          request.setAttribute(RequestAction, "sendMediaMsg");
      }else{
      	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
      }
      return msg_re;
  }
  
  public HttpRequest sendVoice(RequestCallback callback, Voice voice, String touser, CustomService customservice) {
      OutMessage msg = new OutMessage(MsgType.voice, touser);
      msg.setVoice(voice);
	  msg.setCustomservice(customservice);
      HttpRequest msg_re = msgManager.sendMediaMsg(callback, msg);
      if(msg_re!=null)
      	return msg_re;
      
      msg_re = msgManager.getMsgRequest(callback, msg);
      if (msg.getMedia().getMedia() != null) {
      	HttpRequest request = mediaManager.uploadMedia(msg);
          request.setAttribute(RequestCallback, msg_re);
          request.setAttribute(RequestAction, "sendMediaMsg");
      }else{
      	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
      }
      return msg_re;
  }
  
  public HttpRequest broadcastText(RequestCallback callback, Text text, Filter filter){
	  BroadcastMessage msg = new BroadcastMessage(MsgType.text, filter);
	  msg.setText(text);
	  return msgManager.broadcastText(callback, msg);
  }
  
  public HttpRequest broadcastImage(RequestCallback callback, Image image, Filter filter) {
	  BroadcastMessage msg = new BroadcastMessage(MsgType.image, filter);
	  msg.setImage(image);
	  HttpRequest msg_re = msgManager.broadcastMediaMsg(callback, msg);
      if(msg_re!=null)
      	return msg_re;
      
      msg_re = msgManager.getBroadcastRequest(callback, msg);
      if (msg.getMedia().getMedia() != null) {
      	  HttpRequest request = mediaManager.uploadMedia(msg);
          request.setAttribute(RequestCallback, msg_re);
          request.setAttribute(RequestAction, "sendMediaMsg");
      }else{
      	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
      }
      return msg_re;
  }
  
  public HttpRequest broadcastVoice(RequestCallback callback, Voice voice, Filter filter) {
	  BroadcastMessage msg = new BroadcastMessage(MsgType.voice, filter);
      msg.setVoice(voice);
      HttpRequest msg_re = msgManager.broadcastMediaMsg(callback, msg);
      if(msg_re!=null)
      	return msg_re;
      
      msg_re = msgManager.getBroadcastRequest(callback, msg);
      if (msg.getMedia().getMedia() != null) {
      	  HttpRequest request = mediaManager.uploadMedia(msg);
          request.setAttribute(RequestCallback, msg_re);
          request.setAttribute(RequestAction, "sendMediaMsg");
      }else{
      	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
      }
      return msg_re;
  }
  
  public HttpRequest broadcastVideo(RequestCallback callback, Video video, Filter filter) {
	  BroadcastMessage msg = new BroadcastMessage(MsgType.video, filter);
	  msg.setVideo(video);
	  HttpRequest msg_re = msgManager.broadcastMediaMsg(callback, msg);
      if(msg_re!=null)
      	return msg_re;
      
      msg_re = msgManager.getBroadcastRequest(callback, msg);
      if (msg.getMedia().getMedia() != null) {
      	  HttpRequest request = mediaManager.uploadMedia(msg);
          request.setAttribute(RequestCallback, msg_re);
          request.setAttribute(RequestAction, "sendMediaMsg");
      }else{
      	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
      }
      return msg_re;
  }

  public HttpRequest broadcastNews(RequestCallback callback, Mpnews mpnews, Filter filter)throws Exception {
	  BroadcastMessage msg = new BroadcastMessage(MsgType.mpnews, filter);
	  msg.setMpnews(mpnews);
	  HttpRequest msg_re = msgManager.broadcastMediaMsg(callback, msg);
      if(msg_re!=null)
      	return msg_re;
      
      msg_re = msgManager.getBroadcastRequest(callback, msg);
      List<Articles> articles = mpnews.getArticles();
	  if(articles==null || articles.size() > News.max_size){
		  throw new Exception("articles is null or exceed the articles max size limit.\nmax size:"+News.max_size);
	  }
	  
	  boolean articles_ready = true;
	  for(Articles art : articles){
		  if(art.getMedia_id()==null){
			  articles_ready = false;
			  break;
		  }	  
	  }
	  
	  if(articles_ready){
		  OutMessage msg_news = new OutMessage(null,null);
		  msg_news.setArticles(articles);
		  HttpRequest request = msgManager.uploadNews(msg_news);
	      request.setHandler(msg);
          request.setAttribute(RequestCallback, msg_re);
          request.setAttribute(RequestAction, "uploadNews");
	  }else{
		  HttpRequest request = mediaManager.uploadMultiMedia(articles);
		  if(request!=null){
		      request.setHandler(msg);
	          request.setAttribute(RequestCallback, msg_re);
	          request.setAttribute(RequestAction, "uploadMultiMedia");
          }
	  }
	  return msg_re;
  }
  
  public HttpRequest createMenus(RequestCallback callback, Menus menus) {
    if (this.app == null) {
      this.logger.warn("app can't be null!");
      return null;
    }
    HttpRequest request = RequestFactory.createNewMenusReqeust(callback, this.app.getAccess_token(), menus.toJson());
    execute(request);
    return request;
  }

  public HttpRequest getMenus(RequestCallback callback) {
    if (this.app == null) {
      this.logger.warn("app can't be null!");
      return null;
    }
    HttpRequest request = RequestFactory.createGetMenusReqeust(callback, this.app.getAccess_token());
    execute(request);
    return request;
  }

  public HttpRequest deleteMenus(RequestCallback callback) {
    if (this.app == null) {
      this.logger.warn("app can't be null!");
      return null;
    }
    HttpRequest request = RequestFactory.createDeleteMenusReqeust(callback, this.app.getAccess_token());
    execute(request);
    return request;
  }

  public void onError(HttpRequest request, Error error)
  {
    logger.warn(error.toJson());
    
  }

  public void onOk(HttpRequest request, Object o){
	logger.info(o);
	String request_action = (String) request.getAttibute(WechatSession.RequestAction);
	if("uploadMultiMedia".equals(request_action)){
		List<?> medias = (List<?>)o;
		List<Articles> articles = new ArrayList<Articles>();
		for(Object o1 : medias){
			articles.add((Articles)o1);
		}
		Object handle  = request.getHandler();
		OutMessage msg = (OutMessage)handle;
		HttpRequest msg_re = (HttpRequest) request.getAttibute(WechatSession.RequestCallback);
		
		OutMessage msg_news = new OutMessage(null,null);
		msg_news.setArticles(articles);
		request = msgManager.uploadNews(msg_news);
	    request.setHandler(msg);
        request.setAttribute(RequestCallback, msg_re);
        request.setAttribute(RequestAction, "uploadNews");
	}
  }
  
  public boolean appVerify(String signature, String timestamp, String nonce){
	  String token = getApp()!=null?getApp().getToken():null;
	  if(token!=null && signature!=null && timestamp!=null && nonce!=null){
		  return signature.equalsIgnoreCase(WechatUtils.createSignature(token, timestamp, nonce));
	  }
	  return false;
  }

  public void execute(HttpRequest request) {
	// TODO Auto-generated method stub
	  logger.info("out:"+(request.getContent()==null?HttpRequest.GET:request.getContent()));
	  ThreadManager.instance().execute(request);
  }

	public UserManager getUserManager() {
		return userManager;
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public MsgManager getMsgManager() {
		return msgManager;
	}
	
	public void setMsgManager(MsgManager msgManager) {
		this.msgManager = msgManager;
	}
	
	public MediaManager getMediaManager() {
		return mediaManager;
	}
	
	public void setMediaManager(MediaManager mediaManager) {
		this.mediaManager = mediaManager;
	} 
}