package org.jcommon.com.wechat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.cache.SessionCache;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Encrypt;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.InMessage;
import org.jcommon.com.wechat.data.MaterialItem;
import org.jcommon.com.wechat.data.MaterialsCount;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Mpnews;
import org.jcommon.com.wechat.data.Token;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.data.Users;
import org.jcommon.com.wechat.utils.MediaType;
import org.jcommon.com.wechat.utils.MsgType;
import org.jcommon.com.wechat.utils.WechatUtils;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

public class WechatSession implements WechatSessionListener,
	MediaManagerListener,
	UserManagerListener{
	
	protected Logger logger = Logger.getLogger(getClass());
	protected static final String INMESSAGE = "InMessage";
	private WechatSessionListener listener;
	private String wechatID;
	private App app; 
	
	private AppManager   app_manager;
	private AgentManager agent_manager;
	private MediaManager media_manager;
	private MenusManager menus_manager;
	private MsgManager   msg_manager; 
	private QrcodeManager qrcode_manager;
	private UserManager  user_manager;
	private WXBizMsgCrypt msg_crypt;
	 
	public WechatSession(String wechatID, App app, WechatSessionListener listener){
		this.app      = app;
		this.wechatID = wechatID;
	    this.listener = listener;
	    
	    app_manager   = new AppManager(this);
	    agent_manager = new AgentManager(this);
	    media_manager = new MediaManager(this);
	    menus_manager = new MenusManager(this);
	    msg_manager   = new MsgManager(this);
	    qrcode_manager= new QrcodeManager(this);
	    user_manager  = new UserManager(this);
	}
	
	public WechatSession(WXBizMsgCrypt msg_crypt, String wechatID, App app, WechatSessionListener listener){
		this(wechatID,app,listener);
		this.msg_crypt = msg_crypt;
	}
	
	public void startup(){
		app_manager.startup();
		user_manager.startup();
		SessionCache.instance().addWechatSession(this);
		logger.info(logStr(""));
	}
	
	public void shutdown(){
		app_manager.shutdown();
		user_manager.shutdown();
		SessionCache.instance().removeWechatSession(this);
		logger.info(logStr(""));
	}

	public WechatSessionListener getListener() {
		return listener;
	}

	public void setListener(WechatSessionListener listener) {
		this.listener = listener;
	}

	public String getWechatID() {
		return wechatID;
	}

	public void setWechatID(String wechatID) {
		this.wechatID = wechatID;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		logger.info("IN:"+event.getXml());
	    if (this.listener != null)
	      this.listener.onEvent(event);
	}

	@Override
	public void onMessage(InMessage message) {
		// TODO Auto-generated method stub
		logger.info("IN:"+message.getXml());
	    MsgType type = message.getMessageType();
	    if (MediaType.isMediaType(type)){
	        if ((message.getMediaId() != null) && (message.getMedia() == null)) {
	        	Media media          = new Media();
	        	media.setMedia_id(message.getMediaId());
	        	HttpRequest request = media_manager.downloadMedia(media, this);
	        	request.setAttribute(INMESSAGE, message);
	        	return;
	        }
	    }
	    if(message.getFrom()==null && message.getFromUserName()!=null){
	    	User user = new User(null);
	    	user.setOpenid(message.getFromUserName());
	    	HttpRequest request = user_manager.getUserInfo(user, this);
	    	request.setAttribute(INMESSAGE, message);
	    	return;
	    }
	    if (this.listener != null)
	      this.listener.onMessage(message);
	}
	
	public void onToken(Token token) {
		// TODO Auto-generated method stub
		if(token==null)return;
		String access_token = token.getAccess_token();
	    long expires_in = token.getExpires_in();
	    if(access_token!=null){
	    	getApp().setAccess_token(access_token);
	    	getApp().setStatus("app is ok:onRunning");
        }else{
        	getApp().setStatus("app is error:"+token.getJson());
        }
        if (expires_in>0) {
        	getApp().setExpires(expires_in * 1000);
        	getApp().setDelay(app.getExpires()-100);
        }
        SessionCache.instance().updateWechatSession(this);
	}
	
	public String logStr(String format, Object... args){
		List<Object> list=new ArrayList<Object>(Arrays.asList(args));
		list.add(0,wechatID==null?"":wechatID);
		return String.format("(%s)-->" + format, list.toArray());
	}
	
	public void execute(HttpRequest request) {
		// TODO Auto-generated method stub
	   logger.info("out:"+ request.getUrl());
	   logger.info(logStr("out:"+(request.getContent()==null?"":request.getContent())));
	   ThreadManager.instance().execute(request);
    }

	public AppManager getApp_manager() {
		return app_manager;
	}

	public void setApp_manager(AppManager app_manager) {
		this.app_manager = app_manager;
	}

	public AgentManager getAgent_manager() {
		return agent_manager;
	}

	public void setAgent_manager(AgentManager agent_manager) {
		this.agent_manager = agent_manager;
	}

	public MediaManager getMedia_manager() {
		return media_manager;
	}

	public void setMedia_manager(MediaManager media_manager) {
		this.media_manager = media_manager;
	}

	public MenusManager getMenus_manager() {
		return menus_manager;
	}

	public void setMenus_manager(MenusManager menus_manager) {
		this.menus_manager = menus_manager;
	}

	public MsgManager getMsg_manager() {
		return msg_manager;
	}

	public void setMsg_manager(MsgManager msg_manager) {
		this.msg_manager = msg_manager;
	}

	public QrcodeManager getQrcode_manager() {
		return qrcode_manager;
	}

	public void setQrcode_manager(QrcodeManager qrcode_manager) {
		this.qrcode_manager = qrcode_manager;
	}

	public UserManager getUser_manager() {
		return user_manager;
	}

	public void setUser_manager(UserManager user_manager) {
		this.user_manager = user_manager;
	}

	@Override
	public void onError(HttpRequest request, Error error) {
		// TODO Auto-generated method stub
		logger.info(logStr(error.toJson()));
	}

	@Override
	public void onMedia(HttpRequest request, Media media) {
		// TODO Auto-generated method stub
		if(request.getAttibute(INMESSAGE)!=null){
			InMessage msg  = (InMessage) request.getAttibute(INMESSAGE);
			msg.setMedia(media);
			onMessage(msg);
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

	@Override
	public void onGroup(HttpRequest request, List<Group> groups) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGroup(HttpRequest request, Group group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUser(HttpRequest request, User user) {
		// TODO Auto-generated method stub
		if(request.getAttibute(INMESSAGE)!=null){
			if(request.getAttibute(INMESSAGE) instanceof InMessage){
				InMessage msg  = (InMessage) request.getAttibute(INMESSAGE);
				msg.setFrom(user);
				onMessage(msg);
			}
			else if(request.getAttibute(INMESSAGE) instanceof Event){
				Event event  = (Event) request.getAttibute(INMESSAGE);
				event.setFrom(user);
				onEvent(event);
			}
		}
	}

	@Override
	public void onUsers(HttpRequest request, List<User> users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUsers(HttpRequest request, Users users) {
		// TODO Auto-generated method stub
		
	}

	public boolean appVerify(String signature, String timestamp, String nonce){
		 String token = getApp()!=null?getApp().getToken():null;
		 if(token!=null && signature!=null && timestamp!=null && nonce!=null){
			 return signature.equalsIgnoreCase(WechatUtils.createSignature(token, timestamp, nonce));
		 }
		 return false;
	}
	
	public String onEncrypt(Encrypt encrypt) throws AesException{
		logger.info("IN:"+encrypt.getXml());
		logger.info(logStr("msg_signature:%s;timestamp:%s;nonce:%s",encrypt.getMsg_signature(), encrypt.getTimestamp(), encrypt.getNonce()));
		if(getMsg_crypt()!=null)
			return getMsg_crypt().decryptMsg(encrypt.getMsg_signature(), encrypt.getTimestamp(), encrypt.getNonce(), encrypt.getXml());
		return null;
	}

	public void setMsg_crypt(WXBizMsgCrypt msg_crypt) {
		this.msg_crypt = msg_crypt;
	}

	public WXBizMsgCrypt getMsg_crypt() {
		return msg_crypt;
	}
}
