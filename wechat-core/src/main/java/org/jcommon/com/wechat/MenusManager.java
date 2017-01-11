package org.jcommon.com.wechat;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Menus;

public class MenusManager extends ResponseHandler {
	private WechatSession session;

	public MenusManager(WechatSession session) {
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
			MenusManagerListener listener = (MenusManagerListener) paramHttpRequest
					.getAttibute(paramHttpRequest);
			if (listener != null) {
				if (paramObject instanceof Menus) {
					Menus media_return = (Menus) paramObject;
					listener.onMenus(paramHttpRequest, media_return);
				}
			}
		}
	}

	public HttpRequest getMenus(MenusManagerListener listener) {
		HttpRequest request = RequestFactory.getMenusReqeust(this, session
				.getApp().getAccess_token());
		if (listener != null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, Menus.class);
		session.execute(request);
		return request;
	}

	public HttpRequest delMenus(RequestCallback callback) {
		HttpRequest request = RequestFactory.delMenusReqeust(callback, session
				.getApp().getAccess_token());
		session.execute(request);
		return request;
	}

	public HttpRequest createMenus(Menus menus, RequestCallback callback) {
		HttpRequest request = RequestFactory.createMenusReqeust(callback,
				session.getApp().getAccess_token(), menus.toJson());
		session.execute(request);
		return request;
	}
}
