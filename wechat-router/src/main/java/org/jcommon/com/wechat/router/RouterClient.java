package org.jcommon.com.wechat.router;

import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.apache.log4j.Logger;
import org.jcommon.com.util.jmx.Monitor;
import org.jcommon.com.wechat.router.client.NoIOClient;
import org.jcommon.com.wechat.router.client.TokenHttpClient;

public class RouterClient extends Monitor{
	private Logger logger = Logger.getLogger(getClass());
	private Set<NoIOClient> io_clients = new HashSet<NoIOClient>();
	private Set<TokenHttpClient> http_clients = new HashSet<TokenHttpClient>();
	
	public RouterClient() {
		super("Wechat Router Client");
		// TODO Auto-generated constructor stub
	}
	
	public void shutdown(){
		super.shutdown();
		synchronized(io_clients){
			for(NoIOClient client : io_clients){
				client.shutdown();
			}
		}
		synchronized(io_clients){
			for(TokenHttpClient client : http_clients){
				client.stop();
			}
		}
		io_clients.clear();
		http_clients.clear();
	}
	
	@Override 
    public void initOperation() {
		addOperation(new MBeanOperationInfo(
		          "addTokenHttpClient",
		          "addTokenHttpClient",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		        "wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
				          		"token","java.lang.String","token"),
			            new MBeanParameterInfo(
				          		"access_url","java.lang.String","access_url"),
			            new MBeanParameterInfo(
				          		"callback_url","java.lang.String","callback_url")},  
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "removeTokenHttpClient",
		          "removeTokenHttpClient",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		        "wechatID","java.lang.String","wechatID"),
		          		new MBeanParameterInfo(
				          		"token","java.lang.String","token"),
			            new MBeanParameterInfo(
				          		"access_url","java.lang.String","access_url"),
			            new MBeanParameterInfo(
				          		"callback_url","java.lang.String","callback_url")},  
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "addNoIOClient",
		          "addNoIOClient",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		        "addr","java.lang.String","addr"),
		          		new MBeanParameterInfo(
				          		"port","java.lang.Integer","port"),
			            new MBeanParameterInfo(
				          		"wechatID","java.lang.String","wechatID"),
			            new MBeanParameterInfo(
				          		"Token","java.lang.String","Token"),
		          		new MBeanParameterInfo(
				          		"callback_url","java.lang.String","callback_url"),
			            new MBeanParameterInfo(
				          		"token_url","java.lang.String","token_url")},  
		          "void",
		          MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
		          "removeNoIOClient",
		          "removeNoIOClient",
		          new MBeanParameterInfo[]{new MBeanParameterInfo(
		          		        "addr","java.lang.String","addr"),
		          		new MBeanParameterInfo(
				          		"port","java.lang.Integer","port"),
			            new MBeanParameterInfo(
				          		"wechatID","java.lang.String","wechatID"),
			            new MBeanParameterInfo(
				          		"Token","java.lang.String","Token"),
		          		new MBeanParameterInfo(
				          		"callback_url","java.lang.String","callback_url"),
			            new MBeanParameterInfo(
				          		"token_url","java.lang.String","token_url")},  
		          "void",
		          MBeanOperationInfo.ACTION));
	}

	public void addTokenHttpClient(String wechatID,String token,String access_url,String callback_url){
		logger.info(String.format("%s:%s access_url:%s; callback_url:%s", wechatID,token,access_url,callback_url));
		TokenHttpClient client = getTokenHttpClient(wechatID,token,access_url,callback_url);
		if(client==null)
			client = new TokenHttpClient(wechatID,token,access_url,callback_url);
		client.go();
		super.addProperties(client.toKey(), client.toString());
	}
	
	public void removeTokenHttpClient(String wechatID,String token,String access_url,String callback_url){
		logger.info(String.format("%s:%s access_url:%s; callback_url:%s", wechatID,token,access_url,callback_url));
		TokenHttpClient client = getTokenHttpClient(wechatID,token,access_url,callback_url);
		if(client!=null){
			http_clients.remove(client);
			if(http_clients.size()!=0){
				super.addProperties(client.toKey(), client.toString());
			}else{
				super.removeProperties(client.toKey());
			}
		}	
	}
	
	public TokenHttpClient getTokenHttpClient(String wechatID,String token,String access_url,String callback_url){
		String key = String.format("%s:%s; access_url:%s; callback_url:%s", wechatID,token,access_url,callback_url);
		synchronized(io_clients){
			for(TokenHttpClient client : http_clients){
				if(key.equals(client.toString()))
					return client;
			}
		}
		return null;
	}
	
	public void addNoIOClient(String addr,int port,String wechatID,String Token,String callback_url,String token_url){
		logger.info(String.format("%s:%s wechatID:%s; Token:%s; callback_url:%s; token_url:%s", addr,port,wechatID,Token,callback_url,token_url));
		
		NoIOClient client = getNoIOClient(addr,port);
		if(client==null){
			client = new NoIOClient(addr,port);
			client.startup();
			io_clients.add(client);
		}
		client.addCRouter(wechatID, Token, callback_url, token_url);
		super.addProperties(client.toKey(), client.toString());
	}
	
	public void removeNoIOClient(String addr,int port,String wechatID,String Token,String callback_url,String token_url){
		logger.info(String.format("%s:%s wechatID:%s; Token:%s; callback_url:%s; token_url:%s", addr,port,wechatID,Token,callback_url,token_url));
		
		NoIOClient client = getNoIOClient(addr,port);
		if(client==null){
			logger.info("can't find client of:"+ String.format("%s:%s", addr,port));
			return;
		}
		io_clients.remove(client);
		client.removeCRouter(wechatID, Token, callback_url, token_url);
		if(client.size()!=0)
			super.addProperties(client.toKey(), client.toString());
		else
			super.removeProperties(client.toKey());
	}
	
	private NoIOClient getNoIOClient(String addr,int port){
		String key = String.format("%s:%s", addr,port);
		logger.info(key);
		synchronized(io_clients){
			for(NoIOClient client : io_clients){
				logger.info(client.toKey());
				if(key.equals(client.toKey()))
					return client;
				else
					logger.info(false);
			}
		}
		return null;
	}

}
