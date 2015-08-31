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
package org.jcommon.com.wechat.cache;

import org.jcommon.com.util.collections.MapStore;
import org.jcommon.com.wechat.utils.TempFileCache;

public class ContentTypeCache extends MapStore {
	private String file_path;
	private String prefix;
	private String suffix;
	private long length;
	
	public ContentTypeCache(String file_path, String prefix, String suffix, long length){
		super();
		this.file_path = file_path;
		this.prefix    = prefix;
		this.suffix    = suffix;
		this.length    = length;
		org.jcommon.com.util.system.SystemManager.instance().addListener(this);
	}
	
	public void startup(){
		TempFileCache.loadCache(file_path, getAll(), prefix, suffix);
		super.startup();
	}
	
	public void shutdown(){
		TempFileCache.saveCache(file_path, getAll(), prefix, suffix, length);
		super.shutdown();
	}
	
	public String getContentType(String key){
		if(super.hasKey(key))
			return (String) super.getOne(key);
		return "application/unknown";
	}
	
	public void putContentType(String key, String value){
		if(super.hasKey(key))
			return;
		super.addOne(key, value);
	}
	
	public String removeContentType(String key){
		if(super.hasKey(key))
			return (String) super.removeOne(key);
		return null;
	}
}
