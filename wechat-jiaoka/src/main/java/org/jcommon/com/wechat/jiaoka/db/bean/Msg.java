package org.jcommon.com.wechat.jiaoka.db.bean;

public class Msg {
	private int msg_id;
	private String from;
	private String to;
	private String agent;
	private String type;
	private String content;
	private String picUrl;
	private String msg_type;
	
	public int getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(int msg_id) {
		this.msg_id = msg_id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	public String getMsg_type() {
		return msg_type;
	}
}
