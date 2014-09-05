package org.jcommon.com.wechat.test;

import java.io.File;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;


import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.Callback;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.RequestFactory;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Button;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.JsonObject;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Menus;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.utils.ButtonType;
import org.jcommon.com.wechat.utils.MD5;

public class UtilsTest
  implements RequestCallback
{
  public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException
  {
	  //System.out.println(MD5.getMD5("image/jpeg".getBytes()));
	  //String url = "file://c://ddd/dfd/media/userid/picname.jsp";
	  //System.out.println(url.substring(url.indexOf("media")+"media".length()));
//    new Callback();
    //String access_token = "MQS-dY_JT_dzJ07niahioMHEEAjregVEHigwUlNi3sr2S6MzV0PkTab4_lEv01zhDJbo_7Wy2GLC_UIja62jcKzTcU_9Vdiw94uTivd85pEZcOFk7aLIDZ_XhOTkK88J8kxiUyElogFOsNsMsQ7w2A";
//    
//    App app = new App(access_token,"wx742941360129cd17", "37492ad273076440c0f123716865e1da", "spotlight-wechat");
//    WechatSession session = new WechatSession("gh_f49bb9a333b3", app, null);
//    //session.startup();
//    
//    //Thread.sleep(10000);
//    Button button = new Button("button1", ButtonType.click, "button_key_1");
//    Menus menus = new Menus();
//    menus.addButton(button);
////
//    button = new Button("button2", null);
//    button.addSubButton(new Button("sub_button1", ButtonType.view, "http://www.baidu.com"));
//    button.addSubButton(new Button("sub_button2", ButtonType.click, "sub_button2_key"));
//    button.addSubButton(new Button("sub_button3", ButtonType.click, "sub_button3_key"));
//    menus.addButton(button);
////
//    button = new Button("button3", null);
//    button.addSubButton(new Button("sub_button31", ButtonType.view, "http://www.baidu.com"));
//    button.addSubButton(new Button("sub_button32", ButtonType.click, "sub_button32_key"));
//    button.addSubButton(new Button("sub_button33", ButtonType.click, "sub_button33_key"));
//    menus.addButton(button);

//    String path = System.getProperty("WECHATMEDIAPATH", System.getProperty("java.io.tmpdir"));
//    File file = new File(path);
//    System.out.println(file.isDirectory());
//    String str = "{\"touser\":\"of-YetzJFYxGTltb4eCvgccHzHF0\",\"msgtype\":\"image\",\"image\":{\"type\":\"image\",\"created_at\":\"0\"}}";
////    OutMessage out = new OutMessage(str);
//    System.out.println(menus.toJson());
    
//    Text text = new Text("hello test");
//    session.sendText(new UtilsTest(), text, "of-Yet4J6xQ6Yx_M_n8Doe5GtJC4");
////    
//     
//     HttpRequest request  = RequestFactory.createUserInfoRequest(new UtilsTest(), access_token, "of-Yet4J6xQ6Yx_M_n8Doe5GtJC4", null);
//     request.run();
//       FileRequest request  = (FileRequest) RequestFactory.createMediaDownloaddRequest(new UtilsTest(), access_token, "AEFjcYxnuX6sdMi-78tCwQEZX5AUM-aPdg_lR1gitHsjufDCDs8ZlcpmrYtp3phq", new File(System.getProperty("java.io.tmpdir")));
//	   request.run();
//	   System.out.println(request.getContent_type());
    //System.out.println(JsonObject.class.isAssignableFrom(org.jcommon.com.wechat.data.Media.class));
	  Event event = new Event("");
	  System.out.println(event.toXml());
  }

  public void onSuccessful(HttpRequest reqeust, StringBuilder sResult)
  {
    System.out.println(sResult);
  }

  public void onFailure(HttpRequest reqeust, StringBuilder sResult)
  {
    System.out.println(sResult);
  }

  public void onTimeout(HttpRequest reqeust)
  {
  }

  public void onException(HttpRequest reqeust, Exception e)
  {
  }
}