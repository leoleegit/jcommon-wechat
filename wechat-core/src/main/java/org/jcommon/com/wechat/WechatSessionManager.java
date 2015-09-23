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
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Token;

public class WechatSessionManager extends Monitor
  implements MapStoreListener
{
  private Logger logger = Logger.getLogger(getClass());
  private static WechatSessionManager instance;

  public static WechatSessionManager instance(){
    if (instance == null) {
      new WechatSessionManager();
    }
    return instance;
  }

  public WechatSessionManager() {
    super("WechatSessionManager");
    instance = this;
  }

  @Override 
  public void initOperation() {
		// TODO Auto-generated method stub
		addOperation(new MBeanOperationInfo(
	          "removeSession",
	          "removeSession",
	          new MBeanParameterInfo[]{new MBeanParameterInfo(
	          		"wechatID","java.lang.String","wechatID")},   // no parameters
	          "void",
	          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "restartSession",
		          "restartSession",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID")},   // no parameters
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "addSession",
		          "addSession",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
				          		"appid","java.lang.String","appid"),
			          new MBeanParameterInfo(
				          		"secret","java.lang.String","secret"),
			          new MBeanParameterInfo(
				          		"Token","java.lang.String","Token")},   // no parameters
		          "void",
		          MBeanOperationInfo.ACTION));
  }
  
  public void removeSession(String wechat_key){
	  WechatSession session = getWechatSession(wechat_key);
	  if(session!=null)
		  session.shutdown();
  }
  
  public void addSession(String wechatID, String appid, String secret, String Token){
	  logger.info(String.format("wechatID:%s ; appid:%s ; secret:%s ; Toke:%s ;", wechatID,appid,secret,Token));
	  WechatSession session = getWechatSession(wechatID);
	  if(session==null){
		  App app = new App(appid,secret,Token);
		  session = new WechatSession(wechatID,app,null);
		  session.startup();
	  }else{
		  logger.info(String.format("session of %s have been exist!", wechatID));
	  }
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
  
  public void onToken(String signature, String timestamp, String nonce, String xml){
	  this.logger.info(xml);
	  Token token = new Token(xml,signature,timestamp,nonce);
	  String touser = token.getWechatID();
	  boolean done = false;
	  List<WechatSession> sessions = SessionCache.instance().getAllWechatSession();
	  for(WechatSession session : sessions){
		  if((touser!=null && touser.equals(session.getWechatID()))||"*".equals(session.getWechatID())){
	    	  session.onToken(token);
    		  done = true;
    	  }
      }
	  if(!done)
	    	logger.warn("can't find session of "+touser);
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
      Event event = new Event(xml,signature,timestamp,nonce);
      touser = event.getToUserName();
      for(WechatSession session : sessions){
    	  if((touser!=null && touser.equals(session.getWechatID()))||"*".equals(session.getWechatID())){
    		  session.onEvent(event);
    		  done = true;
    	  }
      }
    } else {
      InMessage msg = new InMessage(xml,signature,timestamp,nonce);
      touser = msg.getToUserName();
      for(WechatSession session : sessions){
    	  if((touser!=null && touser.equals(session.getWechatID()))||"*".equals(session.getWechatID())){
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

}