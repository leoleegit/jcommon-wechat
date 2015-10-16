package org.jcommon.com.wechat.jiaoka.db.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.db.bean.SearchResponse;

public class CaseDao {
	
	public boolean insert(Case bean){
		String sql = "INSERT INTO wechat_case (case_id,status,note,handle_agent,jiaoka_type,phone_number,card_number" +
				",location_give,location_get,openid,nickname,create_time,update_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public boolean updateCase(String status, String handle_agent, String note, String case_id, Timestamp update_time){
		String sql = "update wechat_case set status=?, handle_agent=?, note=?, update_time=? where case_id=?";
		Case bean = new Case();
		bean.setStatus(status);
		bean.setHandle_agent(handle_agent);
		bean.setNote(note);
		bean.setUpdate_time(update_time);
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public SearchResponse searchCase(String status, String nickname, String phone_number, String addr, Timestamp from, Timestamp to, int index, int number){
		if(number==0)
			number = 20;
		
		long start = index * number;
		StringBuilder sql  = new StringBuilder("FROM wechat_case where 1=1");
		List<Object>  pars = new ArrayList<Object>();
		
		if(status!=null){
			sql.append(" and status=?");
			pars.add(status);
		}
		if(nickname!=null){
			sql.append(" and nickname like ?");
			pars.add("%"+nickname+"%");
		}
		if(phone_number!=null){
			sql.append(" and phone_number like ?");
			pars.add("%"+phone_number+"%");
		}
		
		if(addr!=null){
			sql.append(" and (location_give like ? or location_get like ?)");
			pars.add("%"+addr+"%");
			pars.add("%"+addr+"%");
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
		List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql_result, Case.class, pars.toArray());
		List<Case> cases     = new ArrayList<Case>();
		if(results!=null){
			for(Object obj : results){
				cases.add((Case)obj);
			}
		}
		return new SearchResponse(count, cases);
	}
}
