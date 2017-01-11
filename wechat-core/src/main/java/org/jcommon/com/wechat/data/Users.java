package org.jcommon.com.wechat.data;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Users extends JsonObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<OpenID> openids;
	private long total;
	private long count;
	private String next_openid;
	private List<User> users;

	public Users(String json) {
		super(json);
		JSONObject jsonO = JsonUtils.getJSONObject(json);
		if (jsonO != null) {
			try {
				if (jsonO.has("data")) {
					jsonO = JsonUtils.getJSONObject(jsonO.getString("data"));
					if (jsonO.has("openid")) {
						JSONArray arr = JsonUtils.getJSONArray(jsonO
								.getString("openid"));
						openids = new ArrayList<OpenID>();
						users = new ArrayList<User>();
						for (int index = 0; index < arr.length(); index++) {
							openids.add(new OpenID(arr.getString(index)));
							users.add(new User().setOpenid(arr.getString(index)));
						}
					}
				}
			} catch (JSONException e) {
				logger.error("", e);
			}
		}
	}

	public List<OpenID> getOpenids() {
		return openids;
	}

	public void setOpenids(List<OpenID> openids) {
		this.openids = openids;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getNext_openid() {
		return next_openid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}
}
