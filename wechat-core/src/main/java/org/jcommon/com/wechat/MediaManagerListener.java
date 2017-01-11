package org.jcommon.com.wechat;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.MaterialItem;
import org.jcommon.com.wechat.data.MaterialsCount;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Mpnews;

public interface MediaManagerListener extends ErrorListener {
	public void onMedia(HttpRequest request, Media media);

	public void onMpnews(HttpRequest request, Mpnews news);

	public void onMaterialItem(HttpRequest request, MaterialItem item);

	public void onMaterialsCount(HttpRequest request, MaterialsCount material);
}
