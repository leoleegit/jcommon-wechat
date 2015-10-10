package org.jcommon.com.wechat.jiaoka.handlers;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.jiaoka.HandlerManager;
import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.db.dao.CaseDao;
import org.jcommon.com.wechat.jiaoka.db.dao.WeChatUserDao;
import org.jcommon.com.wechat.utils.EventType;
import org.jcommon.com.wechat.utils.MsgType;

public class JiaoKa extends Robot implements RequestCallback{
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
	
	private static final String msg01 = "请选择交卡地点：\n";
	private static final String msg02 = "请选择领卡地点：\n";
	private static final String msg03 = "请输入联系手机号码：";
	private static final String msg04 = "请输入预定数量：";
	
	private static final String error03 = "请输入合法的手机号码。(●'◡'●)";
	private static final String error04 = "只允许输入少于100数字哟。(●'◡'●)";
	
	public JiaoKa(HandlerManager manager, WechatSession session) {
		super(manager, session);
		// TODO Auto-generated constructor stub
		String msg = "";
		for(int i=0; i<locations.length;i++){
			msg = msg + (i+1) + "."+ locations[i] + "\n";
		}
		String error_msg = "不要玩啦，只允许输入1到"+locations.length+"的数字哟。(●'◡'●)";
		String[] questions  = new String[]{msg01+msg,msg02+msg,msg03,msg04};
		String[] error_msgs = new String[]{error_msg,error_msg,error03,error04};
		super.initMessage(questions, error_msgs);
	}
	@Override
	public boolean alertGreeting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean errorCheck(InMessage message) {
		// TODO Auto-generated method stub
		int step = getStep();
		if(message.getMessageType() == MsgType.text){
			String txt = message.getContent();
			switch(step){
				case 0:
				case 1:{
					if(Calculator.isNumeric(txt)){
						int num  = Integer.valueOf(txt);
						if(num>0 && num<=locations.length){
							return false;
						}
					}
					break;
				}
				case 2:{
					if(Calculator.isNumeric(txt) && txt.length()==11){
						return false;
					}
					break;
				}
				case 3:{
					if(Calculator.isNumeric(txt)){
						int num  = Integer.valueOf(txt);
						return num>100;
					}
					break;
				}
			}
		}
		return true;
	}

	@Override
	public boolean addAnswer(InMessage message) {
		// TODO Auto-generated method stub
		int step = getStep();
		if(message.getMessageType() == MsgType.text){
			String txt = message.getContent();
			switch(step){
				case 0:{
					int num  = Integer.valueOf(txt);
					this.location_give = locations[num-1];
					break;
				}
				case 1:{
					int num  = Integer.valueOf(txt);
					this.location_get = locations[num-1];
					break;
				}
				case 2:{
					this.phone_number = txt;
					break;
				}
				case 3:{
					this.card_number = Integer.valueOf(txt);
					break;
				}
			}
		}
		return false;
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
					setExpiry();
					logger.info(event.getXml());
					if(JiaoKaKey1.equals(event.getEventKey())){
						jiaoka_type = (jiaoka_types[0]);
						super.setStep(0);
					}else if(JiaoKaKey2.equals(event.getEventKey())){
						jiaoka_type = (jiaoka_types[1]);
						super.setStep(1);
					}else{
						jiaoka_type = (jiaoka_types[2]);
						super.setStep(1);
					}
					super.setStatus(Robot.init);
					goNext();
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

	@Override
	public boolean hanlderMessage(InMessage message) {
		// TODO Auto-generated method stub
		setExpiry();
		
		goNext(message);
		int step = getStep();
		
		if(step==4){
			String result = OrderStr(message);
			Text text  = new Text(result);
			String touser = manager.getName();
			session.getMsg_manager().sendText(touser, text, this);
			manager.dutyEnd(this);
		}
		return false;
	}
	
	public String OrderStr(InMessage message){	
		StringBuilder sb = new StringBuilder();
		long now   = System.currentTimeMillis();
		final Case case_ = new Case();
		case_.setCase_id(org.jcommon.com.util.BufferUtils.generateRandom(20));
		case_.setCreate_time(new Timestamp(now));
		case_.setJiaoka_type(jiaoka_type);
		case_.setCard_number(card_number);
		case_.setLocation_give(location_give);
		case_.setLocation_get(location_get);
		case_.setPhone_number(phone_number);
		case_.setStatus(Case.OPEN);
		
		if(message.getFrom()!=null){
			String nickname = message.getFrom().getNickname();
			String openid   = message.getFrom().getOpenid();
			sb.append("微信昵称 : ").append(nickname).append("\n");
			case_.setNickname(nickname);
			case_.setOpenid(openid);
		}
		sb.append("----------------------------\n");
		sb.append("类型 : ").append(jiaoka_type).append("\n");
		
		if(jiaoka_types[0].equals(jiaoka_type)){
			sb.append("交卡地点 : ").append(this.location_give).append("\n");
		}
		sb.append("领卡地点 : ").append(this.location_get).append("\n")
			.append("联系手机 : ").append(this.phone_number).append("\n")
			.append("预定数量 : ").append(this.card_number).append("\n");
		sb.append("----------------------------\n");
		sb.append("日期 : ").append(Calculator.formatDate(new Date(now)));
		
		org.jcommon.com.util.thread.ThreadManager.instance().execute(new Runnable(){
			public void run(){
				new CaseDao().insert(case_);
				new WeChatUserDao().updatePhoneNumber(phone_number, manager.getName());
			}
		});
		
		return sb.toString();
	}
	
	@Override
	public boolean askQuestion(String question) {
		// TODO Auto-generated method stub
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
