package org.jcommon.com.wechat.jiaoka.handlers;

import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.Image;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.jiaoka.Handler;
import org.jcommon.com.wechat.jiaoka.HandlerManager;
import org.jcommon.com.wechat.jiaoka.db.bean.WechatAgent;
import org.jcommon.com.wechat.jiaoka.handlers.agent.AgentManager;
import org.jcommon.com.wechat.utils.EventType;
import org.jcommon.com.wechat.utils.MsgType;

public class Agent extends Handler implements RequestCallback{
	private Logger logger = Logger.getLogger(getClass());
	
	private static final String name = "Agent";
	public static final String AgentKey1 = "Agent_001";
	
	public  static final String AgentLogin    = "---客服---";
	public  static final String AgentRemark   = "---客服昵称---";
	public  static final String AgentChatEnd  = "---会话结束---";
	
	private static final String AGENT    = "agent";
	private static final String CUSTOMER = "customer";
	
	private String role = null;
	
	private static final String greeting_to_agent    = " 正请求客服服务,请按人工客服连接对话";
	private static final String login_to_agent       = " 客服登录成功。";
	private static final String greeting_to_customer = " 正在为您服务，请问有什么可以你帮到您？";
	private static final String bust_to_customer     = " 非常抱歉告诉您，目前咨询用户过多，客服会尽快回复您。";
	
	private WechatAgent agent;
	private String   customer;
	
	public Agent(HandlerManager manager, WechatSession session) {
		super(manager, session);
		// TODO Auto-generated constructor stub
	}

	synchronized public void setExpiry(){
		closeTimer();
		task = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				manager.dutyEnd(Agent.this);
				if(isAgent()){
					AgentManager.instance().releaseAgent(manager.getName());
					String msg = "会话结束";
					Text text  = new Text(msg);
					String touser = manager.getName();
					session.getMsg_manager().sendText(touser, text, Agent.this);
				}else{
					String msg = "由于较长时间没有收到你的信息，会话将自动结束。如有需要请再次点击人工服务。谢谢。";
					Text text  = new Text(msg);
					String touser = manager.getName();
					session.getMsg_manager().sendText(touser, text, Agent.this);
				}
			}
			
		};
		timer = org.jcommon.com.util.thread.TimerTaskManger.instance().schedule("Handler-"+name(), task, delay);
	}

	@Override
	public boolean mapJob(Event event, InMessage message) {
		// TODO Auto-generated method stub
		if(event!=null){
			EventType type = EventType.getType(event.getEvent());
			if(type!=null && EventType.CLICK == type && AgentKey1.equals(event.getEventKey())){
				setExpiry();
				this.agent = AgentManager.instance().searchAgent(event.getFromUserName());
				User user         = event.getFrom();
				String reply      = null;
				if(agent!=null){
					this.setRole(AGENT);
					if(agent.getChating()==null || "".equals(agent.getChating().trim())){
						AgentManager.instance().releaseAgent(manager.getName());
						reply = login_to_agent;
					}
					else{
						this.customer = agent.getChating();
						reply = "会话连接成功，请直接输入进行客服服务。";
					}
				}else{ 
					this.setRole(CUSTOMER);
					this.agent = AgentManager.instance().getAvailableAgent(manager.getName());
					if(this.agent!=null){
						reply = this.agent.getRemark() +  greeting_to_customer;
						Text text  = new Text( user.getNickname() + greeting_to_agent);
						session.getMsg_manager().sendText(this.agent.getOpenid(), text, this);
					}
					else
						reply = bust_to_customer;			
				}
				if(reply!=null){
					Text text  = new Text(reply);
					String touser = manager.getName();
					session.getMsg_manager().sendText(touser, text, this);
				}
				return true;
			}
		}
		return false;
	}

	public boolean isAgent(){
		return AGENT.equals(this.getRole());
	}
	
	@Override
	public boolean hanlderEvent(Event event) {
		// TODO Auto-generated method stub
		return mapJob(event,null);
	}
	

	@Override
	public boolean hanlderMessage(InMessage message) {
		// TODO Auto-generated method stub
		setExpiry();
		String openid = null;
		String remark = "";
		if(isAgent()){
			if(customer!=null){
				openid = customer;
				remark = agent!=null?agent.getRemark():message.getFrom().getNickname();
				if(message.getMessageType() == MsgType.text){
					String txt = message.getContent();
					if(AgentChatEnd.equalsIgnoreCase(txt)){
						openid = null;
						AgentManager.instance().releaseAgent(message.getFromUserName());
						this.manager.dutyEnd(this);
					}
				}
			}else{
				this.manager.dutyEnd(this);
			}
		}else{
			if(agent!=null){
				openid = agent.getOpenid();
				remark = message.getFrom().getNickname();
			}else{
				this.manager.dutyEnd(this);
			}
		}
		if(openid!=null){
			MsgType type = message.getMessageType();
			switch(type){
			    case text : {
			    	Text text  = new Text(message.getContent()+"\n--------"+remark);
					session.getMsg_manager().sendText(openid, text, this);
			    	break;
			    }
			    case image: 
			    case music:  
			    case news:  
			    case voice:  
			    case video:  {
			    	Image image = new Image();
			    	image.setMedia_id(message.getMediaId());
					session.getMsg_manager().sendImage(openid, image, this);
			    }
			}
		}
		return false;
	}
	
	@Override
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		logger.info(sResult);
	}

	@Override
	public void onFailure(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		logger.info(sResult);
	}

	@Override
	public void onTimeout(HttpRequest reqeust) {
		// TODO Auto-generated method stub
		logger.info("timeout");
	}

	@Override
	public void onException(HttpRequest reqeust, Exception e) {
		// TODO Auto-generated method stub
		logger.error("", e);
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
