package org.jcommon.com.wechat;

import java.util.List;

import org.jcommon.com.wechat.data.Router;

public interface RouterHandler {
	public List<String> getWechatIDs();
	public void onRouter(Router router);
	public void stop();
}
