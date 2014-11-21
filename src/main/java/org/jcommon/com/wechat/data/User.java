// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.wechat.data;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.util.Json2Object;
import org.json.JSONException;
import org.json.JSONObject;

public class User extends JsonObject{
	private String subscribe;
	private String openid;
	private String nickname;
	private String sex;
	private String language;
	private String city;
	private String province;
	private String country;
	private String headimgurl;
	private long subscribe_time;
	
	private static List<String> openids;
	private static long total;
	private static long count;
	private String data;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public User(String json){
		super(json);
		
		if(data!=null){
			JSONObject jsonObject = null;
	        try {
	        	User.openids = new ArrayList<String>();
	            jsonObject = new JSONObject(data);
	            if(jsonObject!=null){
	            	String openids = jsonObject.has("openid")?jsonObject.getString("openid"):null;
	            	if(openids.startsWith("["))
	            		openids = openids.substring(1);
        			if(openids.endsWith("]"))
        				openids = openids.substring(0, openids.length()-1);
	            	if(openids!=null){
	            		String[] ids = openids.split(","); 
	            		for(String id : ids){
	            			if(id.startsWith("\""))
	            				id = id.substring(1);
	            			if(id.endsWith("\""))
	            				id = id.substring(0, id.length()-1);
	            			User.openids.add(id);
	            		}
	            	}else{
	            		logger.info("openids is null:"+data);
	            	}
	            }
	        } catch (JSONException e) {
	        	 Json2Object.logger.error("", e);
	        }
		}
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public long getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(long subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public static List<String> getOpenids() {
		return openids;
	}
}
