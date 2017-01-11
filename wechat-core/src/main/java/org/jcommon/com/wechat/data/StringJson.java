package org.jcommon.com.wechat.data;

public class StringJson extends org.jcommon.com.util.JsonObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String str;

	public StringJson(String str) {
		this.str = str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}

	public String toJson() {
		return str;
	}
}
