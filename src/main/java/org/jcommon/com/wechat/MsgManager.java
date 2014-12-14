package org.jcommon.com.wechat;

import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.FileRequest;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.data.App;
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
    
    public HttpRequest sendImage(RequestCallback callback, OutMessage msg) {
    	HttpRequest msg_re = getMsgRequest(callback, msg);
    	//session.execute(msg_re);
        return msg_re;
    }

    public HttpRequest sendMusic(RequestCallback callback, OutMessage msg) {
    	HttpRequest msg_re = getMsgRequest(callback, msg);
    	//session.execute(msg_re);
        return msg_re;
    }

    public HttpRequest sendVideo(RequestCallback callback, OutMessage msg) {
    	HttpRequest msg_re = getMsgRequest(callback, msg);
    	//session.execute(msg_re);
        return msg_re;
    }

    public HttpRequest sendVoice(RequestCallback callback, OutMessage msg) {
    	HttpRequest msg_re = getMsgRequest(callback, msg);
    	//session.execute(msg_re);
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
