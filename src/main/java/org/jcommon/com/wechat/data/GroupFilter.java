package org.jcommon.com.wechat.data;

import java.util.List;

public class GroupFilter extends Filter{
	private boolean is_to_all;
	private  List<Group> groups;
	
	public GroupFilter(boolean is_to_all, List<Group> groups){
		this.groups = groups;
		this.is_to_all = is_to_all;
	}
	
	public String toJson(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"is_to_all\":").append(is_to_all).append(",");
		if(groups!=null){
			for(Group g : groups){
				if(g.getCount()==0)
					continue;
				sb.append("\"group_id\":").append("\"").append(g.getId()).append("\",");
			}
		}
		if (sb.lastIndexOf(",") == sb.length() - 1)
		    sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	public void setIs_to_all(boolean is_to_all) {
		this.is_to_all = is_to_all;
	}

	public boolean isIs_to_all() {
		return is_to_all;
	}
}
