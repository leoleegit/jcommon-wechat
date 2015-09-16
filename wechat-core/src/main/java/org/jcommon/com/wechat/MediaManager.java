package org.jcommon.com.wechat;

import java.io.File;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.media.DefaultMediaFactory;
import org.jcommon.com.wechat.media.MediaFactory;

public class MediaManager extends ResponseHandler{
	private Logger logger = Logger.getLogger(getClass());
	private WechatSession session;
	private static MediaFactory media_factory;
	private static final String media_object="media_object";
	
	public MediaManager(WechatSession session){
		this.session = session;
	}
	
	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		if(paramHttpRequest.getAttibute(paramHttpRequest)!=null){
			MediaManagerListener listener = (MediaManagerListener) paramHttpRequest.getAttibute(paramHttpRequest);
			listener.onError(paramError);
		}	
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		if(paramHttpRequest instanceof FileRequest){
			FileRequest request = (FileRequest) paramHttpRequest;
			if(paramHttpRequest.getAttibute(paramHttpRequest)!=null){
				MediaManagerListener listener = (MediaManagerListener) paramHttpRequest.getAttibute(paramHttpRequest);
				Media media                   = (Media) paramHttpRequest.getAttibute(media_object);
				if(listener!=null && media!=null){
					String content_type = request.getContent_type();
					media.setMedia(request.getFile());
					media.setContent_type(content_type);
					listener.onMedia(getMedia_factory().createUrl(media));
				}
			}
		}
	}

	public HttpRequest downloadMedia(Media media, MediaManagerListener listener){
		if(media==null || media.getMedia_id()==null){
			logger.warn("media or media id is null");
			return null;
		}
		File file = getMedia_factory().createEmptyFile(media);
		String media_id = media.getMedia_id();
		HttpRequest request = RequestFactory.downloadMediaRequest(this, session.getApp().getAccess_token(),media_id,file);
		if(listener!=null)
			request.setAttribute(request, listener);
		request.setAttribute(media_object, media);
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
