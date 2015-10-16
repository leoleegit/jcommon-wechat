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
		StringBuilder sql  = new StringBuilder("FROM wechat_user where 1=1");
		List<Object>   pars = new ArrayList<Object>();
		
		if(nickname!=null){
			sql.append(" and nickname like ?");
			pars.add("%"+nickname+"%");
		}
		if(phone_number!=null){
			sql.append(" and phone_number like ?");
			pars.add("%"+phone_number+"%");
		}
		sql.append(" and isdelete=?");
		pars.add(0);
		
		String sql_count = "SELECT count(*) "+sql.toString();
	    long count = DbProviderFaceory.createDbProvider().selectCount(sql_count, pars.toArray());
		if(count==-1){
			return null;
		}
		sql.append(" Order By subscribe_time Desc");
		sql.append("  limit ?,?");
		pars.add(start);
		pars.add(number);
		
		
		String sql_result = "SELECT * "+sql.toString();
		List<Object> results = DbProviderFaceory.createDbProvider().selectArray(sql_result, WeChatUser.class, pars.toArray());
		List<WeChatUser> cases     = new ArrayList<WeChatUser>();
		if(results!=null){
			for(Object obj : results){
				cases.add((WeChatUser)obj);
			}
		}
		return new SearchResponse(count, cases);
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
