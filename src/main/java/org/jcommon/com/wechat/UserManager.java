package org.jcommon.com.wechat;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.system.SystemListener;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Group;

public class UserManager extends ResponseHandler implements SystemListener{
	private Logger logger = Logger.getLogger(this.getClass());
	   
	private WechatSession session;
	private List<Group> groups;
	private App app;
	    
	private Timer load_groups;
	
	public UserManager(WechatSession session){
	    this.setSession(session);
	    this.app = session.getApp();
	}

	@Override
	public void startup(){
		loadGroups();
	}
	
	@Override
	public void shutdown(){
		if(groups!=null){
			groups.clear();
			groups = null;
		}
		if(load_groups!=null){
			try{
				load_groups.cancel();
				load_groups = null;
			}catch(Exception e){}
		}
	}
	
	private void loadGroups(){
		load_groups = null;
		HttpRequest request = RequestFactory.createGetGroupsReqeust(this,this.app.getAccess_token());
		request.setAttribute(WechatSession.RequestAction, "loadGroups");
		ThreadManager.instance().execute(request);
	}
	
	public void setSession(WechatSession session) {
		this.session = session;
	}

	public WechatSession getSession() {
		return session;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	@Override
	public void onError(HttpRequest request, Error paramError) {
		// TODO Auto-generated method stub
		logger.info(paramError.toJson());
		String request_action = (String) request.getAttibute(WechatSession.RequestAction);
		if("loadGroups".equals(request_action)){
			load_groups = org.jcommon.com.util.thread.TimerTaskManger.instance().schedule("loadGroups", 
					new TimerTask(){
						public void run(){
							UserManager.this.loadGroups();
						}
			}, 20000);
		}
	}

	@Override
	public void onOk(HttpRequest request, Object paramObject) {
		// TODO Auto-generated method stub
		logger.info(paramObject);
		String request_action = (String) request.getAttibute(WechatSession.RequestAction);
		if("loadGroups".equals(request_action)){
			groups = Group.getGroups(paramObject.toString());
		}
	}
}
