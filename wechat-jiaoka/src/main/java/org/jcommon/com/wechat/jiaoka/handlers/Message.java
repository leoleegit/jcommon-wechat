package org.jcommon.com.wechat.jiaoka.handlers;

import org.apache.log4j.Logger;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.jiaoka.Handler;
import org.jcommon.com.wechat.jiaoka.HandlerManager;

public class Message extends Handler{
	private Logger logger = Logger.getLogger(getClass());
	
	private static final String name = "Message";
	public Message(HandlerManager manager, WechatSession session) {
		super(manager, session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean mapJob(Event event, InMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hanlderEvent(Event event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hanlderMessage(InMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

}
