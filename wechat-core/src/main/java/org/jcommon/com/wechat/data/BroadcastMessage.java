package org.jcommon.com.wechat.data;

import org.jcommon.com.wechat.data.filter.Filter;
import org.jcommon.com.wechat.data.filter.UserFilter;
import org.jcommon.com.wechat.utils.MsgType;

public class BroadcastMessage extends OutMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  private Filter filter;
  private Mpvideo mpvideo;

  public BroadcastMessage(MsgType type,Filter filter){
	  super(type,null);
	  this.filter = filter;
  }
  
  public void setFilter(Filter filter) {
	this.filter = filter;
  }

  public Filter getFilter() {
	return filter;
  }
  
  public String toJson(){
	  
	  if(filter!=null && filter instanceof UserFilter){
		  String to = filter.toJson();
		  Filter filter_copy  = filter;
		  filter = null;
		  String str = super.toJson();
		  filter = filter_copy;
		  filter_copy = null;
		  StringBuilder sb = new StringBuilder(str);
		  if (sb.lastIndexOf("}") == sb.length() - 1)
		      sb.deleteCharAt(sb.length() - 1);
		  sb.append(",").append("\"touser\":").append(to);
		  sb.append("}");
		  return sb.toString();
	  }
	  return super.toJson();
  }

	public void setMpvideo(Mpvideo mpvideo) {
		this.mpvideo = mpvideo;
	}
	
	public Mpvideo getMpvideo() {
		return mpvideo;
	}
	
	public Media getMedia() {
	  MsgType type = MsgType.getType(this.getMsgtype());
	  if(type == MsgType.mpvideo)
		  return mpvideo;
	  else return super.getMedia();
  }
}
