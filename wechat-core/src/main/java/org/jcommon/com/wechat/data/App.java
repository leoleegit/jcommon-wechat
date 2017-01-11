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

import org.jcommon.com.wechat.utils.WechatUtils;

public class App extends JsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static long default_delay = 3000L;
	public final static long default_expires = 7200 * 1000;
	private String access_token;
	private String appid;
	private String secret;
	private String Token;
	private long expires = default_expires;
	private long delay = default_delay;
	private String start_time;
	private String status = "not ready";

	public App(String appid, String secret, String Token) {
		this.appid = appid;
		this.secret = secret;
		this.Token = Token;
	}

	public App(String access_token, String appid, String secret, String Token) {
		this.access_token = access_token;
		this.appid = appid;
		this.secret = secret;
		this.Token = Token;
	}

	public String getAccess_token() {
		return this.access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
		setStart_time(WechatUtils.getDate());
	}

	public String getAppid() {
		return this.appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getToken() {
		return this.Token;
	}

	public void setToken(String token) {
		this.Token = token;
	}

	public long getExpires() {
		return this.expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public long getDelay() {
		return delay;
	}
}