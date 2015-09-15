package org.jcommon.com.wechat.data;

import java.util.HashSet;
import java.util.Set;

import org.jcommon.com.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Agent extends Media{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String kf_account;
	private String kf_nick;
	private String kf_id;
	private String kf_headimgurl;
	private String nickname;
	private String password;
	private Set<Agent> kf_list;
	
	public Agent(String kf_account,String nickname,String password){
		this.kf_account = kf_account;
		this.nickname   = nickname;
		this.password   = password;
	}
	
	public Agent(String json){
		super(json);
		JSONObject jsonO =  JsonUtils.getJSONObject(json);
	    if (jsonO != null){
	    	try {
		        if (jsonO.has("kf_list")) {
		        	JSONArray arr = JsonUtils.getJSONArray(jsonO.getString("kf_list"));
		        	kf_list = new HashSet<Agent>();
		        	for (int index = 0; index < arr.length(); index++) {
		        		kf_list.add(new Agent(arr.getString(index)));
		      	    }
		        }
		    }
		    catch (JSONException e) {
		        logger.error("", e);
		    }
	    }
	}

	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	public String getKf_nick() {
		return kf_nick;
	}

	public void setKf_nick(String kf_nick) {
		this.kf_nick = kf_nick;
	}

	public String getKf_id() {
		return kf_id;
	}

	public void setKf_id(String kf_id) {
		this.kf_id = kf_id;
	}

	public String getKf_headimgurl() {
		return kf_headimgurl;
	}

	public void setKf_headimgurl(String kf_headimgurl) {
		this.kf_headimgurl = kf_headimgurl;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}
	
	public Set<Agent> getKf_list() {
		return kf_list;
	}

	public void setKf_list(Set<Agent> kf_list) {
		this.kf_list = kf_list;
	}
}
