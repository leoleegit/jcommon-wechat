package org.jcommon.com.wechat.jiaoka.db.dao;

import java.sql.Timestamp;

import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.WechatAgent;

public class WechatAgentDao {
	public boolean insert(WechatAgent bean){
		String sql = "INSERT INTO wechat_agent (agent_id,openid,remark,update_time) VALUES (?,?,?,?)";
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public boolean updatePresence(String presence, Timestamp update_time, String openid){
		String sql = "update wechat_agent set presence=?, update_time=? where openid=?";
		WechatAgent bean = new WechatAgent();
		bean.setPresence(presence);
		bean.setUpdate_time(update_time);
		bean.setOpenid(openid);
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public boolean updatePresence(String presence, String chating, Timestamp update_time, String openid){
		String sql = "update wechat_agent set presence=?, chating=?, update_time=? where openid=?";
		WechatAgent bean = new WechatAgent();
		bean.setPresence(presence);
		bean.setChating(chating);
		bean.setUpdate_time(update_time);
		bean.setOpenid(openid);
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public boolean updateRemark(String remark, Timestamp update_time, String openid){
		String sql = "update wechat_agent set remark=?, update_time=? where openid=?";
		WechatAgent bean = new WechatAgent();
		bean.setRemark(remark);
		bean.setUpdate_time(update_time);
		bean.setOpenid(openid);
		return DbProviderFaceory.createDbProvider().insert(sql, bean);
	}
	
	public boolean updateOpenid(String agent_id, Timestamp update_time, String openid){
		String sql = "update wechat_agent set openid=?, update_time=? where agent_id=?";
		WechatAgent bean = new WechatAgent();
		bean.setAgent_id(agent_id);
		bean.setUpdate_time(update_time);
		bean.setOpenid(openid);
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
	
	public WechatAgent searchAvailableAgent(String presence){
		WechatAgent bean = new WechatAgent();
		bean.setPresence(presence);
		String sql = "select * from wechat_agent where presence=? and isdelete=? order by update_time desc";
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
