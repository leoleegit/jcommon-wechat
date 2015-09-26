package org.jcommon.com.wechat.jiaoka.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.jiaoka.Handler;
import org.jcommon.com.wechat.jiaoka.HandlerManager;
import org.jcommon.com.wechat.utils.EventType;
import org.jcommon.com.wechat.utils.MsgType;

public class Calculator extends Handler implements RequestCallback{
	private Logger logger = Logger.getLogger(getClass());
	private static final String name = "Calculator";
	public static final String CalculatorKey = "CalculatorKey_001";
	
	private static final String msg00 = "很高兴您使用胶卡计算器\n( 计算公式:\n单程价格 * 车程次数 * 0.6 + 刷点15块)\n请耐心问答以下两个问题：\n";
	private static final String msg01 = "1.您每一趟车程的费用是多少？ （例如：3元就输入3）";
	private static final String msg02 = "2.您每一个月上班的天数？（例如：22天就输入22）";
	private static final String msg03 = "您每个月交通费用如下：\n";
	private static final String msg_error = "不要玩啦，只允许输入数字哟。(●'◡'●)\n";
	
	private int step = 0;
	private String[] msgs = new String[]{msg00,msg01,msg02,msg03};
	
	private int   days;
	private float cost;
	
	public Calculator(HandlerManager manager, WechatSession session) {
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
		if(event!=null){
			EventType type = EventType.getType(event.getEvent());
			if(type!=null && EventType.CLICK == type && Calculator.CalculatorKey.equals(event.getEventKey())){
				logger.info(event.getXml());
				String msg = msgs[0] + msgs[1];
				Text text  = new Text(msg);
				String touser = event.getFromUserName();
				session.getMsg_manager().sendText(touser, text, this);
				step = 1;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hanlderEvent(Event event) {
		// TODO Auto-generated method stub
		if(step==1){
			return false;
		}
		if(event!=null){
			EventType type = EventType.getType(event.getEvent());
			if(type!=null && EventType.CLICK == type && Calculator.CalculatorKey.equals(event.getEventKey()) ){
				logger.info(event.getXml());
				String msg = msgs[0] + msgs[1];
				Text text  = new Text(msg);
				String touser = event.getFromUserName();
				session.getMsg_manager().sendText(touser, text, this);
				step = 1;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hanlderMessage(InMessage message) {
		// TODO Auto-generated method stub
		switch(step){
			case 1:{
				if(message.getMessageType() == MsgType.text){
					String txt = message.getContent();
					if(txt!=null){
						txt = txt.trim();
						if(!isNumeric(txt)){
							String msg = msg_error + msgs[1];
							Text text  = new Text(msg);
							String touser = message.getFromUserName();
							session.getMsg_manager().sendText(touser, text, this);
							break;
						}
						this.cost = Integer.valueOf(txt);
						String msg = msgs[2];
						Text text  = new Text(msg);
						String touser = message.getFromUserName();
						session.getMsg_manager().sendText(touser, text, this);
						step = 2;
					}
				}else{
					String msg = msgs[0] + msgs[1];
					Text text  = new Text(msg);
					String touser = message.getFromUserName();
					session.getMsg_manager().sendText(touser, text, this);
				}
				break;
			}case 2:{
				if(message.getMessageType() == MsgType.text){
					String txt = message.getContent();
					if(txt!=null){
						txt = txt.trim();
						if(!isNumeric(txt)){
							String msg = msg_error + msgs[2];
							Text text  = new Text(msg);
							String touser = message.getFromUserName();
							session.getMsg_manager().sendText(touser, text, this);
							break;
						}
						this.days = Integer.valueOf(txt);
						float before = before(this.cost,this.days);
						float after  = after(this.cost,this.days);
						String prices= String.format("%.2f", (before-after));
						String msg = msgs[3];
						msg        = msg + "单程："+this.cost+"元\n";
						msg        = msg + "次数："+(this.days*2)+"次\n";
						msg        = msg + "使用胶卡前：" + before +" 元\n";
						msg        = msg + "使用胶卡后：" + after +" 元\n";
						msg        = msg + (before<after?"很抱歉，未能帮你省到钱了。":("小胶为您节省了："+prices));
						Text text  = new Text(msg);
						String touser = message.getFromUserName();
						session.getMsg_manager().sendText(touser, text, this);
						step = 0;
						manager.dutyEnd(this);
					}
				}else{
					step = 1;
					hanlderMessage(message);
				}
				break;
			}default:{
				break;
			}
		}
		return false;
	}
	
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    Matcher isNum = pattern.matcher(str);
	    if( !isNum.matches() ){
	        return false; 
	    } 
	    return true; 
	}
	
	public static float before(float cost, int days){
		int times = days * 2;
		if(times>15){
			return (float) (15 * cost + (times-15) * cost * 0.6);
		}else{
			return cost * times;
		}
	}
	
	public static float after(float cost, int days){
		int times = days * 2;
		return (float) (15 + times * cost * 0.6);
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
