package org.jcommon.com.wechat.jiaoka.db;

import org.jcommon.com.wechat.jiaoka.db.sql.SqlDbProvider;

public class DbProviderFaceory {
	public static DbProvider createDbProvider(){
		return new SqlDbProvider();
	}
}
