package org.jcommon.com.wechat.data;

public class Encrypt extends XmlObject{
	private String ToUserName;
	private String Encrypt;
	private String encrypt_type;
	private String msg_signature;
	private String signature;
	private String timestamp;
	private String nonce;
	
	public Encrypt(String encrypt_type, String msg_signature, String signature, String timestamp, String nonce, String xml) {
		super(xml);
		// TODO Auto-generated constructor stub
		this.encrypt_type = encrypt_type;
		this.msg_signature = msg_signature;
		this.signature     = signature;
		this.timestamp     = timestamp;
		this.nonce         = nonce;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getEncrypt() {
		return Encrypt;
	}

	public void setEncrypt(String encrypt) {
		Encrypt = encrypt;
	}

	public String getEncrypt_type() {
		return encrypt_type;
	}

	public void setEncrypt_type(String encrypt_type) {
		this.encrypt_type = encrypt_type;
	}

	public String getMsg_signature() {
		return msg_signature;
	}

	public void setMsg_signature(String msg_signature) {
		this.msg_signature = msg_signature;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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
}
