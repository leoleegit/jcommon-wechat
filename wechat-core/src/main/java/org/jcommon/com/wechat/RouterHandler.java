package org.jcommon.com.wechat;

import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Router;

public interface RouterHandler {
	public void onRouter(Router router);
	public void onAccessTokenUpdate(App app);
}
