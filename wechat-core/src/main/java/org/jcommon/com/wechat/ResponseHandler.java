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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.collections.MapStore;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.JsonObject;
import org.json.JSONObject;

public abstract class ResponseHandler extends MapStore implements
		RequestCallback {
	private static Logger logger = Logger.getLogger(ResponseHandler.class);

	public abstract void onError(HttpRequest paramHttpRequest, Error paramError);

	public abstract void onOk(HttpRequest paramHttpRequest, Object paramObject);

	public void addHandlerObject(HttpRequest request, Class<?> type) {
		if (!super.addOne(request, type))
			super.updateOne(request, type);
	}

	private Class<?> getHandlerObject(HttpRequest request) {
		Object o = super.removeOne(request);
		return o != null ? (Class<?>) o : null;
	}

	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult) {
		logger.info(sResult.toString());
		String result = sResult.toString();

		JSONObject reObj = JsonUtils.getJSONObject(result);
		Class<?> type = getHandlerObject(reqeust);
		logger.info("handler:" + type);
		Error error_e = new Error("{errcode:\"-1\";errmsg:\"\"}");
		if (reObj != null) {
			if (reObj.has("errcode")) {
				Error error = new Error(result);
				if (org.jcommon.com.wechat.utils.ErrorType.error0.error == error
						.getErrcode()) {
					onOk(reqeust, result);
					return;
				}
				onError(reqeust, error);
				return;
			}
			if (type != null && JsonObject.class.isAssignableFrom(type)) {
				try {
					Object args = newInstance(type, result);
					onOk(reqeust, args);
					return;
				} catch (SecurityException e) {
					logger.warn(e);
					error_e.setErrmsg(e.getMessage());
				} catch (IllegalArgumentException e) {
					logger.warn(e);
					error_e.setErrmsg(e.getMessage());
				}
			} else {
				onOk(reqeust, result);
			}
		} else {
			onOk(reqeust, result);
			return;
		}
		if ((error_e.getErrmsg() != null) && (!"".equals(error_e.getErrmsg())))
			onError(reqeust, error_e);
	}

	public void onFailure(HttpRequest reqeust, StringBuilder sResult) {
		logger.warn(sResult.toString());
		String result = sResult.toString();

		JSONObject reObj = JsonUtils.getJSONObject(result);
		Error error_e = new Error("{errcode:\"-1\";errmsg:\"\"}");
		if (reObj != null) {
			if (reObj.has("errcode")) {
				Error error = new Error(result);
				onError(reqeust, error);
				return;
			}
			error_e.setErrmsg(result);
			onError(reqeust, error_e);
		} else {
			error_e.setErrmsg(result);
			onError(reqeust, error_e);
		}
	}

	public void onTimeout(HttpRequest reqeust) {
		logger.error("timeout");
		super.removeOne(reqeust);
		Error error_e = new Error("{errcode:\"-1\";errmsg:\"\"}");
		error_e.setErrmsg("timeout");
		onError(reqeust, error_e);
	}

	public void onException(HttpRequest reqeust, Exception e) {
		logger.error("", e);
		super.removeOne(reqeust);
		Error error_e = new Error("{errcode:\"-1\";errmsg:\"\"}");
		error_e.setErrmsg(e.getMessage());
		onError(reqeust, error_e);
	}

	private static Object newInstance(Class<?> class_, String args) {
		try {
			Class<?>[] par = { String.class };
			Constructor<?> con = class_.getConstructor(par);
			Object[] objs = { args };
			return con.newInstance(objs);
		} catch (SecurityException e) {
			logger.warn(e);
		} catch (IllegalArgumentException e) {
			logger.warn(e);
		} catch (NoSuchMethodException e) {
			logger.warn(e);
		} catch (InstantiationException e) {
			logger.warn(e);
		} catch (IllegalAccessException e) {
			logger.warn(e);
		} catch (InvocationTargetException e) {
			logger.warn(e);
		}
		return null;
	}
}