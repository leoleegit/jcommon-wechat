package org.jcommon.com.wechat.data;

import java.util.List;

public class Signature extends org.jcommon.com.util.JsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appId;
	private String timestamp;
	private String nonceStr;
	private String signature;
	private List<StringJson> jsApiList;

	public Signature(String json) {
		super(json);
	}

	public Signature() {
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public List<StringJson> getJsApiList() {
		return jsApiList;
	}

	public void setJsApiList(List<StringJson> jsApiList) {
		this.jsApiList = jsApiList;
	}
}
