package org.jcommon.com.wechat.jiaoka.handlers;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.jiaoka.Handler;
import org.jcommon.com.wechat.jiaoka.HandlerManager;

public class Agent extends Handler implements RequestCallback{
	private Logger logger = Logger.getLogger(getClass());
	
	private static final String name = "Agent";
	public  static final String AgentLogin    = "---客服---";
	public  static final String AgentRemark   = "---客服昵称---";
	public  static final String AgentChatEnd  = "---会话结束---";
	
	private static final String AGENT    = "agent";
	private static final String CUSTOMER = "customer";
	
	private String role = null;
	
	private static final String agent_msg01 = "请输入验证码:";	
	private static final String agent_error01 = "验证码错误，请重新输入";
	
	public Agent(HandlerManager manager, WechatSession session) {
		super(manager, session);
		// TODO Auto-generated constructor stub
	}


	@Override
	public boolean mapJob(Event event, InMessage message) {
		// TODO Auto-generated method stub
		if(message!=null){
			
		}
		return false;
	}

	@Override
	public boolean hanlderEvent(Event event) {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public boolean hanlderMessage(InMessage message) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onException(HttpRequest arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(HttpRequest arg0, StringBuilder arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccessful(HttpRequest arg0, StringBuilder arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeout(HttpRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return name;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getRole() {
		return role;
	}
}
