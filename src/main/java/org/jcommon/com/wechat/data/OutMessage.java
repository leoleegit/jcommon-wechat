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

import java.util.List;

import org.jcommon.com.wechat.utils.MsgType;

public class OutMessage extends JsonObject{	
  private String touser;
  private String msgtype;
  private Text text;
  private Image image;
  private Voice voice;
  private Video video;
  private Music music;
  private News news;
  private Mpnews mpnews;
  private Thumb thumb;
  private List<Articles> articles;

  public OutMessage(String data){
    super(data);
  }

  public OutMessage(MsgType type, String touser) {
    this.touser = touser;
    this.msgtype = type!=null?type.toString():null;
  }
  
  public OutMessage() {
  }

  public String getTouser() {
    return this.touser;
  }

  public void setTouser(String touser) {
    this.touser = touser;
  }

  public String getMsgtype() {
    return this.msgtype;
  }

  public void setMsgtype(String msgtype) {
    this.msgtype = msgtype;
  }

  public Text getText() {
    return this.text;
  }

  public void setText(Text text) {
    this.text = text;
  }

  public Image getImage() {
    return this.image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public Voice getVoice() {
    return this.voice;
  }

  public void setVoice(Voice voice) {
    this.voice = voice;
  }

  public Video getVideo() {
    return this.video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

  public Music getMusic() {
    return this.music;
  }

  public void setMusic(Music music) {
    this.music = music;
  }

  public News getNews() {
    return this.news;
  }

  public void setNews(News news) {
    this.news = news;
  }

  public void setMpnews(Mpnews mpnews) {
	this.mpnews = mpnews;
  }

  public Mpnews getMpnews() {
	return mpnews;
  }

  public void setArticles(List<Articles> articles) {
	this.articles = articles;
  }

  public List<Articles> getArticles() {
	return articles;
  }

  public void setThumb(Thumb thumb) {
  	this.thumb = thumb;
  }

  public Thumb getThumb() {
  	return thumb;
  }
  
  public void setMedia(Media media){
	  MsgType type = MsgType.getType(media.getType());
	  if(type == MsgType.image)
		  image = (Image) media;
	  else if(type == MsgType.voice)
		  voice = (Voice) media;
	  else if(type == MsgType.video)
		  video = (Video) media;
	  else if(type == MsgType.mpnews)
		  mpnews = (Mpnews) media;
	  else if(type == MsgType.thumb)
		  thumb = (Thumb) media;
  }
  
  public Media getMedia() {
	  Media media = null;
	  MsgType type = MsgType.getType(msgtype);
	  if(type == MsgType.image)
		  media = image;
	  else if(type == MsgType.voice)
		  media = voice;
	  else if(type == MsgType.video)
		  media = video;
	  else if(type == MsgType.mpnews)
		  media = mpnews;
	  else if(type == MsgType.thumb)
		  media = thumb;
	  return media;
  }
}