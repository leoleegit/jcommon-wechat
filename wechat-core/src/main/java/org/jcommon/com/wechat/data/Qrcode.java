package org.jcommon.com.wechat.data;

import org.jcommon.com.wechat.RequestFactory;

public class Qrcode extends JsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ticket;
	private String expire_seconds;
	private String url;

	public Qrcode(String json) {
		super(json);
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(String expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicUrl() {
		String url = RequestFactory.mp_url + "/showqrcode?ticket=" + ticket;
		return url;
	}
}
