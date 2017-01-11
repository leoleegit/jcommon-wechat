package org.jcommon.com.wechat.manage.service;

import java.util.List;

public class ServiceResponse extends org.jcommon.com.util.JsonObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  static final int FAIL    = -1;
	public  static final int SUCCESS = 0;

	private int code;
	private String msg;
	private long count;
	private org.jcommon.com.util.JsonObject data;
	private List<? extends org.jcommon.com.util.JsonObject> datas;
	
	public ServiceResponse(String error){
		super(null,true);
		this.setMsg(error);
		setCode(FAIL);
	}
	
	public ServiceResponse(int code, String error){
		super(null,true);
		this.setMsg(error);
		setCode(code);
	}
	
	public ServiceResponse(org.jcommon.com.util.JsonObject data){
		super(null,true);
		this.data = data;
		setCode(SUCCESS);
	}
	
	public ServiceResponse(List<? extends org.jcommon.com.util.JsonObject> datas){
		super(null,true);
		setDatas(datas);
		setCode(SUCCESS);
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public void setError(String error) {
		this.setMsg(error);
	}
	public org.jcommon.com.util.JsonObject getData() {
		return data;
	}
	public void setData(org.jcommon.com.util.JsonObject data) {
		this.data = data;
		setCount(data!=null?1:0);
	}

	public List<? extends org.jcommon.com.util.JsonObject> getDatas() {
		return datas;
	}

	public void setDatas(List<? extends org.jcommon.com.util.JsonObject> datas) {
		this.datas = datas;
		setCount(datas!=null?datas.size():0);
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCount() {
		return count;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}


