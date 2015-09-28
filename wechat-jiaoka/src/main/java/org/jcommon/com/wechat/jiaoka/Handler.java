package org.jcommon.com.wechat.jiaoka;

import java.util.Timer;
import java.util.TimerTask;

import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;

public abstract class Handler {
	private static final long delay = 3 * 60 * 1000;
	protected WechatSession session;
	protected HandlerManager manager;
	private Timer timer;
	private TimerTask task;
	
	public Handler(HandlerManager manager, WechatSession session) {
		this.manager = manager;
		this.session = session;
	}
	public abstract String name();
	public abstract boolean mapJob(Event event, InMessage message);
	public abstract boolean hanlderEvent(Event event);
	public abstract boolean hanlderMessage(InMessage message);
	synchronized public void setExpiry(){
		closeTimer();
		task = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				manager.dutyEnd(Handler.this);
			}
			
		};
		timer = org.jcommon.com.util.thread.TimerTaskManger.instance().schedule("Handler-"+name(), task, delay);
	}
	private void closeTimer(){
		try{
			if(timer!=null){
			timer.cancel();
			timer = null;
			task  = null;
		}
		}catch(Exception e){}
	}
}
