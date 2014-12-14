package org.jcommon.com.wechat.data;

import java.util.List;

import org.jcommon.com.wechat.utils.MsgType;

public class BroadcastMessage extends OutMessage {
  private Filter filter;	
  private List<OpenID> touser;

  public BroadcastMessage(MsgType type,Filter filter){
	  super(type,null);
	  this.filter = filter;
  }
  
  public BroadcastMessage(MsgType type,List<OpenID> touser){
	  super(type,null);
	  this.setTouser(touser);
  }
  
  public void setFilter(Filter filter) {
	this.filter = filter;
  }

  public Filter getFilter() {
	return filter;
  }

  public void setTouser(List<OpenID> touser) {
	this.touser = touser;
  }

  public List<OpenID> getTousers() {
	  return touser;
  }
  
  public String toJson(){
	  String str = super.toJson();
	  if(str!=null){
		  StringBuilder sb = new StringBuilder(str);
		  if (sb.lastIndexOf("}") == sb.length() - 1)
		      sb.deleteCharAt(sb.length() - 1);
		  sb.append(",").append("\"touser\":").append(list2Json(touser));
		  sb.append("}");
		  return sb.toString();
	  }
	  return str;
  }
}
