package org.jcommon.com.wechat.router.test;

import java.io.IOException;

import org.jcommon.com.wechat.router.TokenHttpClient;

public class TokenHttpClientTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new TokenHttpClient("gh_f49bb9a333b3","spotlight-wechat","http://192.168.2.104:8080/wechat-router/token").go();
		
		System.in.read();
	}

}
