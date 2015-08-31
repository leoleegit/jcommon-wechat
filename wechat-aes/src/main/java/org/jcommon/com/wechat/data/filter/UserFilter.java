package org.jcommon.com.wechat.data.filter;

import java.util.List;

import org.jcommon.com.wechat.data.OpenID;

public class UserFilter extends Filter{
	private List<OpenID> touser;
	 
	public UserFilter(List<OpenID> touser){
	   this.setTouser(touser);
	}

	public String toJson(){
	  return list2Json(touser);
	}
	
	public void setTouser(List<OpenID> touser) {
		this.touser = touser;
	}

	public List<OpenID> getTouser() {
		return touser;
	}
}
