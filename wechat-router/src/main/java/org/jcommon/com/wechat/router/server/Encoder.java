package org.jcommon.com.wechat.router.server;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.TextLineEncoder;
import org.jcommon.com.wechat.router.Router;

public class Encoder extends TextLineEncoder{

	public Encoder() {
        super(Charset.forName("UTF-8"));
    }
	
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof Router){
			Router router = (Router) message;
			super.encode(session, router.toJson(), out);
		}
	}

}
