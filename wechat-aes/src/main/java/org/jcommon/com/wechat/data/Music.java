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

public class Music extends Media
{
  private String title;
  private String description;
  private String musicurl;
  private String hqmusicurl;
  private String thumb_media_id;

  public Music(String data)
  {
    super(data);
    setMedia_id(this.thumb_media_id);
  }
  
  public Music(String title,String description)
  {
    super();
    setMedia_id(this.thumb_media_id);
    setTitle(title);
    setDescription(description);
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

  public String getMusicurl() {
    return this.musicurl;
  }

  public void setMusicurl(String musicurl) {
    this.musicurl = musicurl;
  }

  public String getHqmusicurl() {
    return this.hqmusicurl;
  }

  public void setHqmusicurl(String hqmusicurl) {
    this.hqmusicurl = hqmusicurl;
  }

  public String getThumb_media_id() {
    return this.thumb_media_id;
  }

  public void setThumb_media_id(String thumb_media_id) {
    this.thumb_media_id = thumb_media_id;
  }
}