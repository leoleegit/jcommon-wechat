package org.jcommon.com.wechat.data;

public class Router extends JsonObject{
	public final static String MESSAGE= "message";
	public final static String EVENT  = "event";
	
	private String signature; 
	private String timestamp; 
	private String nonce;
	private String xml;
	private String router_type = MESSAGE;
	
	public Router(String signature, String timestamp, String nonce, String xml){
		setSignature(signature);
		setTimestamp(timestamp);
		setNonce(nonce);
		setXml(xml);
	}
	
	public Router(String json){
		super(json);
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getRouter_type() {
		return router_type;
	}

	public Router setRouter_type(String router_type) {
		this.router_type = router_type;
		return this;
	}
}
