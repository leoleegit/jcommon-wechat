package org.jcommon.com.wechat;

import org.jcommon.com.wechat.data.Media;

public interface MediaManagerListener extends ErrorListener{
	public void onMedia(Media media);
}
