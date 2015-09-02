package org.jcommon.com.wechat.router;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.apache.log4j.Logger;
import org.jcommon.com.util.jmx.Monitor;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionManager;
import org.jcommon.com.wechat.WechatSessionRouter;
import org.jcommon.com.wechat.data.App;

public class WechatSessionRouterManager extends Monitor{
	
	private Logger logger = Logger.getLogger(getClass());
	private static WechatSessionRouterManager instance;
	
	public static WechatSessionRouterManager instance(){
		if(instance==null)
			new WechatSessionRouterManager();
		return instance;
	}
	
	public WechatSessionRouterManager() {
		super("WechatSessionRouterManager");
		// TODO Auto-generated constructor stub
		instance = this;
	}

    @Override 
    public void initOperation() {
		// TODO Auto-generated method stub
		addOperation(new MBeanOperationInfo(
	          "newWechatSessionRouter",
	          "newWechatSessionRouter ",
	          new MBeanParameterInfo[]{new MBeanParameterInfo(
	          		"wechatID","java.lang.String","wechatID"),
	          		new MBeanParameterInfo(
	    	          		"appid","java.lang.String","appid"),
	          		new MBeanParameterInfo(
	    	          		"secret","java.lang.String","secret"),
	          		new MBeanParameterInfo(
	    	          		"Token","java.lang.String","Token")},  
	          "void",
	          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "removeHandler",
		          "removeHandler",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID")},   
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "addNoIOConnectorHandler",
		          "addNoIOConnectorHandler ",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
		    	          		"port","java.lang.Integer","port")},   
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "addHttpHandler",
		          "addHttpHandler ",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		"wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
		    	          		"callback","java.lang.String","callback")},  
		          "void",
		          MBeanOperationInfo.ACTION));
    }
	  
	public void newWechatSessionRouter(String wechatID,String appid, String secret, String Token){
		logger.info(String.format("wechatID:%s;appid:%s;secret:%s;Token:%s;", 
				wechatID,appid,secret,Token));
		App app = new App(appid,secret,Token);
		WechatSessionRouter router = new WechatSessionRouter(wechatID,app);
		router.startup();
	}
	
	public void addNoIOConnectorHandler(String wechatID,int port){
		logger.info(String.format("wechatID:%s;port:%s;", 
				wechatID,port));
		
		WechatSession session = WechatSessionManager.instance().getWechatSession(wechatID);
		if(session!=null && session instanceof WechatSessionRouter){
			NoIOAcceptorHandler handler = new NoIOAcceptorHandler(null,port);
			handler.setRouter(((WechatSessionRouter)session));
			handler.start();
			((WechatSessionRouter)session).addRouterHandler(handler);
		}else{
			logger.warn("can not find session of "+wechatID);
		}
	}
	
	public void addHttpHandler(String wechatID,String callback){
		logger.info(String.format("wechatID:%s;callback:%s;", 
				wechatID,callback));
		
		WechatSession session = WechatSessionManager.instance().getWechatSession(wechatID);
		if(session!=null && session instanceof WechatSessionRouter){
			HttpHandler handler = new HttpHandler(callback);
			((WechatSessionRouter)session).addRouterHandler(handler);
		}else{
			logger.warn("can not find session of "+wechatID);
		}
	}
	
	public void removeHandler(String wechatID){
		logger.info(String.format("wechatID:%s;", 
				wechatID));
		
		WechatSession session = WechatSessionManager.instance().getWechatSession(wechatID);
		if(session!=null && session instanceof WechatSessionRouter){
			((WechatSessionRouter)session).clearRouterHandler();
		}else{
			logger.warn("can not find session of "+wechatID);
		}
	}
}
