package org.jcommon.com.wechat.jiaoka.handlers;

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

public class JiaoKa extends Handler implements RequestCallback{
	private Logger logger = Logger.getLogger(getClass());
	
	private static final String name = "JiaoKa";
	public static final String JiaoKaKey1 = "JiaoKa_001";
	public static final String JiaoKaKey2 = "JiaoKa_002";
	public static final String JiaoKaKey3 = "JiaoKa_003";
	
	public static final String[] jiaoka_types = new String[]{"刷卡","交卡","买卡"};
	public static final String[] locations    = new String[]{"机场南站-余额机","祈福都会-7栋","祈福都会-3栋北门","机场南站-P4","金港城-L1","金港城-小食代"};
	
	private String jiaoka_type;
	private String phone_number;
	private int    card_number;
	private String location_give;
	private String location_get;
	private String number;
	
	private int step;
	public JiaoKa(HandlerManager manager, WechatSession session) {
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
			if(type!=null && EventType.CLICK == type){
				if(JiaoKaKey1.equals(event.getEventKey()) || 
						JiaoKaKey2.equals(event.getEventKey())||
						JiaoKaKey3.equals(event.getEventKey())){
					logger.info(event.getXml());
					String msg = null;
					if(JiaoKaKey1.equals(event.getEventKey())){
						setJiaoka_type(jiaoka_types[0]);
						msg = "请选择交卡地点：\n";
					}else if(JiaoKaKey2.equals(event.getEventKey())){
						setJiaoka_type(jiaoka_types[1]);
						msg = "请选择领卡地点：\n";
					}else{
						setJiaoka_type(jiaoka_types[2]);
						msg = "请选择领卡地点：\n";
					}
					for(int i=0; i<locations.length;i++){
						msg = msg + (i+1) + "."+ locations[i] + "\n";
					}
					Text text  = new Text(msg);
					String touser = event.getFromUserName();
					session.getMsg_manager().sendText(touser, text, this);
					step = 1;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean hanlderEvent(Event event) {
		// TODO Auto-generated method stub
		return mapJob(event,null);
	}

	private boolean errorCheck(InMessage message, String error){
		String txt = message.getContent();
		boolean isError = false;
		if(Calculator.isNumeric(txt)){
			int num  = Integer.valueOf(txt);
			if(num>0&&num<=locations.length){
				return false;
			}else
				isError = true;
		}else{
			isError = true;
		}
		if(isError){
			Text text  = new Text(error);
			String touser = message.getFromUserName();
			session.getMsg_manager().sendText(touser, text, this);
		}
		return isError;
	}
	
	@Override
	public boolean hanlderMessage(InMessage message) {
		// TODO Auto-generated method stub
		switch(step){
			case 1:{
				String msg = null;
				if(jiaoka_types[0].equals(jiaoka_type)){
					if(!setLocation_give(message)){
						break;
					}
					msg = "请选择领卡地点：\n";
					for(int i=0; i<locations.length;i++){
						msg = msg + (i+1) + "."+ locations[i] + "\n";
					}
				}else if(jiaoka_types[1].equals(jiaoka_type)){
					String error_msg = "不要玩啦，只允许输入1到"+locations.length+"的数字哟。(●'◡'●)\n";
					if(errorCheck(message,error_msg)){
						break;
					}
					String txt = message.getContent();
					int num  = Integer.valueOf(txt);
					this.location_get = locations[num];
					msg = "请输入手机号码：\n";
				}else if(jiaoka_types[2].equals(jiaoka_type)){
					String error_msg = "不要玩啦，只允许输入1到"+locations.length+"的数字哟。(●'◡'●)\n";
					if(errorCheck(message,error_msg)){
						break;
					}
					String txt = message.getContent();
					int num  = Integer.valueOf(txt);
					this.location_get = locations[num];
					msg = "请输入手机号码：\n";
				}else{
					manager.dutyEnd(this);
					break;
				}
				Text text  = new Text(msg);
				String touser = message.getFromUserName();
				session.getMsg_manager().sendText(touser, text, this);
				step = 2;
				break;
			}
	     	case 2:{
	     		String msg = null;
				if(jiaoka_types[0].equals(jiaoka_type)){
					if(!setLocation_get(message)){
						break;
					}
					msg = "请输入联系手机号码：\n";
				}else if(jiaoka_types[1].equals(jiaoka_type)){
					msg = "请输入预定数量：\n";
				}else if(jiaoka_types[2].equals(jiaoka_type)){
					msg = "请输入预定数量：\n";
				}else{
					manager.dutyEnd(this);
					break;
				}
				Text text  = new Text(msg);
				String touser = message.getFromUserName();
				session.getMsg_manager().sendText(touser, text, this);
				step = 3;
				break;
			}
	     	case 3:{
	     		String msg = null;
				if(jiaoka_types[0].equals(jiaoka_type)){
					if(!setPhone_number(message)){
						break;
					}
					msg = "请输入预定数量：\n";
				}else if(jiaoka_types[1].equals(jiaoka_type)){
					msg = "请输入预定数量：\n";
				}else if(jiaoka_types[2].equals(jiaoka_type)){
					msg = "请输入预定数量：\n";
				}else{
					manager.dutyEnd(this);
					break;
				}
				Text text  = new Text(msg);
				String touser = message.getFromUserName();
				session.getMsg_manager().sendText(touser, text, this);
				step = 4;
				break;
			}
	     	case 4:{
	     		String msg = null;
				if(jiaoka_types[0].equals(jiaoka_type)){
					if(!setCard_number(message)){
						break;
					}
					msg = OrderStr();
				}else if(jiaoka_types[1].equals(jiaoka_type)){
					msg = "请输入预定数量：\n";
				}else if(jiaoka_types[2].equals(jiaoka_type)){
					msg = "请输入预定数量：\n";
				}else{
					manager.dutyEnd(this);
					break;
				}
				Text text  = new Text(msg);
				String touser = message.getFromUserName();
				session.getMsg_manager().sendText(touser, text, this);
				step = 0;
				manager.dutyEnd(this);
				break;
			}
		    default:{
				break;
			}
		}
		return false;
	}

	public String OrderStr(){
		StringBuilder sb = new StringBuilder();
		String title     = "您的订单如下：\n";
		sb.append(title);
		if(jiaoka_types[0].equals(jiaoka_type)){
			sb.append("类型:").append(jiaoka_type).append("\n")
			.append("交卡地点:").append(this.getLocation_give()).append("\n")
			.append("领卡地点:").append(this.getLocation_get()).append("\n")
			.append("联系手机:").append(this.getPhone_number()).append("\n")
			.append("预定数量:").append(this.getCard_number()).append("\n");
		}else if(jiaoka_types[1].equals(jiaoka_type)){
			
		}else if(jiaoka_types[2].equals(jiaoka_type)){
		 
		}
		return sb.toString();
	}
	
	public String getJiaoka_type() {
		return jiaoka_type;
	}

	public void setJiaoka_type(String jiaoka_type) {
		this.jiaoka_type = jiaoka_type;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public boolean setPhone_number(InMessage message) {
		String error_msg = "请输入合法的手机号码。(●'◡'●)\n";
		String txt = message.getContent();
		if(Calculator.isNumeric(txt) && txt.length()==11){
			this.phone_number = txt;
			return true;
		}else{
			Text text  = new Text(error_msg);
			String touser = message.getFromUserName();
			session.getMsg_manager().sendText(touser, text, this);
			return false;
		}
	}

	public int getCard_number() {
		return card_number;
	}

	public boolean setCard_number(InMessage message) {
		String error_msg = "只允许输入数字哟。(●'◡'●)\n";
		String txt = message.getContent();
		if(Calculator.isNumeric(txt)){
			this.card_number = Integer.valueOf(txt);
			return true;
		}else{
			Text text  = new Text(error_msg);
			String touser = message.getFromUserName();
			session.getMsg_manager().sendText(touser, text, this);
			return false;
		}
	}

	public String getLocation_give() {
		return location_give;
	}

	public boolean setLocation_give(InMessage message) {
		String error_msg = "不要玩啦，只允许输入1到"+locations.length+"的数字哟。(●'◡'●)\n";
		if(errorCheck(message,error_msg)){
			return false;
		}
		String txt = message.getContent();
		int num  = Integer.valueOf(txt);
		this.location_give = locations[num];
		return true;
	}

	public String getLocation_get() {
		return location_get;
	}

	public boolean setLocation_get(InMessage message) {
		String error_msg = "不要玩啦，只允许输入1到"+locations.length+"的数字哟。(●'◡'●)\n";
		if(errorCheck(message,error_msg)){
			return false;
		}
		String txt = message.getContent();
		int num  = Integer.valueOf(txt);
		this.location_get = locations[num];
		return true;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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
