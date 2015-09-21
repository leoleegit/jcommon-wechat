package org.jcommon.com.wechat;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Menus;

public interface MenusManagerListener extends ErrorListener{
	public void onMenus(HttpRequest request, Menus menus);
}
