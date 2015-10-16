package org.jcommon.com.wechat.jiaoka.db.dao;

import java.sql.Timestamp;
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
	
	public SearchResponse search(String openid, String type, Timestamp from, Timestamp to, int index, int number){
		long start = index * number;
		StringBuilder sql  = new StringBuilder("FROM wechat_auditlog where 1=1");
		List<Object>  pars = new ArrayList<Object>();
		if(openid!=null){
			sql.append(" and openid=?");
			pars.add(openid);
		}
		if(type!=null){
			sql.append(" and type like ?");
			pars.add("%"+type+"%");
		}
		
		if(from!=null && to!=null){
			sql.append(" and (create_time between ? and ?)");
			pars.add(from);
			pars.add(to);
		}
		
		sql.append(" and isdelete=?");
		pars.add(0);
		
		String sql_count = "SELECT count(*) "+sql.toString();
	    long count = DbProviderFaceory.createDbProvider().selectCount(sql_count, pars.toArray());
		if(count==-1){
			return null;
		}
		sql.append(" Order By create_time Desc");
		sql.append("  limit ?,?");
		pars.add(start);
		pars.add(number);
		
		
		String sql_result = "SELECT * "+sql.toString();
		List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql_result, WeChatAuditLog.class, pars.toArray());
		List<WeChatAuditLog> cases     = new ArrayList<WeChatAuditLog>();
		if(results!=null){
			for(Object obj : results){
				cases.add((WeChatAuditLog)obj);
			}
		}
		return new SearchResponse(count, cases);
	}
}
