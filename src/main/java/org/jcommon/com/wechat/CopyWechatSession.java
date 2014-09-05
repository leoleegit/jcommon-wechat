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
package org.jcommon.com.wechat;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;

public class CopyWechatSession extends WechatSession {
	private String callback;
	
	public CopyWechatSession(String wechatID, App app,
			WechatSessionListener listener) {
		super(wechatID, app, listener);
		// TODO Auto-generated constructor stub
	}

	public CopyWechatSession(String wechat_key, String callback){
		super(wechat_key,null,null);
		this.callback = callback;
	}
	
	public void onEvent(Event event){
		logger.info(event.getXml());
		callback(event!=null?event.getXml():null);
	}
	
	public void onMessage(InMessage message){
		logger.info(message.getXml());
		callback(message!=null?message.getXml():null);
	}
	
	private void callback(String xml){
		logger.info(xml);
		if(callback!=null){
			HttpRequest request = new HttpRequest(callback,xml,"POST",this);
			ThreadManager.instance().execute(request);
		}
	}
	
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
	}
	
	public void onFailure(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
	}
}
