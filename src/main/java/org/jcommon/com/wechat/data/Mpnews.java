package org.jcommon.com.wechat.data;

import java.util.List;

import org.jcommon.com.wechat.utils.MediaType;

public class Mpnews extends Media{
  
	private List<Articles> articles;
	  
	public Mpnews(String data){
		super(data);
		setType(MediaType.mpnews.toString());
	}
	  
	public Mpnews(){
		super();
		setType(MediaType.mpnews.toString());
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
}
