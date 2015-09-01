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

import java.util.List;
import java.util.TimerTask;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jcommon.com.util.collections.MapStoreListener;
import org.jcommon.com.util.jmx.Monitor;
import org.jcommon.com.util.thread.TimerTaskManger;
import org.jcommon.com.wechat.cache.ContentTypeCache;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Router;

public class WechatSessionManager extends Monitor
  implements MapStoreListener
{
  private Logger logger = Logger.getLogger(getClass());
  private static WechatSessionManager instance;
  private ContentTypeCache content_type_cache;

  public static WechatSessionManager instance()
  {
    if (instance == null) {
      new WechatSessionManager();
    }
    return instance;
  }

  public WechatSessionManager() {
    super("WechatSessionManager");
    instance = this;
    content_type_cache = new ContentTypeCache(System.getProperty("java.io.tmpdir"),"content_type_cache",".wehcat",-1);
  }

  @Override 
  public void initOperation() {
		// TODO Auto-generated method stub
		addOperation(new MBeanOperationInfo(
	          "removeSession",
	          "removeSession",
	          new MBeanParameterInfo[]{new MBeanParameterInfo(
	          		"wechat_key","java.lang.String","wechat_key")},   // no parameters
	          "void",
	          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "restartSession",
		          "restartSession",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechat_key","java.lang.String","wechat_key")},   // no parameters
		          "void",
		          MBeanOperationInfo.ACTION));
  }
  
  public void removeSession(String wechat_key){
	  WechatSession session = getWechatSession(wechat_key);
	  if(session!=null)
		  session.shutdown();
  }
  
  public void restartSession(String wechat_key){
	  final WechatSession session = getWechatSession(wechat_key);
	  if(session!=null)
		  session.shutdown();
	  
	  TimerTaskManger.instance().schedule("restart wechat session", 
			  new TimerTask(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					session.startup();
				}
		  
	  }, 3000);
  }
  
  public boolean isSynchronized() {
    return true;
  }

  public WechatSession getWechatSession(String key) {
    return SessionCache.instance().getWechatSession(key);
  }

  public List<WechatSession> getAllWechatSession() {
    return SessionCache.instance().getAllWechatSession();
  }

  private void addSessioin(WechatSession session) {
    if (session == null)
      return;
    String key = session.getWechatID();
    String value = session.getApp() != null ? session.getApp().toJson() : "app is null";
    super.addProperties(key, value);
  }

  private void removeSessioin(WechatSession session) {
    if (session == null)
      return;
    String key = session.getWechatID();
    super.removeProperties(key);
  }

  public void shutdown() {
    super.shutdown();
    List<WechatSession> list = getAllWechatSession();
    for (WechatSession session : list) {
      session.shutdown();
    }
    list.clear();
    SessionCache.instance().removeMapStoreListener(this);
  }

  public void startup() {
    super.startup();
    SessionCache.instance().addMapStoreListener(this);
  }

  public boolean addOne(Object key, Object value)
  {
    if (value == null) return false;
    addSessioin((WechatSession)value);
    return false;
  }

  public boolean updateOne(Object key, Object value)
  {
	if (value == null) return false;
	  addSessioin((WechatSession)value);
    return false;
  }

  public Object removeOne(Object key)
  {
    if (key == null) return key;
    removeSessioin(getWechatSession((String)key));
    return key;
  }

  public void onCallback(String signature, String timestamp, String nonce, String xml) {
    this.logger.info(xml);
    Document document = null;
    Element root = null;
    boolean done = false;
    String touser = null;
    try {
      document = DocumentHelper.parseText(xml);
      root = document.getRootElement();
    } catch (DocumentException e) {
      this.logger.error(xml, e);
    }
    if (root == null) return;
    
    List<WechatSession> sessions = SessionCache.instance().getAllWechatSession();
    if (root.element("Event") != null) {
      Event event = new Event(xml);
      touser = event.getToUserName();
      for(WechatSession session : sessions){
    	  if(session instanceof WechatSessionRouter){
    		  ((WechatSessionRouter)session).onRouter(new Router(signature, timestamp, nonce, xml));
    		  done = true;
    	  }
    	  else if(touser!=null && touser.equals(session.getWechatID())){
    		  session.onEvent(event);
    		  done = true;
    	  }
      }
    } else {
      InMessage msg = new InMessage(xml);
      touser = msg.getToUserName();
      for(WechatSession session : sessions){
    	  if(session instanceof WechatSessionRouter){
    		  ((WechatSessionRouter)session).onRouter(new Router(signature, timestamp, nonce, xml));
    		  done = true;
    	  }
    	  else if(touser!=null && touser.equals(session.getWechatID())){
    		  session.onMessage(msg);
    		  done = true;
    	  }
      }
    }
    if(!done)
    	logger.warn("can't find session of "+touser);
  }

  public boolean appVerify(String signature, String timestamp, String nonce) {
	List<WechatSession> sessions = SessionCache.instance().getAllWechatSession();  
	for(WechatSession session : sessions){
		if(session.appVerify(signature, timestamp, nonce))
			return true;
	}
    return false;
  }

  public ContentTypeCache getContent_type_cache() {
	return content_type_cache;
  }

}