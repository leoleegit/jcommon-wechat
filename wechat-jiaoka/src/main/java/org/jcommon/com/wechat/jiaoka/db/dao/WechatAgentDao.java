package org.jcommon.com.wechat.jiaoka.db.dao;

import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.WechatAgent;

public class WechatAgentDao {
	public boolean insert(WechatAgent bean){
		String sql = "INSERT INTO wechat_agent (agent_id,openid,remark,update_time) VALUES (?,?,?,?)";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public boolean updateRemark(WechatAgent bean){
		String sql = "update wechat_agent set remark=?, update_time=? where openid=?";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public boolean updateOpenid(WechatAgent bean){
		String sql = "update wechat_agent set openid=?, update_time=? where agent_id=?";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public WechatAgent searchAgentByOpenid(String openid){
		WechatAgent bean = new WechatAgent();
		bean.setOpenid(openid);
		String sql = "select * from wechat_agent where openid=? and isdelete=?";
		Object result = DbProviderFaceory.createDbProvider().select(sql, WechatAgent.class, bean);
		if(result!=null)
			return (WechatAgent)result;
		return null;
	}
	
	public WechatAgent searchAgentByAgentid(String agent_id){
		WechatAgent bean = new WechatAgent();
		bean.setAgent_id(agent_id);
		String sql = "select * from wechat_agent where agent_id=? and isdelete=?";
		Object result = DbProviderFaceory.createDbProvider().select(sql, WechatAgent.class, bean);
		if(result!=null)
			return (WechatAgent)result;
		return null;
	}
}
