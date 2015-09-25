package org.jcommon.com.wechat.jiaoka;

import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionListener;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.jiaoka.handlers.Calculator;

public class Session extends WechatSession {
	
	private Handler[] handlers;
	private Handler on_duty;
	
	public Session(String wechatID, App app, WechatSessionListener listener) {
		super(wechatID, app, listener);
		// TODO Auto-generated constructor stub
		handlers = new Handler[]{new Calculator(this)};
	}
	
	public void dutyEnd(Handler handler){
		logger.info(handler.name());
		on_duty = null;
	}
	
	public void onDuty(Handler handler){
		logger.info(handler.name());
		on_duty = handler;
	}
	
	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		logger.info("IN:"+event.getXml());
	    if(on_duty!=null){
	    	on_duty.hanlderEvent(event);
	    }else{
	    	for(Handler handler : handlers){
	    		if(handler.mapJob(event, null)){
	    			onDuty(handler);
	    			break;
	    		}
	    	}
	    }
	}
	
	@Override
	public void onMessage(InMessage message) {
		logger.info("IN:"+message.getXml());
		if(on_duty!=null){
		    on_duty.hanlderMessage(message);
		}else{
			for(Handler handler : handlers){
	    		if(handler.mapJob(null, message)){
	    			onDuty(handler);
	    			break;
	    		}
	    	}
		}
	}
}
