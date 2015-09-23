package org.jcommon.com.wechat.router.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.echoserver.ssl.BogusSslContextFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.jcommon.com.wechat.WechatSessionManager;
import org.jcommon.com.wechat.data.Token;
import org.jcommon.com.wechat.router.Router;
import org.jcommon.com.wechat.router.RouterHandler;
import org.jcommon.com.wechat.router.RouterType;
import org.jcommon.com.wechat.router.WechatRouter;
import org.jcommon.com.wechat.utils.WechatUtils;

public class NoIOAcceptorHandler extends IoHandlerAdapter implements RouterHandler{
	protected Logger logger = Logger.getLogger(getClass());
	private NioSocketAcceptor socketAcceptor;
	protected InetAddress localAddr;
	protected int         localPort;
	protected   boolean   sslEnable;
	
	private final static String WECHATID     = "wechatID";
	private final static String DEFAULT_ADDR = "127.0.0.1";
	private final static int    DEFAULT_PORT = 5010;
	private final static long   kill_delay   = 5 * 1000;
	private WechatRouter router;
	
	private Set<IoSession> sessions = new HashSet<IoSession>();
	
	public NoIOAcceptorHandler(WechatRouter router){	
		this(router,DEFAULT_ADDR,DEFAULT_PORT);
	}
	
	public NoIOAcceptorHandler(WechatRouter router,String addr,int port){
		this.router = router;
		if(addr == null){
			try {
				this.localAddr = InetAddress.getLocalHost();
	        }
	        catch (UnknownHostException e) {
	        	addr = DEFAULT_ADDR;
	        	try {
					this.localAddr = InetAddress.getByName(addr);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					logger.error("", e1);
				}
	        }
        }else{
        	try {
				this.localAddr = InetAddress.getByName(addr);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				logger.error("", e1);
			}
        }
		
		this.localPort = port>0?port:DEFAULT_PORT;
	}

    public void sessionCreated(final IoSession session) throws Exception {
        // Empty handler
    	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
    	
    	org.jcommon.com.util.thread.TimerTaskManger.instance().schedule("check session", new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				synchronized(sessions){
		    		if(!sessions.contains(session)){
		    			logger.info("Kill Illegal Session");
		    			session.close(true);
		    		}
		    	}
			}
    		
    	}, kill_delay);
    }

    public void sessionOpened(IoSession session) throws Exception {
        // Empty handler
    	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
    }

    public void sessionClosed(IoSession session) throws Exception {
        // Empty handler
    	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
    	if(this.router!=null)
    		this.router.removeNoIORouter(session);
    }

    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        // Empty handler
    	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
    }
    
    public void messageReceived(IoSession session, Object message)
    	throws Exception {
    	// Empty handler
    	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
    	if(message instanceof Router){
    		Router router = (Router) message;
    		String signature = router.getSignature();
    		String timestamp = router.getTimestamp();
    		String nonce     = router.getNonce();
    		String wechatID  = router.getWechatID();
    		
    	    if (WechatSessionManager.instance().appVerify(signature, timestamp, nonce)){
    	    	session.setAttribute(WECHATID, wechatID);
    	    	if(this.router!=null)
    	    		this.router.addNoIORouter(session);
    	    	Token token = this.getRouter().getCallback_router().getToken(wechatID);
    	    	if(token!=null)
    	    		this.onToken(token);
    	    	else
    	    		logger.info("can't find token");
    	    }  
    	    else
    	    	logger.warn("Illegal Data! : "+router.getJson());
    	}
	}
	
	public void messageSent(IoSession session, Object message) throws Exception {
		// Empty handler
		logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
	}
	
	public void send(IoSession session, Object message){
		if(session.isConnected()){
			session.write(message);
		}
	}

	public void setSslEnable(boolean sslEnable) {
		this.sslEnable = sslEnable;
	}

	public boolean isSslEnable() {
		return sslEnable;
	}

	@Override
	public void startup(){
		int port = System.getProperty("RouterPort")!=null?Integer.valueOf(System.getProperty("RouterPort")):localPort;
		
		socketAcceptor = new NioSocketAcceptor();
		socketAcceptor.getSessionConfig().setReadBufferSize(4096);
		socketAcceptor.getSessionConfig().setReceiveBufferSize(4096);
		DefaultIoFilterChainBuilder chain = socketAcceptor.getFilterChain();
		//socketAcceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
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
		
		// Bind
		socketAcceptor.setHandler(this);
		try {
			socketAcceptor.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}

		logger.info("listening on "
				+ socketAcceptor.getLocalAddress().getHostName()
				+ " : "+socketAcceptor.getLocalAddress().getPort());
	}
	
	@Override
	public void shutdown(){
		if(socketAcceptor!=null){
			socketAcceptor.unbind();
			socketAcceptor = null;
		}

		logger.info("stop on port "
				+ localAddr.getHostAddress()
				+ " : "+localPort);
	}
	
	@Override
	public void onRouter(Router router) {
		// TODO Auto-generated method stub
		router.setType(RouterType.Callback.toString());
		if(router!=null && router.getWechatID()!=null){
			for(IoSession session : getIoSession(router.getWechatID())){
				this.send(session, router);
			}
		}
	}

	@Override
	public void onToken(Token token) {
		// TODO Auto-generated method stub
		logger.info(token.toJson());
		if(token!=null && token.getWechatID()!=null){
			String Token     = token.getToken();
			String wechatID  = token.getWechatID();
			
			String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()); 
		    String nonce     = org.jcommon.com.util.BufferUtils.generateRandom(6);
		    String signature = WechatUtils.createSignature(Token, timestamp, nonce); 
			String xml       = new Token(token.getAccess_token(),token.getExpires_in()).toJson();
			Router router = new Router(timestamp,nonce,signature,xml);
			router.setType(RouterType.Token.toString());
			router.setXml(token.toJson());
			router.setWechatID(wechatID);
			
			for(IoSession session : getIoSession(wechatID)){
				this.send(session, router);
			}
		}
	}

	@Override
	public void addRouter(Object... args) {
		// TODO Auto-generated method stub
		IoSession session = (IoSession) args[0];	
		sessions.add(session);
	}

	@Override
	public void removeRouter(Object... args) {
		// TODO Auto-generated method stub
		IoSession session = (IoSession) args[0];	
		sessions.remove(session);
	}
	
	public Set<IoSession> getIoSession(String wechatID){
		Set<IoSession> ios  = new HashSet<IoSession>();
		for(IoSession session : sessions){
			if(session.getAttribute(WECHATID)!=null && session.getAttribute(WECHATID).equals(wechatID))
				ios.add(session);
		}
		return ios;
	}

	public WechatRouter getRouter() {
		return router;
	}

	public void setRouter(WechatRouter router) {
		this.router = router;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(IoSession session : sessions){
			sb.append(String.format("%s : %s", session.getAttribute(WECHATID),session.getRemoteAddress())).append(";	");
		}
		return sb.toString();
	}
}
