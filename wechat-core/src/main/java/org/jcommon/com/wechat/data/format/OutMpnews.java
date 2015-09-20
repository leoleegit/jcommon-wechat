package org.jcommon.com.wechat.data.format;

import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.data.Media;

public class OutMpnews  extends Media{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Articles articles;
	private int index;
	
	public OutMpnews(String json){
		super(json);
	}

	public OutMpnews(Articles articles,int index){
		this.articles = articles;
		this.index    = index;
	}
	
	public Articles getArticles() {
		return articles;
	}
	public void setArticles(Articles articles) {
		this.articles = articles;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
