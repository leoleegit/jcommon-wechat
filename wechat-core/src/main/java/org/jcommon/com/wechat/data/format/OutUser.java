package org.jcommon.com.wechat.data.format;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.wechat.data.JsonObject;
import org.jcommon.com.wechat.data.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutUser extends JsonObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<User> user_list;
	private List<User> user_info_list;
	
	public OutUser(){
		
	}

	public OutUser(String json){
		super(json);
		JSONObject jsonO =  JsonUtils.getJSONObject(json);
	    if (jsonO != null){
	    	try {
		        if (jsonO.has("user_info_list")) {
		        	JSONArray arr = JsonUtils.getJSONArray(jsonO.getString("user_info_list"));
		        	user_info_list = new ArrayList<User>();
		        	for (int index = 0; index < arr.length(); index++) {
		        		user_info_list.add(new User(arr.getString(index)));
		      	    }
		        }
		    }
		    catch (JSONException e) {
		        logger.error("", e);
		    }
	    }
	}
	
	public void setUser_list(List<User> user_list) {
		this.user_list = user_list;
	}
	public List<User> getUser_list() {
		return user_list;
	}
	public void setUser_info_list(List<User> user_info_list) {
		this.user_info_list = user_info_list;
	}
	public List<User> getUser_info_list() {
		return user_info_list;
	}
}
