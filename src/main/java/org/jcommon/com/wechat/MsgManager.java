package org.jcommon.com.wechat;


import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.OutMessage;

public class MsgManager extends ResponseHandler{
    private Logger logger = Logger.getLogger(this.getClass());
    
    private WechatSession session;
    private App app;
    
    public MsgManager(WechatSession session){
    	this.setSession(session);
    	this.app = session.getApp();
    }
	
    public HttpRequest sendText(RequestCallback callback, OutMessage msg) {
        HttpRequest msg_re = getMsgRequest(callback, msg);
        session.execute(msg_re);
        return msg_re;
    }
    
    public HttpRequest sendNews(RequestCallback callback, OutMessage msg) {
        HttpRequest msg_re = getMsgRequest(callback, msg);
        session.execute(msg_re);
        return msg_re;
    }
    
    public HttpRequest sendMediaMsg(RequestCallback callback, OutMessage msg){
    	Media media = msg.getMedia();
    	if(media==null){
    		return null;
    	}
    	if(media.getMedia_id()!=null){
    		HttpRequest msg_re = getMsgRequest(callback, msg);
        	session.execute(msg_re);
            return msg_re;
    	}
    	return null;
    }
    
    public HttpRequest broadcastMediaMsg(RequestCallback callback, OutMessage msg){
    	Media media = msg.getMedia();
    	if(media==null){
    		return null;
    	}
    	if(media.getMedia_id()!=null){
    		HttpRequest msg_re = getBroadcastRequest(callback, msg);
        	session.execute(msg_re);
            return msg_re;
    	}
    	return null;
    }
    
    
    public HttpRequest broadcastText(RequestCallback callback, OutMessage msg){
    	HttpRequest msg_re = getBroadcastRequest(callback, msg);
        session.execute(msg_re);
        return msg_re;
    }
    
    public HttpRequest uploadNews(OutMessage message){
    	HttpRequest request = RequestFactory.createNewsUploadRequest(this, app.getAccess_token(), message.toJson());
        addHandlerObject(request, Media.class);
        session.execute(request);
        return request;
    }
    
    public HttpRequest getMsgRequest(RequestCallback callback, OutMessage msg){
    	if (app == null) {
    		this.logger.warn("app can't be null!");
    		return null;
    	}
    	HttpRequest request = RequestFactory.createMsgReqeust(callback, app.getAccess_token(), msg.toJson());
    	return request;
    }
    
    public HttpRequest getBroadcastRequest(RequestCallback callback, OutMessage msg){
    	if (app == null) {
    		this.logger.warn("app can't be null!");
    		return null;
    	}
    	HttpRequest request = RequestFactory.createBroadcastRequest(callback, app.getAccess_token(), msg.toJson());
    	return request;
    }
    
	@Override
	public void onError(HttpRequest request, Error error) {
		// TODO Auto-generated method stub
		logger.warn(error.toJson());
	    
		String request_action = (String) request.getAttibute(WechatSession.RequestAction);
		if("uploadNews".equals(request_action)){
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
		if("uploadNews".equals(request_action)){
			Object handle  = request.getHandler();
			OutMessage msg = (OutMessage)handle;
			msg.setArticles(null);
			HttpRequest msg_re = (HttpRequest) request.getAttibute(WechatSession.RequestCallback);
			Media m = (Media)o;
			msg.getMedia().setMedia_id(m.getMedia_id());
			msg_re.setContent(msg.toJson());
			session.execute(msg_re);
		}
	}

	public WechatSession getSession() {
		return session;
	}

	public void setSession(WechatSession session) {
		this.session = session;
	}

}
