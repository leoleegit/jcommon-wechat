package org.jcommon.com.wechat.jiaoka.service.resp;

public class Url extends org.jcommon.com.wechat.data.JsonObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	public Url(String url){
		this.url = url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}
