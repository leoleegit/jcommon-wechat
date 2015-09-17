package org.jcommon.com.wechat;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Error;

public interface ErrorListener {
	public void onError(HttpRequest request,Error error);
}
