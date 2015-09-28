package org.jcommon.com.wechat.jiaoka.db.dao;

import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatUser;

public class WeChatUserDao {
	
	public boolean insert(WeChatUser bean){
		String sql = "INSERT INTO wechatuser (subscribe,openid,nickname,sex,language,city," +
				"province,country,headimgurl,subscribe_time,unionid,remark,groupid,create_time)VALUES" +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public boolean update(WeChatUser bean){
		String sql = "update wechatuser set subscribe=? nickname=? sex=? language=? city=? " +
				"province=? country=? headimgurl=? subscribe_time=? unionid=? remark=? groupid=? where " +
				"openid=?";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
}
