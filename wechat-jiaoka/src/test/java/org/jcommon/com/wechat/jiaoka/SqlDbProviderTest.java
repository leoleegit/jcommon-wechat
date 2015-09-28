package org.jcommon.com.wechat.jiaoka;

import org.jcommon.com.wechat.jiaoka.db.sql.SqlDbProvider;

public class SqlDbProviderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql  = "select * from User_in_role nt where nt.UserID=? ";
	
		for(String s : SqlDbProvider.getParameters(sql))
			System.out.println(s);
	}

}
