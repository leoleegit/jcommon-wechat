package org.jcommon.com.wechat.jiaoka.handlers;

import org.apache.log4j.Logger;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.jiaoka.Handler;
import org.jcommon.com.wechat.jiaoka.HandlerManager;

public abstract class Robot extends Handler{
	private Logger logger = Logger.getLogger(getClass());
	
	private String[] questions;
	private String[] error_msgs;
	
	public  static final int doing    = 1;
	public  static final int init     = 0;
	public  static final int waitting = 2;
	private int step;
	private int status = init;
	
	public Robot(HandlerManager manager, WechatSession session) {
		super(manager, session);
		// TODO Auto-generated constructor stub
	}
	
	public Robot(String[] questions,String[] error_msgs,HandlerManager manager, WechatSession session) {
		super(manager, session);
		initMessage(questions,error_msgs);
	}
	
	public void initMessage(String[] questions,String[] error_msgs){
		this.questions = questions;
		this.error_msgs= error_msgs;
	}

	public abstract boolean alertGreeting();
	public abstract boolean errorCheck(InMessage message);
	public abstract boolean addAnswer(InMessage message);
	public abstract boolean askQuestion(String question);
	public abstract boolean alertError(String error);
	
	
	public void goNext(){
		if(getStatus()==Robot.init)
			alertGreeting();
		int step = getStep();
		if(questions!=null && questions.length>step){
			askQuestion(questions[step]);
			setStatus(Robot.waitting);
		}else
			logger.warn("not question else there.");
	}
	
	public void goNext(InMessage message){
		if(getStatus()==Robot.doing)
			return;
		
		setStatus(Robot.doing);
		if(!errorCheck(message)){
			addAnswer(message);
			setStep(step+1);
			goNext();
			return;
		}else{
			int step = getStep();
			if(error_msgs!=null && error_msgs.length>=step){
				alertError(error_msgs[step]);
			}else
				logger.warn("not error_msgs else there.");
		}
		setStatus(Robot.waitting);
	}

	public String[] getQuestions() {
		return questions;
	}

	public void setQuestions(String[] questions) {
		this.questions = questions;
	}

	public String[] getError_msgs() {
		return error_msgs;
	}

	public void setError_msgs(String[] error_msgs) {
		this.error_msgs = error_msgs;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
