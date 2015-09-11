package org.jcommon.com.wechat.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.data.Mpnews;
import org.jcommon.com.wechat.data.OpenID;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.filter.UserFilter;
import org.jcommon.com.wechat.servlet.Callback;
import org.jcommon.com.wechat.utils.MediaType;

public class BroadcastTest extends TestBase implements RequestCallback{
    Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		 App app = new App("wx9b84c7e1c33a29c7", "9cb43ce5e72ea14fefe77e1ae7760fbd", "spotlight-wechat");
//	     WechatSession session = new WechatSession("gh_ed5dd6bc3c51", app, null);
//	     session.startup();
//	     Thread.sleep(10000);
		 App app = new App("wx742941360129cd17", "37492ad273076440c0f123716865e1da", "spotlight-wechat");
	     WechatSession session = new WechatSession("gh_f49bb9a333b3", app, null);
	     session.startup();
	     Thread.sleep(10000);
	     
	     List<OpenID> openids = session.getUserManager().getAllUsers();
	     //session.broadcastText(new BroadcastTest(), new Text("hello broadcast text!"), new UserFilter(openids));
	     List<Articles> articles = new ArrayList<Articles>();
	     Articles article = new Articles("Title", "Hello World!","description","http://hbase.apache.org/","protel");
	     article.setMedia(new File("C:/Users/Administrator/Desktop/cipango-distribution-2.0.0/media/12.jpg"));
	     article.setType(MediaType.thumb.toString());
	     articles.add(article);
	     
	     article = new Articles("Title2", "Hello World2!","description2","http://hbase.apache.org/","protel");
	     article.setMedia(new File("C:/Users/Administrator/Desktop/cipango-distribution-2.0.0/media/12.jpg"));
	     article.setType(MediaType.thumb.toString());
	     articles.add(article);
	    
	     
	     Mpnews mpnews = new Mpnews();
	     mpnews.setArticles(articles);
	     session.broadcastNews(new BroadcastTest(), mpnews, new UserFilter(openids));
	}

	@Override
	public void onException(HttpRequest arg0, Exception arg1) {
		// TODO Auto-generated method stub
		arg1.printStackTrace();
	}

	@Override
	public void onFailure(HttpRequest arg0, StringBuilder arg1) {
		// TODO Auto-generated method stub
		logger.error(arg1);
	}

	@Override
	public void onSuccessful(HttpRequest arg0, StringBuilder arg1) {
		// TODO Auto-generated method stub
		logger.info(arg1);
	}

	@Override
	public void onTimeout(HttpRequest arg0) {
		// TODO Auto-generated method stub
		logger.error(arg0);
	}

}
