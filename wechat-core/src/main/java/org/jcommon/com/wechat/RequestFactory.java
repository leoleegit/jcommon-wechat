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
import org.jcommon.com.wechat.utils.Lang;

public class RequestFactory {
	public static final String api_root = "https://api.weixin.qq.com";
	public static final String api_url = "https://api.weixin.qq.com/cgi-bin";
	public static final String mp_url = "https://mp.weixin.qq.com/cgi-bin";
	public static final String file_url = "https://file.api.weixin.qq.com/cgi-bin";

	public static HttpRequest uploadHeadImgRequest(HttpListener listener,
			String access_token, File file, String kf_account) {
		String url = api_root + "/customservice/kfaccount/uploadheadimg";
		String[] keys = { "access_token", "kf_account" };
		String[] values = { access_token, kf_account };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new FileRequest(url, file, HttpRequest.POST, listener);
	}

	public static HttpRequest addAgentRequest(HttpListener listener,
			String access_token, String content) {
		String url = api_root + "/customservice/kfaccount/add";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest updateAgentRequest(HttpListener listener,
			String access_token, String content) {
		String url = api_root + "/customservice/kfaccount/update";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest delAgentRequest(HttpListener listener,
			String access_token, String content) {
		String url = api_root + "/customservice/kfaccount/del";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest getAgentRequest(HttpListener listener,
			String access_token) {
		String url = api_url + "/customservice/getkflist";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest msgReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/message/custom/send";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest broadcastReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/message/mass/sendall";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest videoMsgRequest(HttpListener listener,
			String access_token, String content) {
		String url = file_url + "/media/uploadvideo";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest createNewsUploadRequest(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/media/uploadnews";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest getMenusReqeust(HttpListener listener,
			String access_token) {
		String url = api_url + "/menu/get";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest delMenusReqeust(HttpListener listener,
			String access_token) {
		String url = api_url + "/menu/delete";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest createMenusReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/menu/create";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest userInfoRequest(HttpListener listener,
			String access_token, String open_id, Lang lang) {
		String url = api_url + "/user/info";
		String[] keys = { "access_token", "openid", "lang" };
		String[] values = { access_token, open_id, lang.name() };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest userInfosRequest(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/user/info/batchget";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest updateRemarkRequest(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/user/info/updateremark";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest getUsersReqeust(HttpListener listener,
			String access_token, String next_openid) {
		String url = api_url + "/user/get";
		String[] keys = { "access_token", "next_openid" };
		String[] values = { access_token, next_openid };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest getGroupsReqeust(HttpListener listener,
			String access_token) {
		String url = api_url + "/groups/get";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest getGroupsByUserReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/groups/getid";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest createGroupsReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/groups/create";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest updateGroupsReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/groups/update";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest moveGroupsReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/groups/members/batchupdate";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest delGroupsReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/groups/delete";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest accessTokenReqeust(HttpListener listener, App app) {
		String url = api_url + "/token";
		String[] keys = { "grant_type", "appid", "secret" };
		String[] values = { "client_credential", app.getAppid(),
				app.getSecret() };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}
	
	public static HttpRequest userTokenReqeust(HttpListener listener, App app, String code) {
		String url = api_root + "/sns/oauth2/access_token";
		String[] keys = { "grant_type", "appid", "secret", "code" };
		String[] values = { "authorization_code", app.getAppid(),
				app.getSecret(),code };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest callbackIpsReqeust(HttpListener listener,
			String access_token) {
		String url = api_url + "/getcallbackip";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest jsapi_ticketRequest(HttpListener listener,
			String access_token) {
		String url = api_url + "/ticket/getticket";
		String[] keys = { "access_token", "type" };
		String[] values = { access_token, "jsapi" };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest getMaterialReqeust(HttpListener listener,
			String access_token, String content, File file) {
		String url = api_url + "/material/get_material";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest getMaterialsReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/material/batchget_material";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest delMaterialReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/material/del_material";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest updateMaterialReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/material/update_news";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest createQrcodeReqeust(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/qrcode/create";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}

	public static HttpRequest getMaterialCountReqeust(HttpListener listener,
			String access_token) {
		String url = api_url + "/material/get_materialcount";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, listener, true);
	}

	public static HttpRequest downloadMediaRequest(HttpListener listener,
			String access_token, String media_id, File file) {
		String url = api_url + "/media/get";
		String[] keys = { "access_token", "media_id" };
		String[] values = { access_token, media_id };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new FileRequest(url, file, listener);
	}

	public static HttpRequest uploadMediaRequest(HttpListener listener,
			String access_token, File file, String type) {
		String url = api_url + "/media/upload";
		String[] keys = { "access_token", "type" };
		String[] values = { access_token, type };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new FileRequest(url, file, "media", listener);
	}

	public static HttpRequest uploadImgRequest(HttpListener listener,
			String access_token, File file) {
		String url = api_url + "/media/uploadimg";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new FileRequest(url, file, "media", listener);
	}

	public static HttpRequest uploadMaterialMediaRequest(HttpListener listener,
			String access_token, File file, String type, String description) {
		String url = api_url + "/material/add_material";
		String[] keys = { "access_token", "type" };
		String[] values = { access_token, type };
		url = JsonUtils.toRequestURL(url, keys, values);
		if (description != null) {
			FileRequest request = new FileRequest(url, file, "description",
					listener);
			request.addHeader("description", description);
			return request;
		}
		return new FileRequest(url, file, "media", listener);
	}

	public static HttpRequest uploadNewsRequest(HttpListener listener,
			String access_token, String content) {
		String url = api_url + "/material/add_news";
		String[] keys = { "access_token" };
		String[] values = { access_token };
		url = JsonUtils.toRequestURL(url, keys, values);
		return new HttpRequest(url, content, HttpRequest.POST, listener, true);
	}
}