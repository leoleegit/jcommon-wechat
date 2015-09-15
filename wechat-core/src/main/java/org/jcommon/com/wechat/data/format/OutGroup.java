package org.jcommon.com.wechat.data.format;

import java.util.List;

import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.JsonObject;
import org.jcommon.com.wechat.data.OpenID;

public class OutGroup extends JsonObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String to_groupid;
	private String openid;
	private String groupid;
	private List<OpenID> openid_list;
	private Group group;
	
	public OutGroup(){
	}
	
	public OutGroup(String json){
		super(json);
	}
	
	public void setTo_groupid(String to_groupid) {
		this.to_groupid = to_groupid;
	}
	public String getTo_groupid() {
		return to_groupid;
	}
	public void setOpenid_list(List<OpenID> openid_list) {
		this.openid_list = openid_list;
	}
	public List<OpenID> getOpenid_list() {
		return openid_list;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Group getGroup() {
		return group;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getOpenid() {
		return openid;
	}
	
	public String delFormat(){
		return "{\"group\":"+super.toJson()+"}";
	}
	
	public String moveFormat(){
		return super.toJson();
	}
	
	public String updateFormat(){
		return super.toJson();
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getGroupid() {
		return groupid;
	}
	
}
