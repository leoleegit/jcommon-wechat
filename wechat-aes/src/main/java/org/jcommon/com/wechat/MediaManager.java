package org.jcommon.com.wechat;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.utils.MsgType;

public class MediaManager extends ResponseHandler{
	private Logger logger = Logger.getLogger(this.getClass());
    
	public final static String  MEDIA = "MEDIA";
	public final static String  MULTIMEDIA   = "MULTIMEDIA";
	  
    private WechatSession session;
    private App app;
    
    public MediaManager(WechatSession session){
    	this.setSession(session);
    	this.app = session.getApp();
    }
    
    public FileRequest uploadMedia(OutMessage message){
    	Media media = message.getMedia();
    	if(media==null){
    		return null;
    	}
    	
    	if(media.getMedia_id()!=null || media.getThumb_media_id()!=null)
    		return null;
    	
    	File file   = media.getMedia();
    	String type = media.getType();
    	
    	FileRequest request = (FileRequest)RequestFactory.createMediaUploadRequest(this, app.getAccess_token(), file, type);
    	request.setHandler(message);
        addHandlerObject(request, Media.class);
        session.execute(request);
        return request;
    }
    
    public FileRequest uploadMultiMedia(List<?> medias){
    	for(Object o : medias){
    		Media media = (Media) o;
    		OutMessage out = new OutMessage(MsgType.getType(media.getType()),null);
    		Media copy = Media.getMedia(media);
    		copy.setMedia_id(media.getMedia_id());
    		copy.setThumb_media_id(media.getThumb_media_id());
    		out.setMedia(copy);
    		FileRequest request = uploadMedia(out);
    		if(request!=null){
    			request.setAttribute(MEDIA, media);
        		request.setAttribute(MULTIMEDIA, medias);
        		return request;
    		}
    	}
        return null;
    }
    
    public HttpRequest downloadMedia(InMessage message){
    	 String path = System.getProperty(WechatSession.WECHATMEDIAPATH, System.getProperty("java.io.tmpdir"));
         File file = new File(path);
         if(!file.exists()){
         	logger.info(file.getAbsolutePath() + ":" + file.mkdirs());
         }
         FileRequest request = (FileRequest)RequestFactory.createMediaDownloadRequest(this, app.getAccess_token(), message.getMediaId(), file);
         request.setHandler(message);
         addHandlerObject(request, Media.class);
         session.execute(request);
         return request;
    }
    
	@Override
	public void onError(HttpRequest request, Error error) {
		// TODO Auto-generated method stub
		logger.warn(error.toJson());
	    
		String request_action = (String) request.getAttibute(WechatSession.RequestAction);
		if("onMessage".equals(request_action)){
			Object handle = request.getHandler();
			InMessage h = (InMessage)handle;
			Media m = null;
			if ((request instanceof FileRequest)) {
		        m = new Media();
		        h.setMedia(m);
		        session.onMessage(h); 
		    }
		}else if("sendMediaMsg".equals(request_action)){
			HttpRequest msg_re = (HttpRequest) request.getAttibute(WechatSession.RequestCallback);
			if(msg_re.getListener()!=null)
				msg_re.getListener().onSuccessful(msg_re, new StringBuilder(error.toJson()));
		}
	}

	@Override
	public void onOk(HttpRequest request, Object o) {
		// TODO Auto-generated method stub
		logger.info(o);
		String request_action = (String) request.getAttibute(WechatSession.RequestAction);
		if("onMessage".equals(request_action)){
			Object handle = request.getHandler();
			InMessage msg = (InMessage)handle;
			Media m = null;
			if ((request instanceof FileRequest)) {
		        m = new Media();
		        FileRequest re = (FileRequest)request;
		        
		        m.setContent_type(re.getContent_type());
		        m.setMedia(re.getFile());
		        
		        msg.setMedia(m);
		        session.onMessage(msg);   
		    }
		}else if("sendMediaMsg".equals(request_action)){
			Object handle  = request.getHandler();
			OutMessage msg = (OutMessage)handle;
			HttpRequest msg_re = (HttpRequest) request.getAttibute(WechatSession.RequestCallback);
			Media m = (Media)o;
			if(m.getMedia_id()!=null)
				msg.getMedia().setMedia_id(m.getMedia_id());
			else if(m.getThumb_media_id()!=null)
				msg.getMedia().setThumb_media_id(m.getThumb_media_id());
			msg_re.setContent(msg.toJson());
			session.execute(msg_re);
		}else if("uploadMultiMedia".equals(request_action)){
			Media media = (Media) request.getAttibute(MEDIA);
			List<?> medias = (List<?>) request.getAttibute(MULTIMEDIA);
			Media m = (Media)o;
			if(m.getMedia_id()!=null)
				media.setMedia_id(m.getMedia_id());
			else if(m.getThumb_media_id()!=null)
				media.setThumb_media_id(m.getThumb_media_id());
			Object handle  = request.getHandler();
			OutMessage msg = (OutMessage)handle;
			HttpRequest msg_re = (HttpRequest) request.getAttibute(WechatSession.RequestCallback);
			
			HttpRequest request_ = uploadMultiMedia(medias);
			if(request_!=null){
				request_.setHandler(msg);
		        request_.setAttribute(WechatSession.RequestCallback, msg_re);
		        request_.setAttribute(WechatSession.RequestAction, "uploadMultiMedia");
	        }else{
	        	session.onOk(request, medias);
	        }
		}
	}

	public WechatSession getSession() {
		return session;
	}

	public void setSession(WechatSession session) {
		this.session = session;
	}

}
