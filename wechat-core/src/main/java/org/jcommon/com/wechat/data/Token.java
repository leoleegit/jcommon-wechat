package org.jcommon.com.wechat.data;

public class Token extends JsonObject{
	  private String access_token;
	  private long expires_in;
	  private String wechatID;
	  
	  public Token(String access_token,long expires_in){
		  this.access_token = access_token;
		  this.expires_in   = expires_in;
	  }
	  
	  public Token(String json){
		  super(json);
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
  }
