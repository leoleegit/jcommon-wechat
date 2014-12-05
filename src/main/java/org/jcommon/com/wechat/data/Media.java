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

import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.RequestFactory;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionManager;
import org.jcommon.com.wechat.utils.MD5;

public class Media extends JsonObject
{
  private String media_id;
  private String thumb_media_id;
  private String type;
  private long created_at;
  private File media;
  private String url;
  private String content_type;
  public final long max_image = 131072L;
  public final long max_voice = 262144L;
  public final long max_video = 1048576L;
  public final long max_thumb = 65536L;

  public final String type_image = "jpg";
  public final String type_voice = "AMR/MP3";
  public final String type_video = "MP4";
  public final String type_thumb = "jpg";

  public Media(String data) {
    super(data);
  }

  public Media()
  {
  }

  public HttpRequest upload(String access_token, HttpListener listener) {
    return RequestFactory.createMediaUploadRequest(listener, access_token, this.media, this.type);
  }

  public HttpRequest download(String access_token, HttpListener listener, File file) {
    return RequestFactory.createMediaDownloaddRequest(listener, access_token, this.media_id, file);
  }

  public String getMedia_id() {
    return this.media_id;
  }
  public void setMedia_id(String media_id) {
    this.media_id = media_id;
  }
  public String getType() {
    return this.type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public long getCreated_at() {
    return this.created_at;
  }
  public void setCreated_at(long created_at) {
    this.created_at = created_at;
  }
  public File getMedia() {
    return this.media;
  }
  public void setMedia(File media) {
    this.media = media;
    if(media!=null){
    	String path = media.getAbsolutePath();
    	String wechat_path = System.getProperty(WechatSession.WECHATMEDIAURL);
    	if(wechat_path!=null){
    		if(content_type==null){
    			content_type = WechatSessionManager.instance().getContent_type_cache().getContentType(content_type);
    		}
    		try {
				String key = MD5.getMD5(content_type.getBytes());
				setUrl(wechat_path + "/" + key  +"/"+ path.substring(path.lastIndexOf("\\")+1));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
    	}
    }
  }
  
  public String getUrl() {
	return url;
  }
  public void setUrl(String url) {
	this.url = url;
  }

  public String getContent_type() {
	return content_type;
  }

  public void setContent_type(String content_type) {
	this.content_type = content_type;
	if(content_type!=null){
		try {
			String key = MD5.getMD5(content_type.getBytes());
			logger.info(key);
			WechatSessionManager.instance().getContent_type_cache().putContentType(key, content_type);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
  }

  public void setThumb_media_id(String thumb_media_id) {
	this.thumb_media_id = thumb_media_id;
	setMedia_id(thumb_media_id);
  }

  public String getThumb_media_id() {
	return thumb_media_id;
  }
}