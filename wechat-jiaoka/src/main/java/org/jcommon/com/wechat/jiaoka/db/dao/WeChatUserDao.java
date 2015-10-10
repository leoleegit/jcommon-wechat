package org.jcommon.com.wechat.jiaoka.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.SearchResponse;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatUser;

public class WeChatUserDao {
	
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
	
	public boolean updatePhoneNumber(String phone_number,String openid){
		String sql = "update wechat_user set phone_number=? where openid=?";
		WeChatUser bean = new WeChatUser();
		bean.setOpenid(openid);
		bean.setPhone_number(phone_number);
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
	
	public SearchResponse searchAllUser(String phone_number, String nickname, int index, int number){
		if(number==0)
			number = 20;
		
		long start = index * number;
		if(phone_number!=null && nickname!=null){
			String sql = "SELECT count(*) from wechat_user where nickname=? and phone_number=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,nickname,phone_number,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * from wechat_user where nickname=? and phone_number=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, WeChatUser.class,nickname,phone_number,0,start,number);
			List<WeChatUser> cases     = new ArrayList<WeChatUser>();
			if(results!=null){
				for(Object obj : results){
					cases.add((WeChatUser)obj);
				}
			}
			return new SearchResponse(count, cases);	
		}else if(phone_number!=null){
			String sql = "SELECT count(*) from wechat_user where phone_number=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,phone_number,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * from wechat_user where phone_number=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, WeChatUser.class,phone_number,0,start,number);
			List<WeChatUser> cases     = new ArrayList<WeChatUser>();
			if(results!=null){
				for(Object obj : results){
					cases.add((WeChatUser)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else if(nickname!=null){
			String sql = "SELECT count(*) from wechat_user where nickname=? and isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,nickname,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * from wechat_user where nickname=? and isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, WeChatUser.class,nickname,0,start,number);
			List<WeChatUser> cases     = new ArrayList<WeChatUser>();
			if(results!=null){
				for(Object obj : results){
					cases.add((WeChatUser)obj);
				}
			}
			return new SearchResponse(count, cases);
		}else{
			String sql = "SELECT count(*) from wechat_user where isdelete=?";
			long count = DbProviderFaceory.createDbProvider().selectCount(sql,0);
			if(count==-1){
				return null;
			}
			sql = "SELECT * from wechat_user where isdelete=? limit ?,?";		
			List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql, WeChatUser.class,0,start,number);
			List<WeChatUser> cases     = new ArrayList<WeChatUser>();
			if(results!=null){
				for(Object obj : results){
					cases.add((WeChatUser)obj);
				}
			}
			return new SearchResponse(count, cases);
		}
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
