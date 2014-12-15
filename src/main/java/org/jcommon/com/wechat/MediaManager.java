package org.jcommon.com.wechat;

import java.io.File;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.utils.ErrorType;

public class MediaManager extends ResponseHandler{
	private Logger logger = Logger.getLogger(this.getClass());
    
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
    	
    	File file   = media.getMedia();
    	String type = media.getType();
    	
    	FileRequest request = (FileRequest)RequestFactory.createMediaUploadRequest(this, app.getAccess_token(), file, type);
    	request.setHandler(message);
        addHandlerObject(request, Media.class);
        session.execute(request);
        return request;
    }
    
    public HttpRequest downloadMedia(InMessage message){
    	 String path = System.getProperty(WechatSession.WECHATMEDIAPATH, System.getProperty("java.io.tmpdir"));
         File file = new File(path);
         if(!file.exists()){
         	logger.info(file.getAbsolutePath() + ":" + file.mkdirs());
         }
         FileRequest request = (FileRequest)RequestFactory.createMediaDownloaddRequest(this, app.getAccess_token(), message.getMediaId(), file);
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
			msg.getMedia().setMedia_id(m.getMedia_id());
			msg_re.setContent(msg.toJson());
			session.execute(msg_re);
		}
		
		
		
//		
//		Object handle = request.getHandler();
//		
//		if ((handle instanceof InMessage)) {
//			
//	    } else if ((handle instanceof OutMessage)){
//	    	OutMessage out = (OutMessage)handle;
//	    	HttpRequest msg_re = (HttpRequest) request.getAttibute(WechatSession.RequestCallback);
//	    	String request_action = (String) request.getAttibute(WechatSession.RequestAction);
//	    	
//	    	if(("sendVoice".equals(request_action) ||
//	    			"sendImage".equals(request_action)||
//	    			"sendVideo".equals(request_action)) && (o instanceof Media)){
//	    		Media m = (Media)o;
//	    		out.getImage().setMedia_id(m.getMedia_id());
//	    		logger.info("out:" + out.toJson());
//	    		msg_re.setContent(out.toJson());
//	    		ThreadManager.instance().execute(msg_re);
//	    	}else if("sendBroadcast".equals(request_action) && (o instanceof Media)){
//	    		Media m = (Media)o;
//	    		for(Articles art : out.getArticles())
//	    			art.setThumb_media_id(m.getMedia_id());
//	    		
//	    		OutMessage out_ = new OutMessage();
//	    		out_.setArticles(out.getArticles());
//	    		out.setArticles(null);
//	    		request = RequestFactory.createBroadcastRequest(this, session.getApp().getAccess_token(), out_.toJson());
//	    		request.setHandler(out);
//	   	        request.setAttribute(WechatSession.RequestCallback, msg_re);
//	   	        request.setAttribute(WechatSession.RequestAction, "UploadArticles");
//	   	        addHandlerObject(request, Media.class);
//	   	        logger.info("out:" + out_.toJson());
//	   	        ThreadManager.instance().execute(request);
//	    	}else if("UploadArticles".equals(request_action) && (o instanceof Media)){
//	    		Media m = (Media)o;
//	       	    Mpnews mp      = new Mpnews();
//	       	    mp.setMedia_id(m.getMedia_id());
//	       	    out.setMpnews(mp);
//	       	    
////	       	    List<Group> group_list = Group.getGroups();
////	       	    if(group_list!=null){
////	       		    GroupFilter gf = new GroupFilter(true,group_list);
////	       		    out.setFilter(gf);
////	       		    msg_re.setContent(out.toJson());
////	       		    logger.info("out:" + out.toJson());
////	    		    msg_re.setContent(out.toJson());
////	    		    ThreadManager.instance().execute(msg_re);
////	       	    }else{
////	       	    	request = RequestFactory.createGetGroupsReqeust(this,session.getApp().getAccess_token());
////	       	        request.setHandler(out);
////	       	        request.setAttribute(WechatSession.RequestCallback, msg_re);
////	       	        request.setAttribute(WechatSession.RequestAction, "GetGroups");
////	       	        addHandlerObject(request, Group.class);
////	       	        ThreadManager.instance().execute(request);
////	       	    }
//	    	}
//	    }
	}

	public WechatSession getSession() {
		return session;
	}

	public void setSession(WechatSession session) {
		this.session = session;
	}

}
