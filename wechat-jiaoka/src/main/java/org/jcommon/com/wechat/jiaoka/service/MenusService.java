package org.jcommon.com.wechat.jiaoka.service;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.MenusManagerListener;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Menus;

public class MenusService implements MenusManagerListener,
	RequestCallback{
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void onError(HttpRequest request, Error error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMenus(HttpRequest request, Menus menus) {
		// TODO Auto-generated method stub
		
	}

	public void createMenus(WechatSession session, Menus menus){
		if(session!=null && menus!=null){
			session.getMenus_manager().createMenus(menus, this);
		}
	}

	@Override
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		logger.info(sResult);
	}

	@Override
	public void onFailure(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		logger.info(sResult);
	}

	@Override
	public void onTimeout(HttpRequest reqeust) {
		// TODO Auto-generated method stub
		logger.info("timeout");
	}

	@Override
	public void onException(HttpRequest reqeust, Exception e) {
		// TODO Auto-generated method stub
		logger.error("", e);
	}
}
