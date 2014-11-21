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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.util.thread.TimerTaskManger;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.GroupFilter;
import org.jcommon.com.wechat.data.Image;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Menus;
import org.jcommon.com.wechat.data.Mpnews;
import org.jcommon.com.wechat.data.Music;
import org.jcommon.com.wechat.data.News;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.data.Video;
import org.jcommon.com.wechat.data.Voice;
import org.jcommon.com.wechat.utils.MsgType;
import org.json.JSONException;
import org.json.JSONObject;

public class WechatSession extends ResponseHandler
  implements WechatSessionListener
{
  protected Logger logger = Logger.getLogger(getClass());
  private String wechatID;
  private App app;
  private WechatSessionListener listener;
  private final String WECHATMEDIA     = "wechat-media";
  public final static String  WECHATMEDIAPATH = "wechat-media-path";
  public final static String  WECHATMEDIAURL  = "wechat-media-url";
  
  private Timer app_keepalive;

  public WechatSession(String wechatID, App app, WechatSessionListener listener)
  {
    this.wechatID = wechatID;
    this.app = app;
    this.listener = listener;
  }

  public void startup() {
	logger.info(wechatID);
    SessionCache.instance().addWechatSession(this);
    appKeepAlive(this.app); 
  }

  public void shutdown() {
    SessionCache.instance().removeWechatSession(this);
    try {
      if(app_keepalive!=null)
    	  this.app_keepalive.cancel();
      this.app_keepalive = null; } catch (Exception e) {
    }
    logger.info(wechatID);
  }

  private void appKeepAlive(final App app) {
    if (app == null) {
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
    		 ThreadManager.instance().execute(RequestFactory.createAccessTokenReqeust(new HttpListener(){

    			 public void onSuccessful(HttpRequest reqeust, StringBuilder sResult)
    			 {
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
    			    WechatSessionManager.instance().updateStatus(WechatSession.this);
    			  }

    			  public void onFailure(HttpRequest reqeust, StringBuilder sResult)
    			  {
    			    logger.warn(sResult.toString());
    			    WechatSession.this.app.setStatus("app is error:onFailure");
    			    WechatSessionManager.instance().updateStatus(WechatSession.this);
    			    WechatSession.this.appKeepAlive(WechatSession.this.app);
    			  }

    			  public void onTimeout(HttpRequest reqeust)
    			  {
    			    logger.error("timeout");
    			    WechatSession.this.app.setStatus("app is error:onTimeout");
    			    WechatSessionManager.instance().updateStatus(WechatSession.this);
    			    WechatSession.this.appKeepAlive(WechatSession.this.app);
    			  }

    			  public void onException(HttpRequest reqeust, Exception e)
    			  {
    			    logger.error("", e);
    			    WechatSession.this.app.setStatus("app is error:onException");
    			    WechatSessionManager.instance().updateStatus(WechatSession.this);
    			    WechatSession.this.appKeepAlive(WechatSession.this.app);
    			  }
    			 
    		 },app));
    	}
    }, app.getDelay(), app.getExpires() > 0L ? app.getExpires()*1000 - 100L : 7200000L);
  }

  public App getApp()
  {
    return this.app;
  }

  public String getWechatID() {
    return this.wechatID;
  }

  public void setListener(WechatSessionListener listener) {
    this.listener = listener;
  }

  public void onEvent(Event event)
  {
    this.logger.info(event.getXml());
    if (this.listener != null)
      this.listener.onEvent(event);
  }

  public void onMessage(InMessage message)
  {
    this.logger.info(message.getXml());
    MsgType type = message.getMsgType();
    if ((type == MsgType.image) || (type == MsgType.video) || (type == MsgType.voice))
    {
      if ((message.getMediaId() != null) && (message.getMedia() == null)) {
        String path = System.getProperty(WECHATMEDIAPATH, System.getProperty("java.io.tmpdir"));
        File file = new File(path);
        if(!file.exists()){
        	logger.info(file.getAbsolutePath() + ":" + file.mkdirs());
        }
        FileRequest request = (FileRequest)RequestFactory.createMediaDownloaddRequest(this, this.app.getAccess_token(), message.getMediaId(), file);
        request.setHandler(message);
        addHandlerObject(request, org.jcommon.com.wechat.data.Media.class);
        ThreadManager.instance().execute(request);
        return;
      }
    }
    if(message.getFrom()==null && message.getFromUserName()!=null){
    	HttpRequest request = RequestFactory.createUserInfoRequest(this, app.getAccess_token(), message.getFromUserName(), null);
    	request.setHandler(message);
    	addHandlerObject(request,User.class);
    	ThreadManager.instance().execute(request);
    	return;
    }
    if (this.listener != null)
      this.listener.onMessage(message);
  }

  public HttpRequest sendText(RequestCallback callback, Text text, String touser) {
    OutMessage msg = new OutMessage(MsgType.text, touser);
    msg.setText(text);
    HttpRequest msg_re = getMsgRequest(callback, msg);
    ThreadManager.instance().execute(msg_re);
    return msg_re;
  }
  
  public HttpRequest sendNews(RequestCallback callback, List<Articles> articles, String touser) throws Exception {
	    if(articles==null || articles.size() > News.max_size){
	    	throw new Exception("articles is null or exceed the articles max size limit.\nmax size:"+News.max_size);
	    }
	    OutMessage msg = new OutMessage(MsgType.news, touser);
	    msg.setNews(new News(articles));
	    HttpRequest msg_re = getMsgRequest(callback, msg);
	    ThreadManager.instance().execute(msg_re);
	    return msg_re;
  }
  
  public HttpRequest sendNews(RequestCallback callback, List<Articles> articles) throws Exception {
	    if(articles==null || articles.size() > News.max_size){
	    	throw new Exception("articles is null or exceed the articles max size limit.\nmax size:"+News.max_size);
	    }
	    OutMessage msg = new OutMessage(MsgType.news, null);
	    msg.setNews(new News(articles));
	    HttpRequest msg_re = getMsgRequest(callback, msg);
	    msg_re.setProperty(WECHATMEDIA, msg.toJson());
	    
	    HttpRequest request = RequestFactory.createGetUsersReqeust(this, getApp().getAccess_token(), null);
	    request.setHandler(msg_re);
	    addHandlerObject(request, User.class);
	    ThreadManager.instance().execute(request);
	    return msg_re;
  }

  public HttpRequest sendImage(RequestCallback callback, Image image, String touser) {
    OutMessage msg = new OutMessage(MsgType.image, touser);
    msg.setImage(image);
    HttpRequest msg_re = getMsgRequest(callback, msg);
    if (image.getMedia() != null) {
      msg_re.setProperty(WECHATMEDIA, msg.toJson());
      FileRequest request = (FileRequest)RequestFactory.createMediaUploadRequest(this, this.app.getAccess_token(), image.getMedia(), image.getType());

      request.setHandler(msg_re);
      addHandlerObject(request, Media.class);
      ThreadManager.instance().execute(request);
    } else {
      this.logger.info(msg.toJson());
      ThreadManager.instance().execute(msg_re);
    }
    return msg_re;
  }

  public HttpRequest sendMusic(RequestCallback callback, Music music, String touser) {
    OutMessage msg = new OutMessage(MsgType.music, touser);
    msg.setMusic(music);
    HttpRequest msg_re = getMsgRequest(callback, msg);
    ThreadManager.instance().execute(msg_re);
    return msg_re;
  }

  public HttpRequest sendVideo(RequestCallback callback, Video video, String touser) {
    OutMessage msg = new OutMessage(MsgType.video, touser);
    msg.setVideo(video);
    HttpRequest msg_re = getMsgRequest(callback, msg);
    if (video.getMedia() != null) {
      msg_re.setProperty(WECHATMEDIA, msg.toJson());
      FileRequest request = (FileRequest)RequestFactory.createMediaUploadRequest(this, this.app.getAccess_token(), video.getMedia(), video.getType());

      request.setHandler(msg_re);
      addHandlerObject(request, Media.class);
      ThreadManager.instance().execute(request);
    } else {
      ThreadManager.instance().execute(msg_re);
    }
    return msg_re;
  }

  public HttpRequest sendVoice(RequestCallback callback, Voice voice, String touser) {
    OutMessage msg = new OutMessage(MsgType.voice, touser);
    msg.setVoice(voice);
    HttpRequest msg_re = getMsgRequest(callback, msg);
    if (voice.getMedia() != null) {
      msg_re.setProperty(WECHATMEDIA, msg.toJson());
      FileRequest request = (FileRequest)RequestFactory.createMediaUploadRequest(this, this.app.getAccess_token(), voice.getMedia(), voice.getType());

      request.setHandler(msg_re);
      addHandlerObject(request, Media.class);
      ThreadManager.instance().execute(request);
    } else {
      ThreadManager.instance().execute(msg_re);
    }
    return msg_re;
  }
  
  public HttpRequest sendBroadcast(RequestCallback callback, Articles articles){
	  OutMessage msg = new OutMessage(MsgType.mpnews,null);
	  HttpRequest msg_re = RequestFactory.createBroadcastRequest(callback, this.app.getAccess_token(), msg.toJson());
	  
	  if(articles.getMedia()!=null){
		  FileRequest request = (FileRequest)RequestFactory.createMediaUploadRequest(this, this.app.getAccess_token(), articles.getMedia(), articles.getType());
		  
		  request.setHandler(msg_re);
		  msg_re.setProperty(WECHATMEDIA, msg.toJson());
		  msg_re.setAttribute(Articles.class.getName(), articles);
		  addHandlerObject(request, Media.class);
		  ThreadManager.instance().execute(request);
	  }
	  
	  return msg_re;
  }

  private HttpRequest getMsgRequest(RequestCallback callback, OutMessage msg)
  {
    if (this.app == null) {
      this.logger.warn("app can't be null!");
      return null;
    }
    HttpRequest request = RequestFactory.createMsgReqeust(callback, this.app.getAccess_token(), msg.toJson());
    return request;
  }

  public HttpRequest createMenus(RequestCallback callback, Menus menus) {
    if (this.app == null) {
      this.logger.warn("app can't be null!");
      return null;
    }
    HttpRequest request = RequestFactory.createNewMenusReqeust(callback, this.app.getAccess_token(), menus.toJson());
    ThreadManager.instance().execute(request);
    return request;
  }

  public HttpRequest getMenus(RequestCallback callback) {
    if (this.app == null) {
      this.logger.warn("app can't be null!");
      return null;
    }
    HttpRequest request = RequestFactory.createGetMenusReqeust(callback, this.app.getAccess_token());
    ThreadManager.instance().execute(request);
    return request;
  }

  public HttpRequest deleteMenus(RequestCallback callback) {
    if (this.app == null) {
      this.logger.warn("app can't be null!");
      return null;
    }
    HttpRequest request = RequestFactory.createDeleteMenusReqeust(callback, this.app.getAccess_token());
    ThreadManager.instance().execute(request);
    return request;
  }

  public void onError(HttpRequest request, Error error)
  {
    this.logger.warn(error.toJson());
    if ((request instanceof FileRequest)) {
      FileRequest re = (FileRequest)request;
      Object handle = re.getHandler();
      Media m = new Media();

      if ((handle instanceof InMessage)) {
        InMessage h = (InMessage)handle;
        h.setMedia(m);
        onMessage(h);
      } else if ((handle instanceof HttpRequest)) {
        HttpRequest re_ = (HttpRequest)handle;
        ThreadManager.instance().execute(re_);
      }
    }
  }

  public void onOk(HttpRequest request, Object o)
  {
	logger.info(o);
    if ((request instanceof FileRequest)) {
      FileRequest re = (FileRequest)request;
      Object handle = re.getHandler();
      Media m = null;
      if ((o instanceof Media)) {
        m = (Media)o;
      } else {
        m = new Media();
      }
      m.setContent_type(re.getContent_type());
      m.setMedia(re.getFile());
      if ((handle instanceof InMessage)) {
        InMessage h = (InMessage)handle;
        h.setMedia(m);
        onMessage(h);
      } else if ((handle instanceof HttpRequest)) {
        HttpRequest re_ = (HttpRequest)handle;
        if (re_.getProperty(WECHATMEDIA) != null) {
          OutMessage out = new OutMessage(re_.getProperty(WECHATMEDIA));

          MsgType type = MsgType.getType(out.getMsgtype());
          if ((type == MsgType.image) && (out.getImage() != null))
            out.getImage().setMedia_id(m.getMedia_id());
          else if ((type == MsgType.video) && (out.getVideo() != null))
            out.getVideo().setMedia_id(m.getMedia_id());
          else if ((type == MsgType.voice) && (out.getVoice() != null)) {
            out.getVoice().setMedia_id(m.getMedia_id());
          }else if ((type == MsgType.mpnews)) {
        	  Articles articles =  (Articles) re_.getAttibute(Articles.class.getName());
        	  articles.setThumb_media_id(m.getMedia_id());
        	  handlerAfterUploadMedia(re_,articles);
        	  return;
          }
          this.logger.info("out:" + out.toJson());
          HttpRequest re_temp = getMsgRequest(null, out);
          if (re_temp != null)
            re_.setContent(re_temp.getContent());
          else
            this.logger.warn("re_temp is null");
        }
        this.logger.info(re_.getContent());
        ThreadManager.instance().execute(re_);
      } else {
        this.logger.warn(handle);
      }
    }else{
    	 Object handle = request.getHandler();
    	 User u = null;
    	 Media m = null;
    	 Group g = null;
         if ((o instanceof Media)) {
           m = (Media)o;
         }else if ((o instanceof User)) {
           u = (User)o;
         } else if ((o instanceof Group)) {
           g = (Group)o;
         } else if ((o instanceof Error)) {
        	 Error error = (Error)o;
        	 if ((handle instanceof HttpRequest)){
        		 HttpRequest re_ = (HttpRequest)handle;
        		 HttpListener listener = re_.getListener();
        		 if(listener!=null){
        			 RequestCallback callback = (RequestCallback) listener;
        			 callback.onSuccessful(re_, new StringBuilder(error.getJson()));
        		 }
        	 }
         } else {
           u = new User(null);
         }
         if ((handle instanceof InMessage)) {
           InMessage h = (InMessage)handle;
           h.setFrom(u);
           onMessage(h);
         }else  if ((handle instanceof HttpRequest)) {
        	 HttpRequest re_ = (HttpRequest)handle;
        	 if (re_.getProperty(WECHATMEDIA) != null) {
        		 if(g!=null){
        			 OutMessage out = new OutMessage(re_.getProperty(WECHATMEDIA));
        			 List<Group> group_list = Group.getGroups();
                	 if(group_list!=null){
                		 GroupFilter gf = new GroupFilter(group_list);
                		 out.setFilter(gf);
                		 re_.setContent(out.toJson());
                		 this.logger.info(re_.getContent());
                         ThreadManager.instance().execute(re_);
                	 }
        			 return ;
        		 }
        		 
        		 List<String> tos = User.getOpenids();
        		 if(tos==null)
        			 logger.warn("tos is null");
        		 else{
        			 OutMessage out = new OutMessage(re_.getProperty(WECHATMEDIA));
        			 for(String to : tos){
        				 out.setTouser(to);
                         HttpRequest re_temp = getMsgRequest(null, out);
                         if (re_temp != null)
                             re_.setContent(re_temp.getContent());
                         else
                             this.logger.warn("re_temp is null");
                         this.logger.info(re_.getContent());
                         ThreadManager.instance().execute(re_);
        			 }
        		 }     
             }else{
            	 OutMessage out = new OutMessage(MsgType.mpnews,null);
            	 Mpnews mp      = new Mpnews();
            	 mp.setMedia_id(m.getMedia_id());
            	 
            	 List<Group> group_list = Group.getGroups();
            	 if(group_list!=null){
            		 GroupFilter gf = new GroupFilter(group_list);
            		 out.setFilter(gf);
            		 re_.setContent(out.toJson());
            		 this.logger.info(re_.getContent());
                     ThreadManager.instance().execute(re_);
            	 }else{
            		 HttpRequest msg_re = RequestFactory.createGetGroupsReqeust(this,this.app.getAccess_token());
            		 msg_re.setHandler(re_);
            		 re_.setProperty(WECHATMEDIA,out.toJson());
            		 addHandlerObject(msg_re, Group.class);
            	 }
             }
           }  
         
    }
  }

  private void handlerAfterUploadMedia(HttpRequest re_, Articles articles) {
	// TODO Auto-generated method stub
	  OutMessage msg = new OutMessage();
	  List<Articles> list = new ArrayList<Articles>();
	  list.add(articles);
	  msg.setArticles(list);
	  
	  HttpRequest msg_re = RequestFactory.createBroadcastRequest(this, this.app.getAccess_token(), msg.toJson());
	  re_.getProperties().remove(WECHATMEDIA);
	  msg_re.setHandler(re_);
	  addHandlerObject(msg_re, Media.class);
	  ThreadManager.instance().execute(msg_re);
  }
}