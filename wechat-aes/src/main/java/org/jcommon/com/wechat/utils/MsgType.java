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
package org.jcommon.com.wechat.utils;

public enum MsgType
{
  text, 
  image, 
  music, 
  news, 
  voice, 
  video, 
  location, 
  link, 
  event,
  mpnews,
  thumb;

  public static MsgType getType(String name) {
    if (text.toString().equalsIgnoreCase(name))
      return text;
    if (image.toString().equalsIgnoreCase(name))
      return image;
    if (voice.toString().equalsIgnoreCase(name))
      return voice;
    if (location.toString().equalsIgnoreCase(name))
      return location;
    if (link.toString().equalsIgnoreCase(name))
      return link;
    if (event.toString().equalsIgnoreCase(name))
      return event;
    if (music.toString().equalsIgnoreCase(name))
      return music;
    if (news.toString().equalsIgnoreCase(name))
      return news;
    if (video.toString().equalsIgnoreCase(name))
      return video;
    if (mpnews.toString().equalsIgnoreCase(name))
        return mpnews;
    if (thumb.toString().equalsIgnoreCase(name))
        return thumb;
    return text;
  }
}