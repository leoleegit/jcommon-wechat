package org.jcommon.com.wechat.router.client;

import java.net.InetSocketAddress;
import java.sql.Timestamp;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.echoserver.ssl.BogusSslContextFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.jcommon.com.wechat.router.Router;
import org.jcommon.com.wechat.router.server.CodecFactory;
import org.jcommon.com.wechat.router.server.NoIOAcceptorHandler;
import org.jcommon.com.wechat.utils.WechatUtils;

public class NoIOClient extends NoIOAcceptorHandler {
	private NioSocketConnector connector;
	private String wechatID;
	private String Token;
	
	public NoIOClient(String addr, int port, String wechatID, String Token){
		super(null,addr,port);
		this.wechatID = wechatID;
		this.Token    = Token;
	}

	public void sessionCreated(final IoSession session) throws Exception {
        // Empty handler
    	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
    	
    	String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()); 
	    String nonce     = org.jcommon.com.util.BufferUtils.generateRandom(6);
	    String signature = WechatUtils.createSignature(this.Token, timestamp, nonce); 
	    
	    Router router = new Router(signature, timestamp, nonce, null);
	    router.setWechatID(wechatID);
	    this.send(session, router);
    }
	
	public void start(){
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
		connector.connect(new InetSocketAddress(localAddr.getHostAddress(), localPort));

		logger.info("connect on "
				+ localAddr.getHostAddress()
				+ " : "+localPort);
	}
	
	public void stop(){
		if(connector!=null){
			connector.dispose();
			connector = null;
		}

		logger.info("stop on port "
				+ localAddr.getHostAddress()
				+ " : "+localPort);
	}
	
	public void messageReceived(IoSession session, Object message)
	 	throws Exception {
	 	// Empty handler
	 	logger.info("RemoteAddress: "
				+ session.getRemoteAddress());
	 	if(message instanceof Router){
	 		logger.info(((Router)message).getJson());
	 		logger.info(((Router)message).getType());
	 	}
	}

	public String getWechatID() {
		return wechatID;
	}

	public void setWechatID(String wechatID) {
		this.wechatID = wechatID;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}	
}
