package org.jcommon.com.wechat.jiaoka;

import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;

public abstract class Handler {
	protected WechatSession session;
	protected HandlerManager manager;
	public Handler(HandlerManager manager, WechatSession session) {
		this.manager = manager;
		this.session = session;
	}
	public abstract String name();
	public abstract boolean mapJob(Event event, InMessage message);
	public abstract boolean hanlderEvent(Event event);
	public abstract boolean hanlderMessage(InMessage message);
}
