package org.jcommon.com.wechat;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.OutMessage;

public class AgentManager extends ResponseHandler{
	private Logger logger = Logger.getLogger(this.getClass());

	private WechatSession session;
	private App app;
	    
    public AgentManager(WechatSession session){
    	this.setSession(session);
    	this.setApp(session.getApp());
    }
    
    public HttpRequest addAgent(RequestCallback callback, OutMessage msg) {
    	HttpRequest msg_re = RequestFactory.createAddAgentReqeust(callback, app.getAccess_token(), msg.toJson());
        session.execute(msg_re);
        return msg_re;
    }
    
    public HttpRequest delAgent(RequestCallback callback, OutMessage msg) {
    	HttpRequest msg_re = RequestFactory.createDelAgentReqeust(callback, app.getAccess_token(), msg.toJson());
        session.execute(msg_re);
        return msg_re;
    }
    
    public HttpRequest getAgents(RequestCallback callback) {
    	HttpRequest msg_re = RequestFactory.createGetAgentListReqeust(this, app.getAccess_token());
        session.execute(msg_re);
        return msg_re;
    }
	    
	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		
	}

	public void setSession(WechatSession session) {
		this.session = session;
	}

	public WechatSession getSession() {
		return session;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public App getApp() {
		return app;
	}
}
