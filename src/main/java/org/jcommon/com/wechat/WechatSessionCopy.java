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


import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.InMessage;

public class WechatSessionCopy extends WechatSessionMontior {
	private String callback;
	private String mywechat_id;

	public WechatSessionCopy(String wechat_key, String callback, String Token){
		super(wechat_key,new App(null,null,Token),null);
		this.callback = callback;
		mywechat_id = wechat_key!=null && wechat_key.indexOf("-")!=-1?wechat_key.substring(0, wechat_key.lastIndexOf("-")):wechat_key;
		logger.info(callback);
	}
	
	public void onEvent(Event event){
		logger.info(event.getXml());
		String touser = event.getToUserName();
		if(mywechat_id!=null && mywechat_id.equals(touser))
			callback(event!=null?event.getXml():null);
	}
	
	public void onMessage(InMessage message){
		logger.info(message.getXml());
		String touser = message.getToUserName();
		if(mywechat_id!=null && mywechat_id.equals(touser))
			callback(message!=null?message.getXml():null);
	}
	
	private void callback(String xml){
		logger.info(xml);
		if(callback!=null){
			String[] keys   = { "signature","timestamp","nonce" };
			String[] values = { signature,timestamp,nonce };
			String    url   = JsonUtils.toRequestURL(callback, keys, values);
			HttpRequest request = new HttpRequest(url,xml,"POST",this);
			ThreadManager.instance().execute(request);
		}
	}
	
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
	}
	
	public void onFailure(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
	}
	
	public void onTimeout(HttpRequest request){
	    logger.error(callback);
	}

	public void onException(HttpRequest request, Exception e){
	    logger.error(callback, e);
	}
	
	String signature; String timestamp; String nonce;
	public boolean appVerify(String signature, String timestamp, String nonce){
		if(super.appVerify(signature, timestamp, nonce)){
			this.signature = signature;
			this.timestamp = timestamp;
			this.nonce     = nonce;
			return true;
		}
		return false;
	}
}
