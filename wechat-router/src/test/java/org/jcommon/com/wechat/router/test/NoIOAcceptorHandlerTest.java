package org.jcommon.com.wechat.router.test;
import java.io.IOException;

import org.jcommon.com.wechat.router.NoIOAcceptorHandler;


public class NoIOAcceptorHandlerTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new NoIOAcceptorHandler().start();
		System.in.read();
	}

}
