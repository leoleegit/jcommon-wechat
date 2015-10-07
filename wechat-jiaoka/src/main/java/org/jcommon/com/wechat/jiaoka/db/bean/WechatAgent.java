package org.jcommon.com.wechat.jiaoka.db.bean;

import java.sql.Timestamp;

public class WechatAgent extends org.jcommon.com.wechat.data.JsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String agent_id;
	private String openid;
	private String remark;
	private Timestamp update_time;
	private String presence;
	private String chating;
	private int isdelete;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}
	public Timestamp getUpdate_time() {
		return update_time;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}
	public String getAgent_id() {
		return agent_id;
	}
	public void setPresence(String presence) {
		this.presence = presence;
	}
	public String getPresence() {
		return presence;
	}
	public String getChating() {
		return chating;
	}
	public void setChating(String chating) {
		this.chating = chating;
	}
}
