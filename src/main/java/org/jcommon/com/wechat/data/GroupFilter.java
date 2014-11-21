package org.jcommon.com.wechat.data;

import java.util.List;

public class GroupFilter extends Filter{
	private  List<Group> groups;
	
	public GroupFilter(List<Group> groups){
		this.groups = groups;
	}
	
	public String toJson(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if(groups!=null){
			for(Group g : groups){
				sb.append("\"group_id\":").append("\"").append(g.getId()).append("\",");
			}
		}
		if (sb.lastIndexOf(",") == sb.length() - 1)
		    sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}
}
