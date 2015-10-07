package org.jcommon.com.wechat.jiaoka.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatUser;
import org.jcommon.com.wechat.jiaoka.db.sql.ConnectionManager;
import org.jcommon.com.wechat.jiaoka.db.sql.SqlDbProvider;

public class WeChatUserDao {
	private static Logger logger = Logger.getLogger(SqlDbProvider.class);
	
	public boolean insert(WeChatUser bean){
		String sql = "insert into wechat_user (subscribe,openid,nickname,sex,language,city," +
				"province,country,headimgurl,subscribe_time,unionid,remark,groupid,create_time)VALUES" +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if(searchUserIDByOpenid(bean.getOpenid())==-1)
			return DbProviderFaceory.createDbProvider().insert(sql, bean);
		else
			return update(bean);
	}
	
	public boolean update(WeChatUser bean){
		String sql = "update wechat_user set subscribe=?, nickname=?, sex=?, language=?, city=?, " +
				"province=?, country=?, headimgurl=?, subscribe_time=?, unionid=?, remark=?, groupid=? where " +
				"openid=?";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public WeChatUser searchUserByOpenid(String openid){
		String sql = "select * from wechat_user where openid=? and isdelete=?";
		WeChatUser bean = new WeChatUser();
		bean.setOpenid(openid);
		Object result = DbProviderFaceory.createDbProvider().select(sql, WeChatUser.class, bean);
		if(result!=null)
			return (WeChatUser)result;
		return null;
	}
	
	public List<WeChatUser> searchAllUser(int next, int number){
		String sql = "select * from wechat_user where id>? and isdelete=? limit ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if(number==0)
			number = 20;
		
		try {
			conn = ConnectionManager.instance().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, next);
			ps.setInt(2, 0);
			ps.setInt(3, number);
			
			rs = ps.executeQuery();
			List<Object> results       = DbProviderFaceory.createDbProvider().getResult(sql, WeChatUser.class, rs);
			List<WeChatUser> users     = new ArrayList<WeChatUser>();
			if(results!=null){
				for(Object obj : results){
					users.add((WeChatUser)obj);
				}
			}
			return users;
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
	
	public int searchUserIDByOpenid(String openid){
		String sql = "select wechat_user_id from wechat_user where openid=? and isdelete=?";
		WeChatUser bean = new WeChatUser();
		bean.setOpenid(openid);
		Object result = DbProviderFaceory.createDbProvider().select(sql, WeChatUser.class, bean);
		if(result!=null)
			return ((WeChatUser)result).getWechat_user_id();
		return -1;
	}
}
