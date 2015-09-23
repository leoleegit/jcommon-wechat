package org.jcommon.com.wechat;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.BroadcastMessage;
import org.jcommon.com.wechat.data.CustomService;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Image;
import org.jcommon.com.wechat.data.MaterialItem;
import org.jcommon.com.wechat.data.MaterialsCount;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Mpnews;
import org.jcommon.com.wechat.data.Mpvideo;
import org.jcommon.com.wechat.data.Music;
import org.jcommon.com.wechat.data.News;
import org.jcommon.com.wechat.data.OutMessage;
import org.jcommon.com.wechat.data.Text;
import org.jcommon.com.wechat.data.Video;
import org.jcommon.com.wechat.data.filter.Filter;
import org.jcommon.com.wechat.utils.MsgType;

public class MsgManager extends ResponseHandler implements MediaManagerListener{
	private Logger logger = Logger.getLogger(getClass());
	private WechatSession session;
	private MediaManager media_manager;

	private static final String message_object="message_object";
	private static final String request_object="request_object";
	
	public MsgManager(WechatSession session){
		this.session = session;
		this.media_manager = session.getMedia_manager();
	}

	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		if(paramHttpRequest.getAttibute(request_object)!=null){
			HttpRequest request = (HttpRequest) paramHttpRequest.getAttibute(request_object);
			if(request.getListener()!=null)
				request.getListener().onFailure(paramHttpRequest,new StringBuilder(paramError.toJson()));
		}	
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMedia(HttpRequest paramHttpRequest, Media media) {
		// TODO Auto-generated method stub
		if(paramHttpRequest.getAttibute(request_object)!=null){
			HttpRequest request = (HttpRequest) paramHttpRequest.getAttibute(request_object);
			Object      message = paramHttpRequest.getAttibute(message_object);
			if(message!=null){
				OutMessage msg = null;
				if(message instanceof BroadcastMessage){
					msg      = (BroadcastMessage) paramHttpRequest.getAttibute(message_object);
					Media m  = msg.getMedia();
					if(m instanceof Video){
						Mpvideo mv = new Mpvideo((Video)m);
						mv.setMedia_id(media.getMedia_id());
						msg.setMsgtype(MsgType.mpvideo.name());
						((BroadcastMessage)msg).setMpvideo(mv);
						msg.setVideo(null);
						
						HttpRequest upload = RequestFactory.videoMsgRequest(this, session.getApp().getAccess_token(), mv.toJson());
						upload.setAttribute(request_object, request);
						upload.setAttribute(message_object, msg);
						session.execute(request);
						return;
					}
				}else if(message instanceof OutMessage){
					msg      = (OutMessage) paramHttpRequest.getAttibute(message_object);
				}
				if(msg!=null){
					msg.getMedia().setMedia_id(media.getMedia_id());
					request.setContent(msg.toJson());
					session.execute(request);
				}
			}
		}
	}

	@Override
	public void onMpnews(HttpRequest request, Mpnews news) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMaterialItem(HttpRequest request, MaterialItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMaterialsCount(HttpRequest request, MaterialsCount material) {
		// TODO Auto-generated method stub
		
	}

	public HttpRequest sendNews(String touser, News news, RequestCallback callback){
		return sendNews(touser,news,null,callback);
	}
	
	public HttpRequest sendNews(String touser, News news, CustomService customservice, RequestCallback callback){
		OutMessage msg = new OutMessage(MsgType.news,touser);
		msg.setNews(news);
		msg.setCustomservice(customservice);
		return sendMsg(msg,callback);
	}
	
	public HttpRequest sendText(String touser, Text text, RequestCallback callback){
		return sendText(touser,text,null,callback);
	}
	
	public HttpRequest sendText(String touser, Text text, CustomService customservice, RequestCallback callback){
		OutMessage msg = new OutMessage(MsgType.text,touser);
		msg.setText(text);
		msg.setCustomservice(customservice);
		return sendMsg(msg,callback);
	}
	
	public HttpRequest sendImage(String touser, Image image, RequestCallback callback){
		return sendImage(touser,image,null,callback);
	}
	
	public HttpRequest sendImage(String touser, Image image, CustomService customservice, RequestCallback callback){
		OutMessage msg = new OutMessage(MsgType.image,touser);
		msg.setImage(image);
		msg.setCustomservice(customservice);
		if(image.getMedia_id()==null){
			HttpRequest upload = media_manager.uploadMedia(image, this);
			upload.setAttribute(request_object, sendMsg(msg,callback,false));
			upload.setAttribute(message_object, msg);
			return upload;
		}
		return sendMsg(msg,callback);
	}
	
	public HttpRequest sendVideo(String touser, Video video, RequestCallback callback){
		return sendVideo(touser,video,null,callback);
	}
	
	public HttpRequest sendVideo(String touser, Video video, CustomService customservice, RequestCallback callback){
		OutMessage msg = new OutMessage(MsgType.video,touser);
		msg.setVideo(video);
		msg.setCustomservice(customservice);
		if(video.getMedia_id()==null){
			HttpRequest upload = media_manager.uploadMedia(video, this);
			upload.setAttribute(request_object, sendMsg(msg,callback,false));
			upload.setAttribute(message_object, msg);
			return upload;
		}
		return sendMsg(msg,callback);
	}
	
	public HttpRequest sendMusic(String touser, Music music, CustomService customservice, RequestCallback callback){
		OutMessage msg = new OutMessage(MsgType.video,touser);
		msg.setMusic(music);
		msg.setCustomservice(customservice);
		if(music.getMedia_id()==null){
			HttpRequest upload = media_manager.uploadMedia(music, this);
			upload.setAttribute(request_object, sendMsg(msg,callback,false));
			upload.setAttribute(message_object, msg);
			return upload;
		}
		return sendMsg(msg,callback);
	}
	
	public HttpRequest sendMusic(String touser, Music music, RequestCallback callback){
		return sendMusic(touser,music,null,callback);
	}
	
	private HttpRequest sendMsg(OutMessage msg, RequestCallback callback){
		return sendMsg(msg,callback,true);
	}
	
	private HttpRequest sendMsg(OutMessage msg, RequestCallback callback, boolean execute){
		if(msg==null){
			logger.warn("msg is null");
			return null;
		}
		HttpRequest request = RequestFactory.msgReqeust(callback, session.getApp().getAccess_token(),msg.toJson());
		if(execute)
			session.execute(request);
		return request;
	}
	
	public HttpRequest broadcastMpnews(Filter filter, Mpnews mpnews, RequestCallback callback){
		BroadcastMessage msg = new BroadcastMessage(MsgType.mpnews,filter);
		msg.setMpnews(mpnews);
		return broadcastMsg(msg,callback);
	}
	
	public HttpRequest broadcastText(Filter filter, Text text, RequestCallback callback){
		BroadcastMessage msg = new BroadcastMessage(MsgType.text,filter);
		msg.setText(text);
		return broadcastMsg(msg,callback);
	}
	
	public HttpRequest broadcastVideo(Filter filter, Video video, RequestCallback callback){
		BroadcastMessage msg = new BroadcastMessage(MsgType.video,filter);
		msg.setVideo(video);
		if(video.getMedia_id()==null){
			HttpRequest upload = media_manager.uploadMedia(video, this);
			upload.setAttribute(request_object, broadcastMsg(msg,callback,false));
			upload.setAttribute(message_object, msg);
			return upload;
		}
		return broadcastMsg(msg,callback);
	}
	
	public HttpRequest broadcastImage(Filter filter, Image image, RequestCallback callback){
		BroadcastMessage msg = new BroadcastMessage(MsgType.image,filter);
		msg.setImage(image);
		if(image.getMedia_id()==null){
			HttpRequest upload = media_manager.uploadMedia(image, this);
			upload.setAttribute(request_object, broadcastMsg(msg,callback,false));
			upload.setAttribute(message_object, msg);
			return upload;
		}
		return broadcastMsg(msg,callback);
	}
	
	private HttpRequest broadcastMsg(OutMessage msg, RequestCallback callback){
		return broadcastMsg(msg,callback,true);
	}
	
	private HttpRequest broadcastMsg(OutMessage msg, RequestCallback callback, boolean execute){
		if(msg==null){
			logger.warn("msg is null");
			return null;
		}
		HttpRequest request = RequestFactory.broadcastReqeust(callback, session.getApp().getAccess_token(),msg.toJson());
		if(execute)
			session.execute(request);
		return request;
	}
}
