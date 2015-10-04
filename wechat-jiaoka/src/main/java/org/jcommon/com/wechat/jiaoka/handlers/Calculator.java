package org.jcommon.com.wechat.jiaoka.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.jiaoka.HandlerManager;
import org.jcommon.com.wechat.utils.EventType;
import org.jcommon.com.wechat.utils.MsgType;

public class Calculator extends Robot implements RequestCallback{
	private Logger logger = Logger.getLogger(getClass());
	private static final String name = "Calculator";
	public  static final String CalculatorKey = "CalculatorKey_001";
	
	private static final String greeting = "很高兴您使用胶卡计算器\n( 计算公式:\n单程价格 * 车程次数 * 0.6 + 刷点15块)\n请耐心问答以下两个问题：";
	private static final String msg01 = "1.您每一趟车程的费用是多少？ （例如：3元就输入3）";
	private static final String msg02 = "2.您每一个月上班的天数？（例如：22天就输入22）";
	
	private static final String error01 = "不要玩啦，只允许输入数字哟。(●'◡'●)\n";
	private static final String error02 = "不要玩啦，只允许输入数字哟。(●'◡'●)\n";
	
	private int   days;
	private float cost;
	
	public Calculator(HandlerManager manager, WechatSession session) {
		super(manager, session);
		// TODO Auto-generated constructor stub
		String[] questions  = new String[]{msg01,msg02};
		String[] error_msgs = new String[]{error01,error02};
		super.initMessage(questions, error_msgs);
	}

	@Override
	public boolean mapJob(Event event, InMessage message) {
		// TODO Auto-generated method stub
		if(event!=null){
			EventType type = EventType.getType(event.getEvent());
			if(type!=null && EventType.CLICK == type && Calculator.CalculatorKey.equals(event.getEventKey())){
				setExpiry();
				logger.info(event.getXml());
				super.setStatus(Robot.init);
				super.setStep(0);
				goNext();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hanlderEvent(Event event) {
		// TODO Auto-generated method stub
		return mapJob(event,null);
	}

	@Override
	public boolean hanlderMessage(InMessage message) {
		// TODO Auto-generated method stub
		setExpiry();
		
		goNext(message);
		int step = getStep();
		if(step==2){
			float before = before(this.cost,this.days);
			float after  = after(this.cost,this.days);
			String prices= String.format("%.2f", (before-after));
			
			StringBuilder sb = new StringBuilder();
			if(message.getFrom()!=null){
				String nickname = message.getFrom().getNickname();
				sb.append("微信昵称 : ").append(nickname).append("\n");
			}
			sb.append("----------------------------\n");
			sb.append("单程 ："+this.cost+"元\n");
			sb.append("次数 ："+(this.days*2)+"次\n");
			sb.append("使用胶卡前 ：" + before +" 元\n");
			sb.append("使用胶卡后 ：" + after +" 元\n");
			sb.append(before<after?"很抱歉，未能帮你省到钱了。":("节省了："+prices +" 元")).append("\n");
			sb.append("----------------------------\n");
			sb.append("日期 : ").append(formatDate(new Date()));
			
			Text text  = new Text(sb.toString());
			String touser = message.getFromUserName();
			session.getMsg_manager().sendText(touser, text, this);
			manager.dutyEnd(this);
		}
		return false;
	}
	
	public static String formatDate(Date now){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return f.format(now);
	}
	
	@Override
	public boolean errorCheck(InMessage message) {
		// TODO Auto-generated method stub
		if(message.getMessageType() == MsgType.text){
			String txt = message.getContent();
			return !isNumeric(txt.trim());
		}
		return true;
	}

	@Override
	public boolean addAnswer(InMessage message) {
		// TODO Auto-generated method stub
		int step = getStep();
		switch(step){
			case 0:{
				String txt = message.getContent();
				this.cost = Integer.valueOf(txt);
				break;
			}
			case 1:{
				String txt = message.getContent();
				this.days = Integer.valueOf(txt);
				break;
			}
		}
		return false;
	}

	@Override
	public boolean askQuestion(String question) {
		// TODO Auto-generated method stub
		if(getStatus()==Robot.init)
			question = greeting + "\n"+ question;
		Text text  = new Text(question);
		String touser = manager.getName();
		session.getMsg_manager().sendText(touser, text, this);
		return false;
	}

	@Override
	public boolean alertError(String error) {
		// TODO Auto-generated method stub
		Text text  = new Text(error);
		String touser = manager.getName();
		session.getMsg_manager().sendText(touser, text, this);
		return false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean alertGreeting() {
		// TODO Auto-generated method stub
//		Text text  = new Text(greeting);
//		String touser = manager.getName();
//		session.getMsg_manager().sendText(touser, text, this);
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
