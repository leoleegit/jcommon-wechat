package org.jcommon.com.wechat.jiaoka;

import java.util.HashMap;
import java.util.Map;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionListener;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.User;

public class Session extends WechatSession {
	private Map<String,HandlerManager> handlers = new HashMap<String,HandlerManager>();
	
	public Session(String wechatID, App app, WechatSessionListener listener) {
		super(wechatID, app, listener);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		logger.info("IN:"+event.getXml());
	    String from = event.getFromUserName();
	    if(from!=null){
	    	HandlerManager handler = null;
	    	if(handlers.containsKey(from))
	    		handler = handlers.get(from);
	    	else{
	    		handler = new HandlerManager(this);
	    		handlers.put(from, handler);
	    	}
	    	handler.onEvent(event);
	    }
	}
	
	@Override
	public void onMessage(InMessage message) {
		logger.info("IN:"+message.getXml());
		if(message.getFrom()==null && message.getFromUserName()!=null){
			
	    	User user = new User(null);
	    	user.setOpenid(message.getFromUserName());
	    	
	    	if(getUser_manager().getUserInfo(user)!=null){
	    		message.setFrom(getUser_manager().getUserInfo(user));
	    	}else{
	    		HttpRequest request = getUser_manager().getUserInfo(user, this);
		    	request.setAttribute(INMESSAGE, message);
		    	return;
	    	}
	    }
		
		String from = message.getFromUserName();
	    if(from!=null){
	    	HandlerManager handler = null;
	    	if(handlers.containsKey(from))
	    		handler = handlers.get(from);
	    	else{
	    		handler = new HandlerManager(this);
	    		handlers.put(from, handler);
	    	}
	    	handler.onMessage(message);
	    }
	}
}
