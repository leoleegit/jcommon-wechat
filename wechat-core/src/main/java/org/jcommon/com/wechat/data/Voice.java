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

import org.jcommon.com.wechat.utils.MediaType;

public class Voice extends Media
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  public Voice(String data)
  {
    super(data);
    setType(MediaType.voice.toString());
  }
  
  public Voice()
  {
    super();
    setType(MediaType.voice.toString());
  }
  
  public String getType() {
	return MediaType.voice.name();
  }
}