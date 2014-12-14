package org.jcommon.com.wechat.test;

import java.io.File;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.Callback;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.utils.MediaType;

public class BroadcastTest extends Callback implements RequestCallback{
    Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		 App app = new App("wx9b84c7e1c33a29c7", "9cb43ce5e72ea14fefe77e1ae7760fbd", "spotlight-wechat");
	     WechatSession session = new WechatSession("gh_ed5dd6bc3c51", app, null);
	     session.startup();
	     Thread.sleep(10000);
	     Articles article = new Articles("Title", "Hello World!","description","http://hbase.apache.org/","author");
	     article.setMedia(new File("C:/Users/Administrator/Desktop/cipango-distribution-2.0.0/media/12.jpg"));
	     article.setType(MediaType.thumb.toString());
	    // session.sendBroadcast(new BroadcastTest(), article);
	      
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
