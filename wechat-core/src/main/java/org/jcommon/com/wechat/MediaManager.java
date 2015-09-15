package org.jcommon.com.wechat;

import java.io.File;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.media.DefaultMediaFactory;
import org.jcommon.com.wechat.media.MediaFactory;

public class MediaManager extends ResponseHandler{
	private Logger logger = Logger.getLogger(getClass());
	private WechatSession session;
	private static MediaFactory media_factory;
	
	public MediaManager(WechatSession session){
		this.session = session;
	}
	
	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		
	}

	public HttpRequest downloadMedia(Media media, MediaManagerListener listener){
		if(media==null || media.getMedia_id()==null){
			logger.warn("media or media id is null");
			return null;
		}
		File file = getMedia_factory().createEmptyFile(media);
		String media_id = media.getMedia_id();
		HttpRequest request = RequestFactory.downloadMediaRequest(this, session.getApp().getAccess_token(),media_id,file);
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	public static void setMedia_factory(MediaFactory media_factory) {
		MediaManager.media_factory = media_factory;
	}

	public static MediaFactory getMedia_factory() {
		if(media_factory==null){
			media_factory = new DefaultMediaFactory();
		}
		return media_factory;
	}
}
