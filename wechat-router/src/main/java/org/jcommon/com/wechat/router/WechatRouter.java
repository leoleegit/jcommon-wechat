package org.jcommon.com.wechat.router;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.apache.log4j.Logger;
import org.jcommon.com.util.jmx.Monitor;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionManager;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Token;
import org.jcommon.com.wechat.router.server.HttpHandler;
import org.jcommon.com.wechat.router.server.NoIOAcceptorHandler;

public class WechatRouter extends Monitor{
	private Logger logger = Logger.getLogger(getClass());
	
	private CallbackRouter  callback_router;
	private TokenRouter     token_router;
	private RouterHandler[] router_handlers;
	
	private static WechatRouter instance;
	public  static WechatRouter instance(){
		if(instance==null)
			new WechatRouter();
		return instance;
	}

	public WechatRouter() {
		super("WechatRouter");
		// TODO Auto-generated constructor stub
		callback_router = new CallbackRouter(this);
		token_router    = new TokenRouter(this);
		router_handlers = new RouterHandler[]{new HttpHandler(this),new NoIOAcceptorHandler(this)};
		instance = this;
	}
	
	@Override 
    public void initOperation() {
		// TODO Auto-generated method stub
		addOperation(new MBeanOperationInfo(
		          "addHttpCallbackRouter",
		          "addHttpCallbackRouter ",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
		    	          		"url","java.lang.String","url")},  
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "addTokenHttpRouter",
		          "addTokenHttpRouter ",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
		    	          		"url","java.lang.String","url")},  
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "removeHttpCallbackRouter",
		          "removeHttpCallbackRouter ",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
		    	          		"url","java.lang.String","url")},  
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "removeTokenHttpRouter",
		          "removeTokenHttpRouter ",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
		    	          		"url","java.lang.String","url")},  
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "addSession",
		          "addSession",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
				          		"appid","java.lang.String","appid"),
			          new MBeanParameterInfo(
				          		"secret","java.lang.String","secret"),
			          new MBeanParameterInfo(
				          		"Token","java.lang.String","Token")},   // no parameters
		          "void",
		          MBeanOperationInfo.ACTION));
    }

	public void addSession(String wechatID, String appid, String secret, String Token){
		  logger.info(String.format("wechatID:%s ; appid:%s ; secret:%s ; Toke:%s ;", wechatID,appid,secret,Token));
		  WechatSession session = WechatSessionManager.instance().getWechatSession(wechatID);
		  if(session==null){
			  App app = new App(appid,secret,Token);
			  session = new RouterWechatSession(wechatID,app,null);
			  session.startup();
		  }else{
			  logger.info(String.format("session of %s have been exist!", wechatID));
		  }
	}
	
	public void addHttpCallbackRouter(String wechatID,String url){
		addHttpRouter(RouterType.Callback,wechatID,url);
	}
	
	public void addTokenHttpRouter(String wechatID,String url){
		addHttpRouter(RouterType.Token,wechatID,url);
	}
	
	private void addHttpRouter(RouterType type, String wechatID,String url){
		logger.info(String.format("%s %s : %s", wechatID, type,url));
		RouterHandler handler = getRouterHandler(HttpHandler.class);
		if(handler==null){
			logger.warn("can find Http Handler");
			return;
		}
		handler.addRouter(type.toString(),wechatID,url);
		super.addProperties(handler.getClass().getSimpleName(),handler.toString());
	}
	
	public void removeHttpCallbackRouter(String wechatID,String url){
		removeHttpRouter(RouterType.Callback,wechatID,url);
	}
	
	public void removeTokenHttpRouter(String wechatID,String url){
		removeHttpRouter(RouterType.Token,wechatID,url);
	}
	
	private void removeHttpRouter(RouterType type, String wechatID,String url){
		logger.info(String.format("%s %s : %s", wechatID, type,url));
		RouterHandler handler = getRouterHandler(HttpHandler.class);
		if(handler==null){
			logger.warn("can find Http Handler");
			return;
		}
		handler.removeRouter(type.toString(),wechatID,url);
		super.addProperties(handler.getClass().getSimpleName(),handler.toString());
	}
	
	public void addNoIORouter(Object session){
		RouterHandler handler = getRouterHandler(NoIOAcceptorHandler.class);
		if(handler==null){
			logger.warn("can find NoIO Handler");
			return;
		}
		handler.addRouter(session);
		super.addProperties(handler.getClass().getSimpleName(),handler.toString());
	}
	
	public void removeNoIORouter(Object session){
		RouterHandler handler = getRouterHandler(NoIOAcceptorHandler.class);
		if(handler==null){
			logger.warn("can find NoIO Handler");
			return;
		}
		handler.removeRouter(session);
		super.addProperties(handler.getClass().getSimpleName(),handler.toString());
	}
	
	public void startup(){
		callback_router.startup();
		token_router.startup();
		if(router_handlers!=null){
			for(RouterHandler h : router_handlers)
				h.startup();
		}
		super.startup();
		logger.info("WechatRouter startup");
	}
	
	public void shutdown(){
		callback_router.shutdown();
		token_router.shutdown();
		if(router_handlers!=null){
			for(RouterHandler h : router_handlers)
				h.shutdown();
		}
		super.shutdown();
		logger.info("WechatRouter shutdown");
	}
	
	public void onRouter(Router router){
		if(router_handlers!=null){
			for(RouterHandler h : router_handlers)
				h.onRouter(router);
		}
	}
	
	public void onToken(Token token){
		logger.info(token.toJson());
		String wechatID = token.getWechatID();
		if(token.getAccess_token()==null){
			super.removeProperties(wechatID);
			return;
		}
		super.addProperties(wechatID, token.toJson());
		if(router_handlers!=null){
			for(RouterHandler h : router_handlers)
				h.onToken(token);
		}
	}
	
	private RouterHandler getRouterHandler(Class<? extends RouterHandler> clazz){
		if(router_handlers!=null){
			for(RouterHandler h : router_handlers){
				if(clazz == h.getClass())
					return h;
			}
		}
		return null;
	}
	
	public CallbackRouter getCallback_router() {
		return callback_router;
	}

	public void setCallback_router(CallbackRouter callback_router) {
		this.callback_router = callback_router;
	}

	public TokenRouter getToken_router() {
		return token_router;
	}

	public void setToken_router(TokenRouter token_router) {
		this.token_router = token_router;
	}
}
