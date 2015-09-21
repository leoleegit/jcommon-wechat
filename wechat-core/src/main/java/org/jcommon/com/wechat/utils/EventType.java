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

public enum EventType{
  subscribe, 
  unsubscribe, 
  scan, 
  LOCATION, 
  CLICK,
  MASSSENDJOBFINISH,
  VIEW,
  scancode_push,
  scancode_waitmsg,
  pic_sysphoto,
  pic_photo_or_album,
  pic_weixin,
  location_select;

  public static EventType getType(String name) {
    for(EventType et : EventType.class.getEnumConstants()){
    	if(et.name().equalsIgnoreCase(name))
    		return et;
    }
    return null;
  }
}