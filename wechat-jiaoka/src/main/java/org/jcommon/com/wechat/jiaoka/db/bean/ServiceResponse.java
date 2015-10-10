package org.jcommon.com.wechat.jiaoka.db.bean;

import java.util.List;

public class ServiceResponse extends org.jcommon.com.wechat.data.JsonObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  static final int FAIL    = -1;
	public  static final int SUCCESS = 0;

	private int code;
	private String error;
	private long count;
	private org.jcommon.com.wechat.data.JsonObject data;
	private List<? extends org.jcommon.com.wechat.data.JsonObject> datas;
	
	public ServiceResponse(String error){
		this.error = error;
		setCode(FAIL);
	}
	
	public ServiceResponse(SearchResponse resp){
		this.datas = resp.getDatas();
		this.count = resp.getCount();
		setCode(SUCCESS);
	}
	
	public ServiceResponse(org.jcommon.com.wechat.data.JsonObject data){
		this.data = data;
		setCode(SUCCESS);
	}
	
	public ServiceResponse(List<? extends org.jcommon.com.wechat.data.JsonObject> datas){
		this.datas = datas;
		setCode(SUCCESS);
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public org.jcommon.com.wechat.data.JsonObject getData() {
		return data;
	}
	public void setData(org.jcommon.com.wechat.data.JsonObject data) {
		this.data = data;
	}

	public List<? extends org.jcommon.com.wechat.data.JsonObject> getDatas() {
		return datas;
	}

	public void setDatas(List<org.jcommon.com.wechat.data.JsonObject> datas) {
		this.datas = datas;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCount() {
		return count;
	}
}
