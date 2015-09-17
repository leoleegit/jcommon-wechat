package org.jcommon.com.wechat.data.format;

import org.jcommon.com.wechat.data.JsonObject;

public class Materials extends JsonObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private int offset;
	private int count;
	
	public Materials(String type,int offset,int count){
		this.type = type;
		this.offset = offset;
		this.count  = count;
	}
	
	public Materials(String json){
		super(json);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
