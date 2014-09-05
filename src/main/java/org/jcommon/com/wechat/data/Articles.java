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

public class Articles extends JsonObject
{
  private String title;
  private String description;
  private String url;
  private String picurl;

  public Articles(String title, String description, String url, String picurl)
  {
    this.title = title;
    this.description = description;
    this.url = url;
    this.picurl = picurl;
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
}