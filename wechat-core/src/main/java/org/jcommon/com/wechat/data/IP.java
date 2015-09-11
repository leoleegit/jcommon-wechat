package org.jcommon.com.wechat.data;

import java.util.HashSet;
import java.util.Set;

import org.jcommon.com.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IP extends JsonObject{
	private String ip;
	private Set<IP> ip_list;
	
	public IP(String ips){
		JSONObject jsonO =  ips.startsWith("{")?JsonUtils.getJSONObject(ips):null;
	    if (jsonO != null){
	    	try {
		        if (jsonO.has("ip_list")) {
		        	JSONArray arr = JsonUtils.getJSONArray(jsonO.getString("ip_list"));
		        	ip_list = new HashSet<IP>();
		        	for (int index = 0; index < arr.length(); index++) {
		        		ip_list.add(new IP(arr.getString(index)));
		      	    }
		        }
		    }
		    catch (JSONException e) {
		        logger.error("", e);
		    }
	    }else
	    	this.ip = ips;
	      
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp_list(Set<IP> ip_list) {
		this.ip_list = ip_list;
	}

	public Set<IP> getIp_list() {
		return ip_list;
	}
}
