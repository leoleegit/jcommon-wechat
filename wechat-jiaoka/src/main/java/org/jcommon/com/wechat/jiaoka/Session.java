package org.jcommon.com.wechat.jiaoka;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionListener;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatAuditLog;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatUser;
import org.jcommon.com.wechat.jiaoka.db.dao.WeChatAuditLogDao;
import org.jcommon.com.wechat.jiaoka.db.dao.WeChatUserDao;

public class Session extends WechatSession {
	private Map<String,HandlerManager> handlers = new HashMap<String,HandlerManager>();
	private static final String CHECKTIME = "0259";
	private static final long   expires = 12;
	private Timer  expires_timer;
	
	public Session(String wechatID, App app, WechatSessionListener listener) {
		super(wechatID, app, listener);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		logger.info("IN:"+event.getXml());
		WeChatAuditLog bean = new WeChatAuditLog();
		bean.setLogstr(event.getXml());
		bean.setType(event.getEvent());
		if(event.getCreateTime()!=0)
			bean.setCreate_time(new Timestamp(event.getCreateTime()*1000));
		new WeChatAuditLogDao().insert(bean);
		
	    String from = event.getFromUserName();
	    if(from!=null){
	    	HandlerManager handler = null;
	    	if(handlers.containsKey(from))
	    		handler = handlers.get(from);
	    	else{
	    		handler = new HandlerManager(this,from);
	    		handlers.put(from, handler);
	    	}
	    	handler.onEvent(event);
	    }
	}
	
	@Override
	public void onMessage(InMessage message) {
		logger.info("IN:"+message.getXml());
		WeChatAuditLog bean = new WeChatAuditLog();
		bean.setLogstr(message.getXml());
		bean.setType(message.getMessageType().name());
		if(message.getCreateTime()!=0)
			bean.setCreate_time(new Timestamp(message.getCreateTime()*1000));
		new WeChatAuditLogDao().insert(bean);
		
		
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
		
		//insert user
		WeChatUser wechat_user = new WeChatUser(message.getFrom());
		new WeChatUserDao().insert(wechat_user);
		
		String from = message.getFromUserName();
	    if(from!=null){
	    	HandlerManager handler = null;
	    	if(handlers.containsKey(from))
	    		handler = handlers.get(from);
	    	else{
	    		handler = new HandlerManager(this,from);
	    		handlers.put(from, handler);
	    	}
	    	handler.onMessage(message);
	    }
	}
	
	public void startup(){
		super.startup();
		expireCheck();
	}
	
	public void shutdown(){
		closeTimer();
		super.shutdown();
	}
	
	private void closeTimer(){
		try{
			if(expires_timer!=null){
			expires_timer.cancel();
			expires_timer = null;
			logger.info("Login expire checker is stop");
		}
		}catch(Exception e){}
	}
	
	public void expireCheck(){
		logger.info("Login expire checker is start");
		
		final long expire = 24 * 60 * 60 * 1000;
		long firstTime    = getFirstTime();
		expires_timer = org.jcommon.com.util.thread.TimerTaskManger.instance().schedule("Login expire Checker", new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				logger.info("Login expire Checker Start...");
				Set<HandlerManager> expire_users = new HashSet<HandlerManager>();
				
				long now    = System.currentTimeMillis();
				long expire = expires * 60 * 60 * 1000;
				for(HandlerManager hm : handlers.values()){
					if(now-hm.getUpdate_time() > expire)
						expire_users.add(hm);
				}
				for(HandlerManager hm : expire_users){
					synchronized (handlers) {
						handlers.remove(hm.getName());
						logger.info("remove expire handler:"+hm.getName());
					}
				}
				expire_users.clear();
				expire_users = null;
				logger.info("Login expire Checker End...");
			}
			
		}, firstTime, expire);
	}
	
	private long getFirstTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");
		Date now = new Date();
		String today      = sdf.format(now);
		String check      = today.substring(0, 10)+" "+CHECKTIME;
		logger.info(String.format("now:%s;check_time:%s", today,check));
		try {
			
			Date check_date   = sdf.parse(check);
			long first_time   = check_date.getTime();
			logger.info(String.format("now:%s;check_time:%s;", sdf.format(now),sdf.format(check_date)));
			if(first_time < now.getTime())
				first_time = first_time + (24 * 60 * 60 * 1000);
			return 	first_time - now.getTime();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		return 0;
	}
}
