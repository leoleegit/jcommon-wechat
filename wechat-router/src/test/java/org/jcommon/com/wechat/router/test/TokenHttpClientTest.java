package org.jcommon.com.wechat.router.test;

import java.io.IOException;

import org.jcommon.com.wechat.router.client.TokenHttpClient;

public class TokenHttpClientTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new TokenHttpClient("gh_f49bb9a333b3","spotlight-wechat","http://127.0.0.1/wechat-router/token").go();
		
		System.in.read();
	}

}
