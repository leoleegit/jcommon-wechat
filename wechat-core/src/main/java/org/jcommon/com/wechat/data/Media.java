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
import org.jcommon.com.wechat.utils.MsgType;

public class Media extends JsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String media_id;
	private String thumb_media_id;
	private String type;
	private long created_at;
	private File media;
	private String url;
	private String name;
	private String content_type;
	private String media_name;
	private String update_time;
	public final long max_image = 1 * 1024 * 1024; // 1M
	public final long max_voice = 2 * 1024 * 1024; // 2M
	public final long max_video = 10 * 1024 * 1024;// 10M
	public final long max_thumb = 1 * 64 * 1024;// 64K

	public final String type_image = "jpg";
	public final String type_voice = "AMR/MP3";
	public final String type_video = "MP4";
	public final String type_thumb = "jpg";

	public Media(String data) {
		super(data);
	}

	public Media(File media, String type) {
		this.media = media;
		this.type = type;
	}

	public Media() {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
		// setMedia_id(thumb_media_id);
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setMedia(File file) {
		this.media = file;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}

	public String getMedia_name() {
		return media_name;
	}

	public static Media getMedia(Media media) {
		MsgType type = MsgType.getType(media.getType());
		File file = media.getMedia();
		if (type == MsgType.image)
			media = new Image();
		else if (type == MsgType.voice)
			media = new Voice();
		else if (type == MsgType.video)
			media = new Video();
		else if (type == MsgType.mpnews)
			media = new Mpnews();
		else if (type == MsgType.thumb)
			media = new Thumb();
		media.setMedia(file);
		return media;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}