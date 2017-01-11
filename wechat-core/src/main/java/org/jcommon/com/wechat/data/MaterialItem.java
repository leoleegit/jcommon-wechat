package org.jcommon.com.wechat.data;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MaterialItem extends JsonObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int total_count;
	private int item_count;
	private List<Mpnews> item;

	public MaterialItem(String json) {
		super(json);
		JSONObject jsonO = JsonUtils.getJSONObject(json);
		if (jsonO != null) {
			try {
				if (jsonO.has("item")) {
					JSONArray arr = JsonUtils.getJSONArray(jsonO
							.getString("item"));
					item = new ArrayList<Mpnews>();
					for (int index = 0; index < arr.length(); index++) {
						item.add(new Mpnews(arr.getString(index)));
					}
				}
			} catch (JSONException e) {
				logger.error("", e);
			}
		}
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setItem_count(int item_count) {
		this.item_count = item_count;
	}

	public int getItem_count() {
		return item_count;
	}

	public void setItem(List<Mpnews> item) {
		this.item = item;
	}

	public List<Mpnews> getItem() {
		return item;
	}

}
