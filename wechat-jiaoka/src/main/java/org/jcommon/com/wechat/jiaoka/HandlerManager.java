package org.jcommon.com.wechat.jiaoka;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.jiaoka.handlers.Calculator;

public class HandlerManager implements RequestCallback{
	private Logger logger = Logger.getLogger(getClass());
	
	private Handler[] handlers;
	private Handler on_duty;
	private WechatSession session;
	
	public HandlerManager(WechatSession session){
		handlers = new Handler[]{new Calculator(this, session)};
		this.session = session;
	}
	
	public void dutyEnd(Handler handler){
		logger.info(handler.name());
		on_duty = null;
	}
	
	public void onDuty(Handler handler){
		logger.info(handler.name());
		on_duty = handler;
	}
	
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

	public void onMessage(InMessage message) {
		logger.info("IN:"+message.getXml());
		if(on_duty!=null){
		    on_duty.hanlderMessage(message);
		}else{
			boolean handled = false;
			for(Handler handler : handlers){
	    		if(handler.mapJob(null, message)){
	    			onDuty(handler);
	    			handled = true;
	    			break;
	    		}
	    	}
			if(!handled){
				String msg = "小胶表示不懂！不过你放心，小胶正在努力地成长，会变得越来越聪明的。你也要努力哦   (●'◡'●)";
				Text text  = new Text(msg);
				String touser = message.getFromUserName();
				session.getMsg_manager().sendText(touser, text, this);
			}
		}
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
}
