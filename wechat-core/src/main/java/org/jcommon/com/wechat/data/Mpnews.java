package org.jcommon.com.wechat.data;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.wechat.utils.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Mpnews extends Media{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Articles> articles;
	private int index;
	  
	public Mpnews(String data){
		super(data);
		setType(MediaType.mpnews.toString());
		JSONObject jsonO =  JsonUtils.getJSONObject(data);
	    if (jsonO != null){
	    	try {
		        if (jsonO.has("content")) {
		        	jsonO =  JsonUtils.getJSONObject(jsonO.getString("content"));
		        	if (jsonO.has("news_item")) {
		        		JSONArray arr = JsonUtils.getJSONArray(jsonO.getString("news_item"));
			        	articles      = new ArrayList<Articles>();
			        	for (int index = 0; index < arr.length(); index++) {
			        		articles.add(new Articles(arr.getString(index)));
			      	    }
			        }
		        }
		    }
		    catch (JSONException e) {
		        logger.error("", e);
		    }
	    }
	}
	  
	public Mpnews(){
		super();
	}
	
	public Mpnews(int index, Articles article){
		super();
		this.index    = index;
		this.articles = new ArrayList<Articles>();
		this.articles.add(article);
	}
	
	public Mpnews(List<Articles> articles){
		super();
		this.articles = articles;
	}

	public void setArticles(List<Articles> articles) {
		this.articles = articles;
	}
	
	public List<Articles> getArticles() {
		return articles;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getType() {
		return MediaType.mpnews.name();
    }
}
