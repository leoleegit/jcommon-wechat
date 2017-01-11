package org.jcommon.com.wechat.test;

import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestFactory;
import org.jcommon.com.wechat.data.App;


public class AccessTokenTest extends TestBase implements HttpListener{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String appid="wx742941360129cd17";
		String secret="37492ad273076440c0f123716865e1da";
		String Token="spotlight-wechat";
		App app = new App(appid,secret,Token);
		RequestFactory.accessTokenReqeust(new AccessTokenTest(),app).run();
		
	}

	@Override
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		System.out.println(sResult);
	}

	@Override
	public void onFailure(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		System.out.println(sResult);
	}

	@Override
	public void onTimeout(HttpRequest reqeust) {
		// TODO Auto-generated method stub
		System.out.println("onTimeout");
	}

	@Override
	public void onException(HttpRequest reqeust, Exception e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
	}

}
