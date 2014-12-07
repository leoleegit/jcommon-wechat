package org.jcommon.com.wechat;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.GroupFilter;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Mpnews;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.utils.ErrorType;

public class MediaManager extends ResponseHandler{
	private Logger logger = Logger.getLogger(this.getClass());
    
    private WechatSession session;
    
    public void MediaManager(WechatSession session){
    	this.setSession(session);
    }
    
    public FileRequest uploadMedia(RequestCallback callback, File file, String type){
    	FileRequest request = (FileRequest)RequestFactory.createMediaUploadRequest(this, session.getApp().getAccess_token(), file, type);
    	addHandlerObject(request, Media.class);
    	ThreadManager.instance().execute(request);
    	return request;
    }
    
    public void downloadMedia(){
    	
    }
    
	@Override
	public void onError(HttpRequest request, Error error) {
		// TODO Auto-generated method stub
		logger.warn(error.toJson());
	    
	    Object handle = request.getHandler();
	    
	    if ((handle instanceof InMessage)) {
	    	InMessage h = (InMessage)handle;
			if ((request instanceof FileRequest)) {
		        h.setMedia(new Media());
		        session.onMessage(h);       
		    }
	    } else if ((handle instanceof OutMessage)){
	    	HttpRequest msg_re = (HttpRequest) request.getAttibute(WechatSession.RequestCallback);
	    	if(msg_re.getListener()!=null)
	    		msg_re.getListener().onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
	    }
	}

	@Override
	public void onOk(HttpRequest request, Object o) {
		// TODO Auto-generated method stub
		logger.info(o);
		Object handle = request.getHandler();
		
		if ((handle instanceof InMessage)) {
			InMessage h = (InMessage)handle;
			Media m = null;
			User u = null;
			if ((request instanceof FileRequest)) {
		        m = new Media();
		        FileRequest re = (FileRequest)request;
		        
		        m.setContent_type(re.getContent_type());
		        m.setMedia(re.getFile());
		        
		        h.setMedia(m);
		        session.onMessage(h);
		        
		    }
	    } else if ((handle instanceof OutMessage)){
	    	OutMessage out = (OutMessage)handle;
	    	HttpRequest msg_re = (HttpRequest) request.getAttibute(WechatSession.RequestCallback);
	    	String request_action = (String) request.getAttibute(WechatSession.RequestAction);
	    	
	    	if(("sendVoice".equals(request_action) ||
	    			"sendImage".equals(request_action)||
	    			"sendVideo".equals(request_action)) && (o instanceof Media)){
	    		Media m = (Media)o;
	    		out.getImage().setMedia_id(m.getMedia_id());
	    		logger.info("out:" + out.toJson());
	    		msg_re.setContent(out.toJson());
	    		ThreadManager.instance().execute(msg_re);
	    	}else if("sendBroadcast".equals(request_action) && (o instanceof Media)){
	    		Media m = (Media)o;
	    		for(Articles art : out.getArticles())
	    			art.setThumb_media_id(m.getMedia_id());
	    		
	    		OutMessage out_ = new OutMessage();
	    		out_.setArticles(out.getArticles());
	    		out.setArticles(null);
	    		request = RequestFactory.createBroadcastRequest(this, this.app.getAccess_token(), out_.toJson());
	    		request.setHandler(out);
	   	        request.setAttribute(RequestCallback, msg_re);
	   	        request.setAttribute(RequestAction, "UploadArticles");
	   	        addHandlerObject(request, Media.class);
	   	        logger.info("out:" + out_.toJson());
	   	        ThreadManager.instance().execute(request);
	    	}else if("UploadArticles".equals(request_action) && (o instanceof Media)){
	    		Media m = (Media)o;
	       	    Mpnews mp      = new Mpnews();
	       	    mp.setMedia_id(m.getMedia_id());
	       	    out.setMpnews(mp);
	       	    
	       	    List<Group> group_list = Group.getGroups();
	       	    if(group_list!=null){
	       		    GroupFilter gf = new GroupFilter(true,group_list);
	       		    out.setFilter(gf);
	       		    msg_re.setContent(out.toJson());
	       		    logger.info("out:" + out.toJson());
	    		    msg_re.setContent(out.toJson());
	    		    ThreadManager.instance().execute(msg_re);
	       	    }else{
	       	    	request = RequestFactory.createGetGroupsReqeust(this,this.app.getAccess_token());
	       	        request.setHandler(out);
	       	        request.setAttribute(RequestCallback, msg_re);
	       	        request.setAttribute(RequestAction, "GetGroups");
	       	        addHandlerObject(request, Group.class);
	       	        ThreadManager.instance().execute(request);
	       	    }
	    	}else if("GetGroups".equals(request_action) && (o instanceof Group)){
	    		 List<Group> group_list = Group.getGroups();
	        	 if(group_list!=null){
	        		 GroupFilter gf = new GroupFilter(true,group_list);
	        		 out.setFilter(gf);
	        		 msg_re.setContent(out.toJson());
	        		 logger.info("out:" + out.toJson());
		     		 msg_re.setContent(out.toJson());
		     		 ThreadManager.instance().execute(msg_re);
	        	 }else{
	        		 if(msg_re.getListener()!=null)
	        	    	msg_re.getListener().onSuccessful(msg_re, new StringBuilder(new Error("GetGroups fail",-1).toJson()));
	        	 }
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
