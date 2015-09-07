package org.jcommon.com.wechat.router.client;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.echoserver.ssl.BogusSslContextFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.jcommon.com.wechat.WechatSessionManager;
import org.jcommon.com.wechat.data.Token;
import org.jcommon.com.wechat.router.Router;
import org.jcommon.com.wechat.router.RouterType;
import org.jcommon.com.wechat.router.WechatRouter;
import org.jcommon.com.wechat.router.server.CodecFactory;
import org.jcommon.com.wechat.router.server.NoIOAcceptorHandler;
import org.jcommon.com.wechat.utils.WechatUtils;

public class NoIOClient extends NoIOAcceptorHandler {
	private NioSocketConnector connector;
	private IoSession session;
	private Set<CRouter> routers = new HashSet<CRouter>();
	
	public NoIOClient(String addr, int port){
		super(null,addr,port);
	}

	public void startup(){
		connector = new NioSocketConnector();
		connector.getSessionConfig().setReadBufferSize(4096);
		connector.getSessionConfig().setReceiveBufferSize(4096);
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		//connector.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
		try {
			if(sslEnable){
				SslFilter sslFilter = new SslFilter(BogusSslContextFactory
						.getInstance(true));
				chain.addLast("sslFilter", sslFilter);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		chain.addLast( "logger", new LoggingFilter() );
		//chain.addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory(Charset.forName( "UTF-8" ))));
	    chain.addLast( "codec", new ProtocolCodecFilter(new CodecFactory()));
		
		// connect
		connector.setHandler(this);
		try{
			connector.connect(new InetSocketAddress(localAddr.getHostAddress(), localPort));
		}catch(Exception e){
			logger.error("connect", e);
		}

		logger.info("connect on "
				+ localAddr.getHostAddress()
				+ " : "+localPort);
	}
	
	public void shutdown(){
		if(connector!=null){
			connector.dispose();
			connector = null;
		}
		synchronized (routers) {
    		for(CRouter cr : this.routers){
    			if(cr.callback_url!=null)
    				WechatRouter.instance().removeHttpCallbackRouter(cr.wechatID, cr.callback_url);
    			if(cr.token_url!=null)
    				WechatRouter.instance().removeTokenHttpRouter(cr.wechatID, cr.token_url);
    		}
    	}
		routers.clear();
		logger.info("stop on port "
				+ localAddr.getHostAddress()
				+ " : "+localPort);
	}
	
	public void sessionCreated(final IoSession session) throws Exception {
        // Empty handler
    	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
    	this.session = session;
    	synchronized (routers) {
    		for(CRouter cr : this.routers){
    			send(cr);
    		}
		}
    }
	
	private void send(CRouter cr){
		if(session!=null && session.isConnected()){
			String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()); 
		    String nonce     = org.jcommon.com.util.BufferUtils.generateRandom(6);
		    String signature = WechatUtils.createSignature(cr.Token, timestamp, nonce); 
		    
		    Router router = new Router(signature, timestamp, nonce, null);
		    router.setWechatID(cr.wechatID);
		    this.send(session, router);
		}
	}
	
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		 super.exceptionCaught(session, cause);
		 logger.error("", cause);
	}

	public void messageReceived(IoSession session, Object message)
	 	throws Exception {
	 	// Empty handler
	 	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
	 	if(message instanceof Router){
	 		Router router = (Router)message;
	 		logger.info(router.getJson());

	 		if(RouterType.Token == RouterType.getType(router.getType())){
	 			Token token = new Token(router.getXml());
	 			WechatRouter.instance().onToken(token);
	 		}else{
	 			WechatSessionManager.instance().onCallback(
	 					router.getSignature(), router.getTimestamp(), router.getNonce(), router.getXml());
	 		}
	 	}
	}
	
	public void addCRouter(String wechatID,String Token,String callback_url,String token_url){
		CRouter router = getCRouter(wechatID, Token, callback_url, token_url);
		if(router==null){
			router = new CRouter(wechatID, Token, callback_url, token_url);
			routers.add(router);
			if(callback_url!=null)
				WechatRouter.instance().addHttpCallbackRouter(wechatID, callback_url);
			if(token_url!=null)
				WechatRouter.instance().addTokenHttpRouter(wechatID, token_url);
			send(router);
		}
	}
	
	public void addCRouter(String wechatID,String Token){
		addCRouter(wechatID,Token,null,null);
	}
	
	public void removeCRouter(String wechatID,String Token,String callback_url,String token_url){
		CRouter router = getCRouter(wechatID, Token, callback_url, token_url);
		if(router!=null){
			routers.remove(router);
			if(callback_url!=null)
				WechatRouter.instance().removeHttpCallbackRouter(wechatID, callback_url);
			if(token_url!=null)
				WechatRouter.instance().removeTokenHttpRouter(wechatID, token_url);
		}
	}
	
	public void removeCRouter(String wechatID,String Token){
		removeCRouter(wechatID,Token,null,null);
	}
	
	public CRouter getCRouter(String wechatID,String Token,String callback_url,String token_url){
		synchronized(routers){
			for(CRouter h : routers){
				if(h.equals(wechatID,wechatID,callback_url,token_url))
					return h;
			}
		}
	
		return null;
	}
	
	class CRouter{
		public String wechatID;
		public String Token;
		public String callback_url;
		public String token_url;
		
		public CRouter(String wechatID,String Token){
			this(wechatID,Token,null,null);
		}
		
		public CRouter(String wechatID,String Token,String callback_url,String token_url){
			this.wechatID = wechatID;
			this.Token    = Token;
			this.callback_url = callback_url;
			this.token_url    = token_url;
		}
		
		public String toString(){
			return String.format("%s %s : %s %s", wechatID,Token,callback_url,token_url);
		}
		
		public boolean equals(String wechatID,String Token){
			return equals(wechatID,Token,null,null);
		}

		public boolean equals(String wechatID,String Token,String callback_url,String token_url){
			if(wechatID==null || Token==null)
				return false;
			boolean back_url = false;
			boolean t_url    = false;
			if(callback_url!=null)
				back_url = callback_url.equals(this.callback_url);
			else
				back_url = this.callback_url == null;
			if(token_url!=null)
				t_url = token_url.equals(this.token_url);
			else
				t_url = this.token_url == null;
			return back_url && t_url && wechatID.equals(this.wechatID) && Token.equals(this.Token);
		}

	}
}
