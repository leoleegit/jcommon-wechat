package org.jcommon.com.wechat.router;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.echoserver.ssl.BogusSslContextFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.jcommon.com.wechat.RouterHandler;
import org.jcommon.com.wechat.WechatSessionRouter;
import org.jcommon.com.wechat.data.Router;

public class NoIOAcceptorHandler extends IoHandlerAdapter implements RouterHandler{
	protected Logger logger = Logger.getLogger(getClass());
	private NioSocketAcceptor socketAcceptor;
	protected InetAddress localAddr;
	protected int         localPort;
	protected   boolean     sslEnable;
	
	private final static String DEFAULT_ADDR = "127.0.0.1";
	private final static int    DEFAULT_PORT = 5010;
	private WechatSessionRouter router;
	
	private List<IoSession> sessions = new ArrayList<IoSession>();
	
	public NoIOAcceptorHandler(){
		this(DEFAULT_ADDR,DEFAULT_PORT);
	}
	
	public NoIOAcceptorHandler(String addr, int port){
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
		
		this.localPort = port!=0?port:DEFAULT_PORT;
	}

    public void sessionCreated(IoSession session) throws Exception {
        // Empty handler
    	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
  
    	synchronized(sessions){
    		if(!sessions.contains(session))
    			sessions.add(session);
    	}
    	
    	if(router!=null && router.getAccesstoken()!=null)
    		session.write(router.getAccesstoken().toJson());
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
    	synchronized(sessions){
    		if(sessions.contains(session))
    			sessions.remove(session);
    	}
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
	}
	
	public void messageSent(IoSession session, Object message) throws Exception {
		// Empty handler
		logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
	}
	
	public void broadcast(String msg)throws Exception{
		synchronized(sessions){
			for(IoSession session : sessions){
				session.write(msg);
			}
		}
	}

	public void start(){
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
		chain.addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
		
		// Bind
		socketAcceptor.setHandler(this);
		try {
			socketAcceptor.bind(new InetSocketAddress(localPort));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}

		logger.info("listening on "
				+ socketAcceptor.getLocalAddress().getHostName()
				+ " : "+socketAcceptor.getLocalAddress().getPort());
	}
	
	public void stop(){
		if(socketAcceptor!=null){
			socketAcceptor.unbind();
			socketAcceptor = null;
		}

		logger.info("stop on port "
				+ localAddr.getHostAddress()
				+ " : "+localPort);
	}

	public void setSslEnable(boolean sslEnable) {
		this.sslEnable = sslEnable;
	}

	public boolean isSslEnable() {
		return sslEnable;
	}

	
	@Override
	public void onRouter(Router router) {
		// TODO Auto-generated method stub
		try {
			if(router!=null){
				broadcast(router.toJson());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}

	public void setRouter(WechatSessionRouter router) {
		this.router = router;
	}

	public WechatSessionRouter getRouter() {
		return router;
	}
}
