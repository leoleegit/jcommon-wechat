package org.jcommon.com.wechat;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.ActionInfo;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Qrcode;

public class QrcodeManager extends ResponseHandler {
	private Logger logger = Logger.getLogger(getClass());
	private WechatSession session;
	private Qrcode qrcode;

	public QrcodeManager(WechatSession session) {
		this.session = session;
	}

	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		if (paramHttpRequest.getAttibute(paramHttpRequest) != null) {
			MediaManagerListener listener = (MediaManagerListener) paramHttpRequest
					.getAttibute(paramHttpRequest);
			listener.onError(paramHttpRequest, paramError);
		}
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		if (paramHttpRequest.getAttibute(paramHttpRequest) != null) {
			QrcodeManagerListener listener = (QrcodeManagerListener) paramHttpRequest
					.getAttibute(paramHttpRequest);
			if (listener != null) {
				if (paramObject instanceof Qrcode) {
					qrcode = (Qrcode) paramObject;
					listener.onQrcode(paramHttpRequest, qrcode);
				}
			}
		}
	}

	public HttpRequest createQrcode(ActionInfo info,
			QrcodeManagerListener listener) {
		if (info == null) {
			logger.warn("info is null");
			return null;
		}

		HttpRequest request = RequestFactory.createQrcodeReqeust(this, session
				.getApp().getAccess_token(), info.toJson());
		if (listener != null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, Qrcode.class);
		session.execute(request);
		return request;
	}
}
