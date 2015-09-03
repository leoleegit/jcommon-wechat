package org.jcommon.com.wechat.router.server;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.jcommon.com.wechat.router.Router;

public class Encoder extends ProtocolEncoderAdapter{

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof Router){
			Router router = (Router) message;
			IoBuffer ioBuffer = IoBuffer.wrap(ByteBuffer.wrap(router.toJson().getBytes("utf-8")));
			ioBuffer.position(ioBuffer.capacity());
			ioBuffer.flip();
			out.write(ioBuffer);
		}
	}

}
