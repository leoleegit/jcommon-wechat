package org.jcommon.com.wechat.router.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.jcommon.com.wechat.router.Router;

public class Decoder extends ProtocolDecoderAdapter {

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		byte[] dataBytes = in.array();
		String message = new String(dataBytes, "utf-8");
		
		if(message!=null && "".equals(message))
    		return;
		Router router = new Router(message);
		out.write(router);
		in.position(in.limit());
	}

}
