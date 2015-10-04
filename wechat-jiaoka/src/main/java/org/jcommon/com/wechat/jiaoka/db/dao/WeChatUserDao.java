package org.jcommon.com.wechat.jiaoka.db.dao;

import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
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
	
	public WeChatUser searchUserByOpenid(String openid){
		String sql = "select * from wechat_user where openid=? and isdelete=?";
		WeChatUser bean = new WeChatUser();
		bean.setOpenid(openid);
		Object result = DbProviderFaceory.createDbProvider().select(sql, WeChatUser.class, bean);
		if(result!=null)
			return (WeChatUser)result;
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
