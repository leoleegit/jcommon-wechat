package org.jcommon.com.wechat.jiaoka.db.bean;

import java.sql.Timestamp;

public class WeChatAuditLog extends org.jcommon.com.wechat.data.JsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String type;
	private String logstr;
	private String openid;
	private Timestamp create_time;
	private int isdelete;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLogstr() {
		return logstr;
	}
	public void setLogstr(String logstr) {
		this.logstr = logstr;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getOpenid() {
		return openid;
	}
}
