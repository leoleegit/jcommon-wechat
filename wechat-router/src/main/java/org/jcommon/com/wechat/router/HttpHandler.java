package org.jcommon.com.wechat.router;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;
import org.jcommon.com.wechat.RouterHandler;
import org.jcommon.com.wechat.data.Router;

public class HttpHandler implements RouterHandler, HttpListener{
	private Logger logger = Logger.getLogger(getClass());
	private String callback;
	
	public HttpHandler(String callback) {
		this.callback = callback;
	}
	
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
	}
	
	public void onFailure(HttpRequest reqeust, StringBuilder sResult){
		logger.info(sResult);
	}
	
	public void onTimeout(HttpRequest request){
	    logger.error(callback);
	}

	public void onException(HttpRequest request, Exception e){
	    logger.error(callback, e);
	}
	
//	public boolean appVerify(String signature, String timestamp, String nonce){
//		return true;
//	}

	@Override
	public void onRouter(Router router) {
		// TODO Auto-generated method stub
		if(callback!=null && router!=null){
			String signature = router.getSignature();
			String timestamp = router.getTimestamp();
			String nonce     = router.getNonce();
			String xml       = router.getXml();
			String[] keys   = { "signature","timestamp","nonce" };
			String[] values = { signature,timestamp,nonce };
			String    url   = JsonUtils.toRequestURL(callback, keys, values);
			HttpRequest request = new HttpRequest(url,xml,"POST",this);
			ThreadManager.instance().execute(request);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
