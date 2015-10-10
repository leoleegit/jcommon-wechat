package org.jcommon.com.wechat.jiaoka.db.bean;

import java.util.List;

public class SearchResponse {
	private long count;
	private List<? extends org.jcommon.com.wechat.data.JsonObject> datas;
	
	public SearchResponse(long count, List<? extends org.jcommon.com.wechat.data.JsonObject> datas){
		this.count = count;
		this.datas = datas;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCount() {
		return count;
	}

	public void setDatas(List<? extends org.jcommon.com.wechat.data.JsonObject> datas) {
		this.datas = datas;
	}

	public List<? extends org.jcommon.com.wechat.data.JsonObject> getDatas() {
		return datas;
	}
}
