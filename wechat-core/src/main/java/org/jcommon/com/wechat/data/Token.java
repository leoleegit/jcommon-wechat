package org.jcommon.com.wechat.data;

public class Token extends JsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String access_token;
	private long expires_in;
	private String wechatID;

	private String signature;
	private String timestamp;
	private String nonce;
	private String Token;
	private String ticket;

	public Token(String access_token, long expires_in) {
		this.access_token = access_token;
		this.expires_in = expires_in;
	}

	public Token(String xml, String signature, String timestamp, String nonce) {
		super(xml);
		this.signature = signature;
		this.timestamp = timestamp;
		this.nonce = nonce;
	}

	public Token(String xml) {
		super(xml);
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getWechatID() {
		return wechatID;
	}

	public void setWechatID(String wechatID) {
		this.wechatID = wechatID;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getTicket() {
		return ticket;
	}

}
