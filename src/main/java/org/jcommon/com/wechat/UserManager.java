package org.jcommon.com.wechat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.system.SystemListener;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.OpenID;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.utils.EventType;

public class UserManager extends ResponseHandler implements SystemListener{
	private Logger logger = Logger.getLogger(this.getClass());
	
	private Map<String,User> users = new HashMap<String,User>();
	private static final long expires = 1000 * 60 * 30;
	
	private  List<OpenID> openids;
	private  long total;
	private  long count;
	
	private WechatSession session;
	private List<Group> groups;
	private App app;
	    
	private Timer load_groups;
	private Timer load_users;
	private Timer update_users;
	
	public UserManager(WechatSession session){
	    this.setSession(session);
	    this.app = session.getApp();
	}

	public void onEvent(Event event){
		if(EventType.subscribe == EventType.getType(event.getEvent()) ||
				EventType.unsubscribe == EventType.getType(event.getEvent())){
			 String open_id = event.getFromUserName();
			 if(openids!=null){
				 OpenID id = null;
				 synchronized(openids){
					 for(OpenID o_id : openids){
						 if(o_id.getOpenid().equals(open_id)){
							 id = o_id;
							 break;
						 }
					  }
				 }
				 if(EventType.subscribe == EventType.getType(event.getEvent())){
					 openids.add(id);
				 }else  if(EventType.unsubscribe == EventType.getType(event.getEvent())){
					 openids.remove(id);
				 }
			  }
		}   
	}
	
	@Override
	public void startup(){
		loadGroups();
		loadUsers(null);
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
		if(update_users!=null){
			try{
				update_users.cancel();
				update_users = null;
			}catch(Exception e){}
		}
		if(load_users!=null){
			try{
				load_users.cancel();
				load_users = null;
			}catch(Exception e){}
		}
	}
	
	public void loadUsers(String next_openid){
		HttpRequest request = RequestFactory.createGetUsersReqeust(this, app.getAccess_token(), next_openid);
	    addHandlerObject(request, User.class);
	    request.setAttribute(WechatSession.RequestAction, "loadUsers");
	    request.setAttribute("next_openid", next_openid);
	    session.execute(request);
	}
	
	public HttpRequest getUser(InMessage message){
		User user = users.get(message.getFromUserName());
		if(user!=null){
			message.setFrom(user);
			session.onMessage(message);
			return null;
		}
		HttpRequest request = RequestFactory.createUserInfoRequest(this, app.getAccess_token(), message.getFromUserName(), null);
    	request.setHandler(message);
    	addHandlerObject(request,User.class);
    	session.execute(request);
    	return request;
	}
	
	private void loadGroups(){
		load_groups = null;
		HttpRequest request = RequestFactory.createGetGroupsReqeust(this,this.app.getAccess_token());
		request.setAttribute(WechatSession.RequestAction, "loadGroups");
		session.execute(request);
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
	
	public String getGroupsStr(){
		if(groups==null)
			return null;
		return Group.getGroupsStr(groups);
	}

	@Override
	public void onError(final HttpRequest request, Error paramError) {
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
		}else if("loadUsers".equals(request_action)){
			load_users = org.jcommon.com.util.thread.TimerTaskManger.instance().schedule("load_users", 
					new TimerTask(){
						public void run(){
							String next_openid = request.getAttibute("next_openid")!=null?(String)request.getAttibute("next_openid"):null;
							UserManager.this.loadUsers(next_openid);
						}
			}, 20000);
		}else if("onMessage".equals(request_action)){
			Object handle = request.getHandler();
			InMessage h = (InMessage)handle;
			User u = new User(null);
			u.setOpenid(h.getFromUserName());
		    h.setFrom(u);
			session.onMessage(h);
		}
	}

	@Override
	public void onOk(HttpRequest request, Object paramObject) {
		// TODO Auto-generated method stub
		logger.info(paramObject);
		String request_action = (String) request.getAttibute(WechatSession.RequestAction);
		if("loadGroups".equals(request_action)){
			groups = Group.getGroups(paramObject.toString());
		}else if("loadUsers".equals(request_action)){
			if(paramObject instanceof User){
				User user = (User)paramObject;
				String next_openid = user.getNext_openid();
				this.setCount(user.getCount());
				this.setTotal(user.getTotal());
				List<OpenID> openids = user.getOpenids();
				
				if(request.getAttibute("next_openid")==null){
					this.openids = openids;
				}else{
					if(this.openids==null)
						this.openids = new ArrayList<OpenID>();
					for(OpenID id : openids)
						this.openids.add(id);
					openids.clear();
					openids = null;
				}
				if(next_openid!=null)
					this.loadUsers(next_openid);
			}else{
				logger.warn("class can't map user:"+paramObject);
			}
		}else if("onMessage".equals(request_action)){
			Object handle = request.getHandler();
			InMessage h = (InMessage)handle;
			User u = null;
			if (paramObject instanceof User) {
			     u = (User)paramObject;
			     h.setFrom(u);
			     u.setCreate_time(new Date().getTime());
			     users.put(h.getFromUserName(), u);
			     if(update_users==null){
			    	 update_users = org.jcommon.com.util.thread.TimerTaskManger.instance().schedule("update_users",
			    			 new TimerTask(){
			    		 public void run(){
			    			 List<User> clear = new ArrayList<User>();
			    			 synchronized(users){
			    				 long now = new Date().getTime();
			    				 for(User u : users.values()){
			    					 if(now-u.getCreate_time()>expires)
			    						 clear.add(u);
			    				 }
			    				 for(User u : clear){
				    				 users.remove(u.getOpenid());
				    			 }
			    				 clear.clear();
			    				 clear = null;
			    			 }
			    			
			    		 }
			    	 }, expires, expires);
			     }
			}
			session.onMessage(h);
		}
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
