package org.jcommon.com.wechat.router.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.jcommon.com.wechat.router.Router;

public class Decoder extends TextLineDecoder {

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		super.decode(session, in, out);
	}

	@Override
	protected void writeText(IoSession session, String text, ProtocolDecoderOutput out) {
		if(text!=null && "".equals(text))
    		return;
		Router router = new Router(text);
		out.write(router);
	}
}
