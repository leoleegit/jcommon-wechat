// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.wechat.data;

public class Articles extends Media
{
  private String title;
  private String description;
  private String url;
  private String picurl;
  
  //上传图文消息素材
  private String thumb_media_id;
  private String author;
  //private String title;
  private String content_source_url;
  private String content;
  private String digest;
  private int show_cover_pic; // 1 or 0

  public Articles(String title, String description, String url, String picurl)
  {
    this.title = title;
    this.description = description;
    this.url = url;
    this.picurl = picurl;
  }
  
  public Articles(String title,String content,String digest,String content_source_url,
		  String author){
	  this(title,content,digest,content_source_url,author,0);
  }
  
  public Articles(String title,String content,String digest,String content_source_url,
		  String author,int show_cover_pic){
	 this.title = title;
	 this.content = content;
	 this.digest  = digest;
	 this.content_source_url = content_source_url;
	 this.show_cover_pic = show_cover_pic;
	 this.author = author;
  }

  public Articles(String data) {
    super(data);
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPicurl() {
    return this.picurl;
  }

  public void setPicurl(String picurl) {
    this.picurl = picurl;
  }

	public String getThumb_media_id() {
		return thumb_media_id;
	}
	
	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getContent_source_url() {
		return content_source_url;
	}
	
	public void setContent_source_url(String content_source_url) {
		this.content_source_url = content_source_url;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDigest() {
		return digest;
	}
	
	public void setDigest(String digest) {
		this.digest = digest;
	}
	
	public int getShow_cover_pic() {
		return show_cover_pic;
	}
	
	public void setShow_cover_pic(int show_cover_pic) {
		this.show_cover_pic = show_cover_pic;
	}
  
  
}