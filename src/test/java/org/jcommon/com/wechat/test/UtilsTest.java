package org.jcommon.com.wechat.test;

import java.io.File;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.Callback;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.RequestFactory;
import org.jcommon.com.wechat.ResponseHandler;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.data.Button;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.GroupFilter;
import org.jcommon.com.wechat.data.JsonObject;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Menus;
import org.jcommon.com.wechat.data.Mpnews;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.utils.ButtonType;
import org.jcommon.com.wechat.utils.MD5;
import org.jcommon.com.wechat.utils.MsgType;

public class UtilsTest
  extends ResponseHandler
{
  public static void main(String[] args) throws Exception
  {
	  new Callback();
	  //System.out.println(MD5.getMD5("image/jpeg".getBytes()));
	  //String url = "file://c://ddd/dfd/media/userid/picname.jsp";
	  //System.out.println(url.substring(url.indexOf("media")+"media".length()));
//    new Callback();
//      String access_token = "bxDacvoohUuOU3YfS3okopE1YJJ1ghnH0d2E_OOl1DihDD5KDyRgP8U9zf_r24zolK02dGbIF3S4MtdQHYsjJm7BKmnzL5-Ikd_ZqRH6RyA";
//////    
      App app = new App("wxe3493e70ee036e60", "bc54d3dee215742ce37c700e2d2bc2a2", "spotlight-wechat");
      WechatSession session = new WechatSession("gh_e6e86fdce3b9", app, null);
      session.startup();
      Thread.sleep(10000);
      List<Articles> articles = new ArrayList<Articles>();
      String title = "Welcome to Apache HBase";
      String description = "Apache HBase is the Hadoop database, a distributed, scalable, big data store.";
      String url = "http://hbase.apache.org/";
      String picurl = "http://hbase.apache.org/images/hbase_logo.png";
      Articles art = new Articles(title,description,url,picurl);
      articles.add(art);
      
//      title = "SpotlightX";
//      description = "SpotlightX description.";
//      url = "http://demo.protel.com.hk/2014-11-13_01.html";
//      picurl = "http://www.protelnet.com/en/images/enspark_logo.jpg";
//      art = new Articles(title,description,url,picurl);
//      articles.add(art);
      
      session.sendNews(new UtilsTest(), articles);

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
//	  Event event = new Event("");
//	  System.out.println(event.toXml());
	  
//	  String groups = "{\"groups\": [{\"id\": 0,\"name\": \"未分组\", \"count\": 72596}, {\"id\": 1, \"name\": \"黑名单\", \"count\": 36}]}";
//	  Group g = new Group(groups);
//	  
//	  
//	  OutMessage out = new OutMessage(MsgType.mpnews,"leolee");
//	  Mpnews np = new Mpnews();
//	  np.setMedia_id("123dsdajkasd231jhksad");
//	  out.setFilter(new GroupFilter(Group.getGroups()));
//	  out.setMpnews(np);
//	  System.out.println(out.toJson());
	  
//	  UtilsTest ut = new UtilsTest();
//	  HttpRequest fr = RequestFactory.createGetUsersReqeust(ut, session.getApp().getAccess_token(), null);
//	  ut.addHandlerObject(fr, User.class);
//      ThreadManager.instance().execute(fr);
//	  String str = "{\"total\":5,\"count\":5,\"data\":{\"openid\":[\"oB5EKt-36LlLvmcUJrLUCiNBbXgs\",\"oB5EKt4-qKs6soTG5fWf0LmZD26k\",\"oB5EKt0mScn-cgYtl_W0ipO9qcyM\",\"oB5EKt7ogUYtzdMbrzVXXA6icGxE\",\"oB5EKtyGPfqHv9d1ZUTR29OsRKDQ\"]},\"next_openid\":\"oB5EKtyGPfqHv9d1ZUTR29OsRKDQ\"}";
//		new User(str);
//		for(String s : User.getOpenids())
//			System.out.println(s);
	  
  }

@Override
public void onError(HttpRequest paramHttpRequest, Error paramError) {
	// TODO Auto-generated method stub
	
}

@Override
public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
	// TODO Auto-generated method stub
//	User user = (User) paramObject;
//	System.out.println(user.getOpenids().size());
}
}