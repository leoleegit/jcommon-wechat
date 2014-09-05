package org.jcommon.com.wechat.test;

import java.util.List;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.jmx.Monitor;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionListener;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Text;

public class WechatSessionTest extends Monitor
  implements WechatSessionListener, RequestCallback
{
  private Logger logger = Logger.getLogger(getClass());

  public WechatSessionTest() {
    super("WechatSessionTest");
    String path = System.getProperty("user.dir")+ java.io.File.separator + "media";
    System.setProperty(WechatSession.WECHATMEDIAPATH, path);
    System.setProperty(WechatSession.WECHATMEDIAURL, "media");
  }

  public void initOperation()
  {
    addOperation(new MBeanOperationInfo("newWechatSession", "new WechatSession", new MBeanParameterInfo[] { new MBeanParameterInfo("appid", "java.lang.String", "appid"), new MBeanParameterInfo("secret", "java.lang.String", "secret"), new MBeanParameterInfo("Token", "java.lang.String", "app Token"), new MBeanParameterInfo("wechatID", "java.lang.String", "wechatID") }, "void", 1));

    addOperation(new MBeanOperationInfo("deleteWechatSession", "delete WechatSession", new MBeanParameterInfo[] { new MBeanParameterInfo("wechatID", "java.lang.String", "wechatID") }, "void", 1));

    addOperation(new MBeanOperationInfo("sendTextMsg", "sendTextMsg", new MBeanParameterInfo[] { new MBeanParameterInfo("touser", "java.lang.String", "touser"), new MBeanParameterInfo("content", "java.lang.String", "content") }, "void", 1));

    super.initOperation();
  }

  public void newWechatSession(String appid, String secret, String Token, String wechatID) {
    App app = new App(appid, secret, Token);
    WechatSession session = new WechatSession(wechatID, app, this);
    session.startup();
  }

  public void deleteWechatSession(String wechatID) {
    WechatSession session = SessionCache.instance().getWechatSession(wechatID);
    if (session != null)
      session.shutdown();
  }

  public void sendTextMsg(String touser, String content) {
    List list = SessionCache.instance().getAllWechatSession();
    if ((list == null) || (list.size() == 0)) {
      this.logger.warn("can't find wechat session!");
      return;
    }
    WechatSession session = (WechatSession)list.get(0);
    Text text = new Text(content);
    session.sendText(this, text, touser);
  }

  public void onSuccessful(HttpRequest reqeust, StringBuilder sResult)
  {
    this.logger.info(sResult.toString());
  }

  public void onFailure(HttpRequest reqeust, StringBuilder sResult)
  {
    this.logger.info(sResult.toString());
  }

  public void onTimeout(HttpRequest reqeust)
  {
    this.logger.error("timeout");
  }

  public void onException(HttpRequest reqeust, Exception e)
  {
    this.logger.error("", e);
  }

  public void onEvent(Event event)
  {
    this.logger.info(event.getXml());
  }

  public void onMessage(InMessage message)
  {
    this.logger.info(message.getXml());
    if(message.getMedia()!=null){
    	logger.info(message.getMedia().getMedia() + "	"+message.getMedia().getContent_type());
    	logger.info(message.getMedia().getUrl());
    }
    this.logger.info(message.getFrom()!=null?message.getFrom().getNickname():"from user is null !");
  }
}