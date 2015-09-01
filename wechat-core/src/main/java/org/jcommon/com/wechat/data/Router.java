package org.jcommon.com.wechat.data;

public class Router extends JsonObject{
	private String signature; 
	private String timestamp; 
	private String nonce;
	private String xml;
	
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
}
