package org.jcommon.com.wechat.data;

public class OpenID extends JsonObject {
	private String openid;

	public OpenID(String openid){
		this.openid = openid;
	}
	
	public String toJson(){
		if(openid!=null)
			return "\"" + openid + "\"";
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return openid;
	}
}
