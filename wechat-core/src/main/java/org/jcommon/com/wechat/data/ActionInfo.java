package org.jcommon.com.wechat.data;

import org.jcommon.com.wechat.utils.QRType;

public class ActionInfo extends JsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long expire_seconds;
	private String action_name;
	private String scene_id;
	private String scene_str;
	private String action_info;
	
	public ActionInfo(long expire_seconds, QRType type){
		this.expire_seconds = expire_seconds;
		this.action_name    = type.name();
	}
	
	public long getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(long expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public String getScene_str() {
		return scene_str;
	}
	public void setScene_str(String scene_str) {
		this.scene_str = scene_str;
	}
	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}
	public String getScene_id() {
		return scene_id;
	}
	public void setAction_info(String action_info) {
		this.action_info = action_info;
	}
	public String getAction_info() {
		return action_info;
	}
	public String toJson(){
		if(scene_id!=null)
			action_info = "{\"scene\": {\"scene_id\": \""+scene_id+"\"}}}";
		else if(scene_str!=null)
			action_info = "{\"scene\": {\"scene_str\": \""+scene_str+"\"}}}";
		scene_str = null;
		scene_id  = null;
		return super.toJson();
	}
}
