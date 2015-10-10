package org.jcommon.com.wechat.jiaoka.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.SearchResponse;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatAuditLog;

public class WeChatAuditLogDao {
	public boolean insert(WeChatAuditLog bean){
		String sql = "INSERT INTO wechat_auditlog (type,openid,logstr,create_time) VALUES (?,?,?,?)";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public SearchResponse search(String openid, int index, int number){
		long start = index * number;
		if(openid!=null){
			String sql = "SELECT count(*) FROM wechat_auditlog where openid=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,openid,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_auditlog where openid=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, WeChatAuditLog.class,openid,0,start,number);
			List<WeChatAuditLog> cases     = new ArrayList<WeChatAuditLog>();
			if(results!=null){
				for(Object obj : results){
					cases.add((WeChatAuditLog)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else{
			String sql = "SELECT count(*) FROM wechat_auditlog where isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_auditlog where isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, WeChatAuditLog.class,0,start,number);
			List<WeChatAuditLog> cases     = new ArrayList<WeChatAuditLog>();
			if(results!=null){
				for(Object obj : results){
					cases.add((WeChatAuditLog)obj);
				}
			}
			return new SearchResponse(count, cases);
		}
	}
}
