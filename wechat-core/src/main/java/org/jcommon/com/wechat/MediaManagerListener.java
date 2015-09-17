package org.jcommon.com.wechat;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.MaterialsCount;
import org.jcommon.com.wechat.data.Media;

public interface MediaManagerListener extends ErrorListener{
	public void onMedia(HttpRequest request, Media media);
	public void onMaterialsCount(HttpRequest request, MaterialsCount material);
}
