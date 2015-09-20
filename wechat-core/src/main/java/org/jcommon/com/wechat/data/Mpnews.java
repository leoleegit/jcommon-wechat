package org.jcommon.com.wechat.data;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.wechat.utils.MediaType;

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
	}
	  
	public Mpnews(){
		super();
		setType(MediaType.mpnews.toString());
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
}
