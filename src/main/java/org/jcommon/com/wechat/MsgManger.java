package org.jcommon.com.wechat;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;

public class MsgManger extends ResponseHandler{
    private Logger logger = Logger.getLogger(this.getClass());
    
    private App app;
    
    public MsgManger(App app){
    	
    }
	
	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		
	}

	public void setApp(App app) {
		this.app = app;
	}

	public App getApp() {
		return app;
	}

}
