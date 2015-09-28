package org.jcommon.com.wechat.jiaoka;

import org.jcommon.com.wechat.jiaoka.db.sql.SqlDbProvider;

public class SqlDbProviderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO 'jiaoka'.'wechatauditlog'('type','logstr','create_time')VALUES(?,?,?);";
		String str = sql.substring(sql.indexOf("(")+1, sql.indexOf(")"));
		System.out.println(sql.substring(0, 6));
		System.out.println(str);
		for(String s : SqlDbProvider.getParameters(sql))
			System.out.println(s);
	}

}
