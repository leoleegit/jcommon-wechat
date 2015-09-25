package org.jcommon.com.wechat.jiaoka.handlers;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.jiaoka.Handler;
import org.jcommon.com.wechat.utils.EventType;

public class Calculator extends Handler implements RequestCallback{
	private Logger logger = Logger.getLogger(getClass());
	private static final String name = "Calculator";
	
	private static final String msg00 = "很高兴您使用胶卡计算器，请耐心问答一下问题：\n";
	private static final String msg01 = "1.您每一趟车程的费用是多少？ （例如：3元就输入3）";
	private static final String msg02 = "2.您每一趟车程的费用是多少？ （例如：3元就输入3）";
	
	private int step = 0;
	private String[] msgs = new String[]{msg00,msg01};
	
	
	public Calculator(WechatSession session) {
		super(session);
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
		if(event!=null){
			EventType type = EventType.getType(event.getEvent());
			if(type!=null && EventType.CLICK == type){
				logger.info(event.getXml());
				String msg = msgs[0] + msgs[1];
				Text text  = new Text(msg);
				String touser = event.getFromUserName();
				session.getMsg_manager().sendText(touser, text, this);
				return true;
			}
		}
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
