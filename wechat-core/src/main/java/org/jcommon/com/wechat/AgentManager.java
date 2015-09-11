package org.jcommon.com.wechat;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Agent;
import org.jcommon.com.wechat.data.Error;

public class AgentManager extends ResponseHandler{
	private Logger logger = Logger.getLogger(getClass());
	private WechatSession session;
	
	public AgentManager(WechatSession session){
		this.session = session;
	}

	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		if(paramHttpRequest.getAttibute(paramHttpRequest)!=null){
			AgentManagerListener listener = (AgentManagerListener) paramHttpRequest.getAttibute(paramHttpRequest);
			listener.onError(paramError);
		}	
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		if(paramObject instanceof Agent){
			Agent agent = (Agent) paramObject;
			logger.info(agent.getKf_list().size());
			if(paramHttpRequest.getAttibute(paramHttpRequest)!=null){
				AgentManagerListener listener = (AgentManagerListener) paramHttpRequest.getAttibute(paramHttpRequest);
				listener.onAgents(agent.getKf_list());
			}	
		}
	}
	
	public void getAgent(AgentManagerListener listener){
		HttpRequest request = RequestFactory.getAgentRequest(this, session.getApp().getAccess_token());
		if(listener!=null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, Agent.class);
		logger.info(request.getUrl());
		session.execute(request);
	}
	
	public void addAgent(Agent agent, RequestCallback callback){
		HttpRequest request = RequestFactory.addAgentRequest(callback, session.getApp().getAccess_token(),agent.toJson());
		logger.info(request.getUrl());
		session.execute(request);
	}
	
	public void updateAgent(Agent agent, RequestCallback callback){
		HttpRequest request = RequestFactory.updateAgentRequest(callback, session.getApp().getAccess_token(),agent.toJson());
		logger.info(request.getUrl());
		session.execute(request);
	}
	
	public void delAgent(Agent agent, RequestCallback callback){
		HttpRequest request = RequestFactory.delAgentRequest(callback, session.getApp().getAccess_token(),agent.toJson());
		logger.info(request.getUrl());
		session.execute(request);
	}
	
	public void uploadAgentHeadImg(Agent agent, RequestCallback callback){
		HttpRequest request = RequestFactory.uploadHeadImgRequest(callback, session.getApp().getAccess_token(),agent.getMedia(),agent.getKf_account());
		logger.info(request.getUrl());
		session.execute(request);
	}
}
