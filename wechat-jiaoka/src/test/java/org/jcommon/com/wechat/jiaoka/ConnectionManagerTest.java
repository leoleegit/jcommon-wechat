package org.jcommon.com.wechat.jiaoka;

import java.sql.Connection;

import org.jcommon.com.wechat.jiaoka.db.sql.ConnectionManager;
import org.jcommon.com.wechat.test.TestBase;

public class ConnectionManagerTest extends TestBase{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = ConnectionManager.instance().getConnection();
		System.out.println(connection);
		connection.close();
		
	}

}
