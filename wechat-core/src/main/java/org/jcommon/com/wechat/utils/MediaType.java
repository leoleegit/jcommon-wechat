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

public enum MediaType
{
  image, 
  voice, 
  video, 
  thumb,
  mpnews;

  public static MediaType getType(String name) {
    if (image.toString().equalsIgnoreCase(name))
      return image;
    if (voice.toString().equalsIgnoreCase(name))
      return voice;
    if (video.toString().equalsIgnoreCase(name))
      return video;
    if (thumb.toString().equalsIgnoreCase(name))
      return thumb;
    if (mpnews.toString().equalsIgnoreCase(name))
        return mpnews;
    return null;
  }
}