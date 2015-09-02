package org.jcommon.com.wechat.router;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.echoserver.ssl.BogusSslContextFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.jcommon.com.wechat.RouterHandler;
import org.jcommon.com.wechat.data.Router;

public class NoIOClient extends NoIOAcceptorHandler {
	private NioSocketConnector connector;
	private RouterHandler handler;
	
	public NoIOClient(String addr, int port){
		super(addr,port);
	}
	
	public NoIOClient(String addr, int port, RouterHandler handler){
		this(addr,port);
		this.handler = handler;
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
		chain.addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
		
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
		if(handler!=null)
			handler.stop();

		logger.info("stop on port "
				+ localAddr.getHostAddress()
				+ " : "+localPort);
	}
	
	public void messageReceived(IoSession session, Object message)
	 	throws Exception {
	 	// Empty handler
	 	logger.info("RemoteAddress: "
				+ session.getRemoteAddress()+"\n"+message);
	 	if(handler!=null)
	 		handler.onRouter(new Router(message.toString()));
	}

	public void setHandler(RouterHandler handler) {
		this.handler = handler;
	}

	public RouterHandler getHandler() {
		return handler;
	}
	
	
}
