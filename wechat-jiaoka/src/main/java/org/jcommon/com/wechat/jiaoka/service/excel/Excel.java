package org.jcommon.com.wechat.jiaoka.service.excel;

import org.jcommon.com.wechat.data.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

public class Excel extends JsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String name;
	private String data;
	
	public Excel(String json){
		super(json);
	}
	
	public JSONObject getJsonTitle(){
		return org.jcommon.com.util.JsonUtils.getJSONObject(title);
	}
	
	public JSONArray getJsonData(){
		return org.jcommon.com.util.JsonUtils.getJSONArray(data);
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = org.jcommon.com.util.CoderUtils.decode(title);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = org.jcommon.com.util.CoderUtils.decode(name);
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = org.jcommon.com.util.CoderUtils.decode(data);
	}
}
