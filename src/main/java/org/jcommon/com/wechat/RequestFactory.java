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

import java.io.File;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.App;

public class RequestFactory
{
  private static final String api_url = "https://api.weixin.qq.com/cgi-bin";

  public static HttpRequest createUserInfoRequest(HttpListener listener, String access_token, String open_id, String lang){
	    String url = api_url+ "/user/info";
	    String[] keys = { "access_token", "openid", "lang" };
	    String[] values = { access_token, open_id, lang };
	    url = JsonUtils.toRequestURL(url, keys, values);
	    return new HttpRequest(url,listener,true);
  }
  
  public static HttpRequest createMediaUploadRequest(HttpListener listener, String access_token, File file, String type){
    String url = api_url+ "/media/upload";
    String[] keys = { "access_token", "type" };
    String[] values = { access_token, type };
    url = JsonUtils.toRequestURL(url, keys, values);
    return new FileRequest(url, "POST", listener, file);
  }
  
  public static HttpRequest createVideoUploadRequest(HttpListener listener, String access_token, File file, String type){
	    String url = "https://file.api.weixin.qq.com/cgi-bin/media/uploadvideo";
	    String[] keys = { "access_token", "type" };
	    String[] values = { access_token, type };
	    url = JsonUtils.toRequestURL(url, keys, values);
	    return new FileRequest(url, "POST", listener, file);
  }

  public static HttpRequest createMediaDownloaddRequest(HttpListener listener, String access_token, String media_id, File file) {
    String url = api_url+ "/media/get";
    String[] keys = { "access_token", "media_id" };
    String[] values = { access_token, media_id };
    url = JsonUtils.toRequestURL(url, keys, values);
    return new FileRequest(url, "GET", listener, file);
  }

  public static HttpRequest createMsgReqeust(HttpListener listener, String access_token, String content) {
    String url = api_url+ "/message/custom/send";
    String[] keys = { "access_token" };
    String[] values = { access_token };
    url = JsonUtils.toRequestURL(url, keys, values);
    return new HttpRequest(url, content, "POST", listener, true);
  }
  
  public static HttpRequest createNewsUploadRequest(HttpListener listener, String access_token, String content){
	    String url = api_url+ "/media/uploadnews";
	    String[] keys = { "access_token" };
	    String[] values = { access_token };
	    url = JsonUtils.toRequestURL(url, keys, values);
	    return new HttpRequest(url, content, "POST", listener, true);
  }
  
  public static HttpRequest createBroadcastRequest(HttpListener listener, String access_token, String content){
	    String url = api_url+ "/message/mass/send";
	    String[] keys = { "access_token" };
	    String[] values = { access_token };
	    url = JsonUtils.toRequestURL(url, keys, values);
	    return new HttpRequest(url, content, "POST", listener, true);
  }

  public static HttpRequest createNewMenusReqeust(HttpListener listener, String access_token, String content) {
    String url = api_url+ "/menu/create";
    String[] keys = { "access_token" };
    String[] values = { access_token };
    url = JsonUtils.toRequestURL(url, keys, values);
    return new HttpRequest(url, content, "POST", listener, true);
  }

  public static HttpRequest createGetMenusReqeust(HttpListener listener, String access_token) {
    String url = api_url+ "/menu/get";
    String[] keys = { "access_token" };
    String[] values = { access_token };
    url = JsonUtils.toRequestURL(url, keys, values);
    return new HttpRequest(url, listener, true);
  }

  public static HttpRequest createDeleteMenusReqeust(HttpListener listener, String access_token) {
    String url = api_url+ "/menu/delete";
    String[] keys = { "access_token" };
    String[] values = { access_token };
    url = JsonUtils.toRequestURL(url, keys, values);
    return new HttpRequest(url, listener, true);
  }
  
  public static HttpRequest createGetUsersReqeust(HttpListener listener, String access_token, String next_openid) {
	String url = api_url+ "/user/get";
	String[] keys = { "access_token", "next_openid" };
	String[] values = { access_token, next_openid };
	url = JsonUtils.toRequestURL(url, keys, values);
	return new HttpRequest(url, listener, true);
  }
  
  public static HttpRequest createGetGroupsReqeust(HttpListener listener, String access_token) {
	String url = api_url+ "/groups/get";
	String[] keys = { "access_token" };
	String[] values = { access_token };
	url = JsonUtils.toRequestURL(url, keys, values);
	return new HttpRequest(url, listener, true);
  }
  
  public static HttpRequest createGreateGroupsReqeust(HttpListener listener, String access_token, String content) {
	String url = api_url+ "/groups/create";
	String[] keys = { "access_token" };
	String[] values = { access_token };
	url = JsonUtils.toRequestURL(url, keys, values);
	return new HttpRequest(url, content, "POST", listener, true);
  }

  public static HttpRequest createAccessTokenReqeust(HttpListener listener, App app) {
    String url = api_url+ "/token";
    String[] keys = { "grant_type", "appid", "secret" };
    String[] values = { "client_credential", app.getAppid(), app.getSecret() };
    url = JsonUtils.toRequestURL(url, keys, values);
    return new HttpRequest(url, listener, true);
  }
}