package org.jcommon.com.wechat.test;

import java.security.NoSuchAlgorithmException;

import org.jcommon.com.wechat.utils.MD5;

public class MD5Test {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		System.out.println(MD5.getMD5("Protel117".getBytes()));
	}

}
