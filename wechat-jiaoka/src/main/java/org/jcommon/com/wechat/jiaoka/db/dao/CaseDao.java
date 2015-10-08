package org.jcommon.com.wechat.jiaoka.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.db.sql.ConnectionManager;
import org.jcommon.com.wechat.jiaoka.db.sql.SqlDbProvider;

public class CaseDao {
	private static Logger logger = Logger.getLogger(SqlDbProvider.class);
	
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
	
	public List<Case> searchAllCase(String status, String nickname, String phone_number, int next, int number){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if(number==0)
			number = 20;
		
		try {
			conn = ConnectionManager.instance().getConnection();
			conn.setAutoCommit(false);
			String sql = null;
			if(status!=null && nickname!=null && phone_number!=null){
				sql = "SELECT * FROM wechat_case where id>? and status=? and nickname=?  and phone_number=? and isdelete=? limit ?";		
				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, next);
				ps.setString(2, status);
				ps.setString(3, nickname);
				ps.setString(4, phone_number);
				ps.setInt(5, 0);
				ps.setInt(6, number);
			}else if(status!=null && nickname!=null){
				sql = "SELECT * FROM wechat_case where id>? and status=? and nickname=? and isdelete=? limit ?";		
				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, next);
				ps.setString(2, status);
				ps.setString(3, nickname);
				ps.setInt(4, 0);
				ps.setInt(5, number);
			}else if(status!=null && phone_number!=null){
				sql = "SELECT * FROM wechat_case where id>? and status=? and phone_number=? and isdelete=? limit ?";		
				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, next);
				ps.setString(2, status);
				ps.setString(3, phone_number);
				ps.setInt(4, 0);
				ps.setInt(5, number);
			}else if(nickname!=null && phone_number!=null){
				sql = "SELECT * FROM wechat_case where id>? and nickname=?  and phone_number=? and isdelete=? limit ?";		
				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, next);
				ps.setString(2, nickname);
				ps.setString(3, phone_number);
				ps.setInt(4, 0);
				ps.setInt(5, number);
			}else if(status!=null){
				sql = "SELECT * FROM wechat_case where id>? and status=? and isdelete=? limit ?";		
				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, next);
				ps.setString(2, status);
				ps.setInt(3, 0);
				ps.setInt(4, number);
			}else if(nickname!=null){
				sql = "SELECT * FROM wechat_case where id>? and nickname=? and isdelete=? limit ?";		
				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, next);
				ps.setString(2, nickname);
				ps.setInt(3, 0);
				ps.setInt(4, number);
			}else if(phone_number!=null){
				sql = "SELECT * FROM wechat_case where id>? and phone_number=? and isdelete=? limit ?";		
				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, next);
				ps.setString(2, phone_number);
				ps.setInt(3, 0);
				ps.setInt(4, number);
			}else{
				sql = "SELECT * FROM wechat_case where id>? and isdelete=? limit ?";		
				ps = conn.prepareStatement(sql);
				
				ps.setInt(1, next);
				ps.setInt(2, 0);
				ps.setInt(3, number);
			}
			
			
			rs = ps.executeQuery();
			List<Object> results = DbProviderFaceory.createDbProvider().getResult(sql, Case.class, rs);
			List<Case> cases     = new ArrayList<Case>();
			if(results!=null){
				for(Object obj : results){
					cases.add((Case)obj);
				}
			}
			return cases;
		} catch (Exception e) {
			try {
				logger.info("Exception and rollback.");
				if (conn != null)
					conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				logger.error("", e1);
			}
			logger.error("", e);
		} finally {
			ConnectionManager.release(conn, ps, rs);
		}
		return null;
	}
}
