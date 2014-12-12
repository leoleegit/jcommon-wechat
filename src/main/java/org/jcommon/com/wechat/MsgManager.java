package org.jcommon.com.wechat;

import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Image;
import org.jcommon.com.wechat.data.Music;
import org.jcommon.com.wechat.data.News;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.Video;
import org.jcommon.com.wechat.data.Voice;
import org.jcommon.com.wechat.utils.ErrorType;
import org.jcommon.com.wechat.utils.MsgType;

public class MsgManager extends ResponseHandler{
    private Logger logger = Logger.getLogger(this.getClass());
    
    private WechatSession session;
    
    public MsgManager(WechatSession session){
    	this.setSession(session);
    }
	
    public HttpRequest sendText(RequestCallback callback, Text text, String touser) {
        OutMessage msg = new OutMessage(MsgType.text, touser);
        msg.setText(text);
        HttpRequest msg_re = getMsgRequest(callback, msg);
        ThreadManager.instance().execute(msg_re);
        return msg_re;
    }
    
    public HttpRequest sendNews(RequestCallback callback, List<Articles> articles, String touser) throws Exception {
	    if(articles==null || articles.size() > News.max_size){
	    	throw new Exception("articles is null or exceed the articles max size limit.\nmax size:"+News.max_size);
	    }
	    OutMessage msg = new OutMessage(MsgType.news, touser);
	    msg.setNews(new News(articles));
	    HttpRequest msg_re = getMsgRequest(callback, msg);
	    ThreadManager.instance().execute(msg_re);
	    return msg_re;
    }
    
    public HttpRequest sendImage(RequestCallback callback, Image image, String touser) {
        OutMessage msg = new OutMessage(MsgType.image, touser);
        msg.setImage(image);
        HttpRequest msg_re = getMsgRequest(callback, msg);
        if (image.getMedia() != null) {
            FileRequest request = session.getMediaManager().uploadMedia(callback, image.getMedia(), image.getType());

            request.setHandler(msg);
            request.setAttribute(WechatSession.RequestCallback, msg_re);
            request.setAttribute(WechatSession.RequestAction, "sendImage");
        }else{
        	callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
        }
        return msg_re;
    }

    public HttpRequest sendMusic(RequestCallback callback, Music music, String touser) {
        OutMessage msg = new OutMessage(MsgType.music, touser);
        msg.setMusic(music);
        HttpRequest msg_re = getMsgRequest(callback, msg);
        ThreadManager.instance().execute(msg_re);
        return msg_re;
    }

    public HttpRequest sendVideo(RequestCallback callback, Video video, String touser) {
    	OutMessage msg = new OutMessage(MsgType.video, touser);
    	msg.setVideo(video);
        HttpRequest msg_re = getMsgRequest(callback, msg);
    	if (video.getMedia() != null) {
    		FileRequest request = session.getMediaManager().uploadMedia(callback, video.getMedia(), video.getType());

    	    request.setHandler(msg);
    	    request.setAttribute(WechatSession.RequestCallback, msg_re);
    	    request.setAttribute(WechatSession.RequestAction, "sendVideo");
    	}else{
    	    callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
    	}
    	return msg_re;
    }

    public HttpRequest sendVoice(RequestCallback callback, Voice voice, String touser) {
        OutMessage msg = new OutMessage(MsgType.voice, touser);
        msg.setVoice(voice);
        HttpRequest msg_re = getMsgRequest(callback, msg);
        if (voice.getMedia() != null) {
        	FileRequest request = session.getMediaManager().uploadMedia(callback, voice.getMedia(), voice.getType());

    	    request.setHandler(msg);
    	    request.setAttribute(WechatSession.RequestCallback, msg_re);
    	    request.setAttribute(WechatSession.RequestAction, "sendVoice");
    	}else{
    	    callback.onSuccessful(msg_re, new StringBuilder(new Error(ErrorType.error_3).toJson()));
    	}
    	return msg_re;
    }
    
    private HttpRequest getMsgRequest(RequestCallback callback, OutMessage msg){
    	if (this.session.getApp() == null) {
    		this.logger.warn("app can't be null!");
    		return null;
    	}
    	HttpRequest request = RequestFactory.createMsgReqeust(callback, session.getApp().getAccess_token(), msg.toJson());
    	return request;
    }
    
	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		
	}

	public WechatSession getSession() {
		return session;
	}

	public void setSession(WechatSession session) {
		this.session = session;
	}

}
