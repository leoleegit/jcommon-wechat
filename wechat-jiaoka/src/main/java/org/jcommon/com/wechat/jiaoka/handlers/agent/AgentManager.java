package org.jcommon.com.wechat.jiaoka.handlers.agent;

import java.sql.Timestamp;

import org.jcommon.com.wechat.jiaoka.db.bean.WechatAgent;
import org.jcommon.com.wechat.jiaoka.db.dao.WechatAgentDao;
import org.jcommon.com.wechat.jiaoka.utils.Presence;

public class AgentManager {
	private WechatAgentDao dao = new WechatAgentDao();
	private static AgentManager instance;
	
	public static AgentManager instance(){
		if(instance==null)
			instance = new AgentManager();
		return instance;
	}
	
	
	public WechatAgent getAvailableAgent(String customer){
		WechatAgent agent = dao.searchAvailableAgent(Presence.Available.name);
		if(agent!=null){
			dao.updatePresence(Presence.Busy.name, customer, new Timestamp(System.currentTimeMillis()), agent.getOpenid());
			return agent;
		}
		return null;
	}
	
	public WechatAgent searchAgent(String openid){
		return dao.searchAgentByOpenid(openid);
	}
	
	public void releaseAgent(String openid){
		if(openid!=null){
			dao.updatePresence(Presence.Available.name, new Timestamp(System.currentTimeMillis()), openid);
		}
	}
	
	public void logoutAgent(String openid){
		if(openid!=null){
			dao.updatePresence(Presence.UnAvailable.name, new Timestamp(System.currentTimeMillis()), openid);
		}
	}
}
