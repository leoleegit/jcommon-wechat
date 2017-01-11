package org.jcommon.com.wechat;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Qrcode;

public interface QrcodeManagerListener extends ErrorListener {
	public void onQrcode(HttpRequest request, Qrcode qrcode);
}
