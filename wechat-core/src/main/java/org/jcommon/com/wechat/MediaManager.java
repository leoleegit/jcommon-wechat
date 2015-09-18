package org.jcommon.com.wechat;

import java.io.File;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Image;
import org.jcommon.com.wechat.data.MaterialsCount;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Thumb;
import org.jcommon.com.wechat.data.Video;
import org.jcommon.com.wechat.data.Voice;
import org.jcommon.com.wechat.data.format.Materials;
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
			listener.onError(paramHttpRequest,paramError);
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
				
				if(listener!=null){
					if(paramObject instanceof Media){
						Media media_return = (Media) paramObject;
						listener.onMedia(paramHttpRequest,media_return);
					}else if(media!=null){
						String content_type = request.getContent_type();
						media.setMedia(request.getFile());
						media.setContent_type(content_type);
						listener.onMedia(paramHttpRequest,getMedia_factory().createUrl(media));
					}
				}
				
			}
		}else{
			if(paramHttpRequest.getAttibute(paramHttpRequest)!=null){
				MediaManagerListener listener = (MediaManagerListener) paramHttpRequest.getAttibute(paramHttpRequest);
				if(listener!=null){
					if(paramObject instanceof MaterialsCount){
						MaterialsCount media_return = (MaterialsCount) paramObject;
						listener.onMaterialsCount(paramHttpRequest,media_return);
					}
				}
			}
		}
	}
	
	public HttpRequest getMaterials(String type, int offset, int count, MediaManagerListener listener){
		if(count<=0 || count>20)
			count = 20;
		
		HttpRequest request = RequestFactory.getMaterialsReqeust(this, session.getApp().getAccess_token(),new Materials(type,offset,count).toJson());
		if(listener!=null)
			request.setAttribute(request, listener);
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}
	
	public HttpRequest getMaterialCount(MediaManagerListener listener){	
		HttpRequest request = RequestFactory.getMaterialCountReqeust(this, session.getApp().getAccess_token());
		if(listener!=null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, MaterialsCount.class);
		logger.info(request.getUrl());
		session.execute(request);
		return request;
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
		session.execute(request);
		return request;
	}
	
	public HttpRequest uploadMedia(Media media, MediaManagerListener listener){
		if(media==null || media.getType()==null){
			logger.warn("media or media type is null");
			return null;
		}
		File file = media.getMedia();
		if(file==null)
			file = getMedia_factory().getMediaFromUrl(media.getUrl()).getMedia();
		if(file==null){
			logger.warn("media file is null");
			return null;
		}
		String media_type = media.getType();
		HttpRequest request = RequestFactory.uploadMediaRequest(this, session.getApp().getAccess_token(),file,media_type);
		if(listener!=null)
			request.setAttribute(request, listener);
		request.setAttribute(media_object, media);
		super.addHandlerObject(request, Media.class);
		logger.info(file.getAbsoluteFile());
		session.execute(request);
		return request;
	}
	
	public HttpRequest uploadMaterialMedia(Media media, String description, MediaManagerListener listener){
		if(media==null || media.getType()==null){
			logger.warn("media or media type is null");
			return null;
		}
		File file = media.getMedia();
		if(file==null)
			file = getMedia_factory().getMediaFromUrl(media.getUrl()).getMedia();
		if(file==null){
			logger.warn("media file is null");
			return null;
		}
		String media_type = media.getType();
		HttpRequest request = RequestFactory.uploadMaterialMediaRequest(this, session.getApp().getAccess_token(),file,media_type,description);
		if(listener!=null)
			request.setAttribute(request, listener);
		request.setAttribute(media_object, media);
		super.addHandlerObject(request, Media.class);
		logger.info(description);
		session.execute(request);
		return request;
	}
	
	public HttpRequest uploadMaterialMedia(Image image, MediaManagerListener listener){
		return uploadMaterialMedia(image,null,listener);
	}
	
	public HttpRequest uploadMaterialMedia(Voice voice, MediaManagerListener listener){
		return uploadMaterialMedia(voice,null,listener);
	}
	
	public HttpRequest uploadMaterialMedia(Thumb thumb, MediaManagerListener listener){
		return uploadMaterialMedia(thumb,null,listener);
	}
	
	public HttpRequest uploadMaterialMedia(Video video, MediaManagerListener listener){
		if(video==null){
			logger.warn("media is null");
			return null;
		}
		return uploadMaterialMedia(video,video.toJson(),listener);
	}
	
	public HttpRequest uploadNewsImg(Media media, MediaManagerListener listener){
		if(media==null){
			logger.warn("media is null");
			return null;
		}
		File file = media.getMedia();
		if(file==null)
			file = getMedia_factory().getMediaFromUrl(media.getUrl()).getMedia();
		if(file==null){
			logger.warn("media file is null");
			return null;
		}
		HttpRequest request = RequestFactory.uploadImgRequest(this, session.getApp().getAccess_token(),file);
		if(listener!=null)
			request.setAttribute(request, listener);
		request.setAttribute(media_object, media);
		super.addHandlerObject(request, Media.class);
		logger.info(file.getAbsoluteFile());
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
