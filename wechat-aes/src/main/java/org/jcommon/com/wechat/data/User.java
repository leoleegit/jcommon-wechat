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

import org.jcommon.com.util.JsonUtils;
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
	
	private long create_time;
	
	private  List<OpenID> openids;
	private  long total;
	private  long count;
	private  String next_openid;
	
	public User(String json){
		super(json);	
		
		if(json!=null){
			JSONObject jsonObject = null;
	        try {
	        	jsonObject = new JSONObject(json);
	        	total = jsonObject.has("total")?jsonObject.getLong("total"):0;
	        	count = jsonObject.has("count")?jsonObject.getLong("count"):0;
	        	if(count==0)
	        		return;
	        	next_openid = jsonObject.has("next_openid")?jsonObject.getString("next_openid"):null;
	        	json = jsonObject.has("data")?jsonObject.getString("data"):null;
	        	
	        	openids = new ArrayList<OpenID>();
	            jsonObject = JsonUtils.getJSONObject(json);
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
	            			this.openids.add(new OpenID(id));
	            		}
	            	}else{
	            		logger.info("openids is null:"+json);
	            	}
	            }
	        } catch (JSONException e) {
	        	 logger.error("", e);
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

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public List<OpenID> getOpenids() {
		return openids;
	}

	public void setOpenids(List<OpenID> openids) {
		this.openids = openids;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getNext_openid() {
		return next_openid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}
}
