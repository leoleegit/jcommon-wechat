package org.jcommon.com.wechat.jiaoka.db.dao;

import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatAuditLog;

public class WeChatAuditLogDao {
	public boolean insert(WeChatAuditLog bean){
		String sql = "INSERT INTO wechatauditlog(type,logstr,create_time) VALUES (?,?,?)";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
}
