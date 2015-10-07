package org.jcommon.com.wechat.jiaoka.db.bean;

import java.sql.Timestamp;

public class Case extends org.jcommon.com.wechat.data.JsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String OPEN     = "open";
	public static final String CLOSE    = "close";
	public static final String REOPEN   = "reopen";
	public static final String FOLLOW   = "following";
	
	private int id;
	private String case_id; 
	private String status;
	private String note;
	private String handle_agent;
	
	private String jiaoka_type;
	private String phone_number;
	private int    card_number;
	private String location_give;
	private String location_get;
	
	private String openid;
	private String nickname;
	
	private Timestamp create_time;
	private Timestamp update_time;
	private int isdelete;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCase_id() {
		return case_id;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getHandle_agent() {
		return handle_agent;
	}
	public void setHandle_agent(String handle_agent) {
		this.handle_agent = handle_agent;
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
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public int getCard_number() {
		return card_number;
	}
	public void setCard_number(int card_number) {
		this.card_number = card_number;
	}
	public String getLocation_give() {
		return location_give;
	}
	public void setLocation_give(String location_give) {
		this.location_give = location_give;
	}
	public String getLocation_get() {
		return location_get;
	}
	public void setLocation_get(String location_get) {
		this.location_get = location_get;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public int getIsdelete() {
		return isdelete;
	}
}
