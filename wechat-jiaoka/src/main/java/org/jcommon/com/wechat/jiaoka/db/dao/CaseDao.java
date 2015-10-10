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
	
	public SearchResponse searchCase(String status, String nickname, String phone_number, int index, int number){
		if(number==0)
			number = 20;
		
		long start = index * number;
		if(status!=null && nickname!=null && phone_number!=null){
			String sql = "SELECT count(*) FROM wechat_case where status=? and nickname=? and phone_number=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql, status,nickname,phone_number,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_case where status=? and nickname=? and phone_number=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, Case.class, status,nickname,phone_number,0,start,number);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else if(status!=null && nickname!=null){
			String sql = "SELECT count(*) FROM wechat_case where status=? and nickname=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql, status,nickname,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_case where status=? and nickname=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, Case.class, status,nickname,0,start,number);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else if(status!=null && phone_number!=null){
			String sql = "SELECT count(*) FROM wechat_case where status=? and phone_number=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql, status,phone_number,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_case where status=? and phone_number=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, Case.class, status,phone_number,0,start,number);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else if(nickname!=null && phone_number!=null){
			String sql = "SELECT count(*) FROM wechat_case where nickname=? and phone_number=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,nickname,phone_number,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_case where nickname=? and phone_number=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, Case.class,nickname,phone_number,0,start,number);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else if(status!=null){
			String sql = "SELECT count(*) FROM wechat_case where status=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql, status,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_case where status=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, Case.class, status,0,start,number);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else if(nickname!=null){
			String sql = "SELECT count(*) FROM wechat_case where nickname=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,nickname,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_case where nickname=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, Case.class,nickname,0,start,number);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else if(phone_number!=null){
			String sql = "SELECT count(*) FROM wechat_case where phone_number=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,phone_number,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_case where phone_number=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, Case.class,phone_number,0,start,number);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else{
			String sql = "SELECT count(*) FROM wechat_case where isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * FROM wechat_case where isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, Case.class,0,start,number);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return new SearchResponse(count, cases);
		}
	}
}
