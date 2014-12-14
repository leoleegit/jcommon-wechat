package org.jcommon.com.wechat.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jcommon.com.util.Json2Object;
import org.jcommon.com.util.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Group extends JsonObject{
	
	private String id;
	private String name;
	private long count;
	
	public Group(String json){
		super(json);
	}
	
	public static List<Group> getGroups(String json){
		JSONObject jsonO = JsonUtils.getJSONObject(json);
	    if (jsonO != null)
	      try {
	        if (jsonO.has("groups")) {
	          List<Object> list = Json2Object.json2Objects(Group.class, jsonO.getString("groups"));
	          return resetGroups(list);
	        }
	      }
	      catch (JSONException e) {
	        Json2Object.logger.error("", e);
	      }
	    return null;
	}
	
	private static List<Group> resetGroups(List<Object> list){
	    if (list == null) return null;
	    List<Group> groups = new ArrayList<Group>();
	    for (Iterator<?> i$ = list.iterator(); i$.hasNext(); ) {
	      Object o = i$.next();
	      groups.add((Group)o);
	    }
	    list.clear();
	    list = null;
	    return groups;
    }
	
	public static String getGroupsStr(List<Group> groups){
		if(groups==null)
			return null;
		return list2Json(groups);
	}
	  
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
