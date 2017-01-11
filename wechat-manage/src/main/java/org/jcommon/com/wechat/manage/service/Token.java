package org.jcommon.com.wechat.manage.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.WechatSessionManager;
import org.jcommon.com.wechat.data.Signature;
import org.jcommon.com.wechat.data.StringJson;
import org.jcommon.com.wechat.data.UserToken;
import org.jcommon.com.wechat.utils.WechatUtils;


@Path("token")
public class Token extends Service{
	final static List<StringJson> jsApiList = new ArrayList<StringJson>();
	static {
		jsApiList.add(new StringJson("getLocation"));
		jsApiList.add(new StringJson("openLocation"));
		jsApiList.add(new StringJson("getNetworkType"));
	}
	
	@GET 
	@Path("signature")
	@Produces("text/plain;charset=UTF-8")  
	public String signature(@Context HttpServletRequest request){
		pritelnParameter(request);
		String url     = request.getParameter("url");
		String id      = request.getParameter("id");
		
		ServiceResponse resp = null;
		if(id==null || url==null){
			resp = new ServiceResponse("id and url can not be null");
		}else{
			WechatSession session = WechatSessionManager.instance().getWechatSession(id);
			if(session==null){
				resp = new ServiceResponse("can not find session by "+id);
			}else{
				String noncestr     = org.jcommon.com.util.BufferUtils.generateRandom(6);
				String jsapi_ticket = session.getApp_manager().getTicket();
				String timestamp    = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()); 
				String signature    = WechatUtils.createJsSignature(noncestr, jsapi_ticket, timestamp, url);
				Signature signatureObj = new Signature();
				signatureObj.setNonceStr(noncestr);
				signatureObj.setSignature(signature);
				signatureObj.setTimestamp(timestamp);
				signatureObj.setAppId(session.getApp().getAppid());
				//signatureObj.setJsApiList(jsApiList);
				resp = new ServiceResponse(signatureObj); 
			}
		}
		
		return resp.toJson();
	}
	
	@GET 
	@Path("usertoken")
	@Produces("text/plain;charset=UTF-8")  
	public String usertoken(@Context HttpServletRequest request){
		pritelnParameter(request);
		String code     = request.getParameter("code");
		ServiceResponse resp = null;
		if(code==null){
			resp = new ServiceResponse("code can not be null");
		}else{
			WechatSession session = WechatSessionManager.instance().getWechatSession("gh_f49bb9a333b3");
			UserToken token = session.getApp_manager().getUserToken(code);
			resp = new ServiceResponse(token);
		}
		return resp.toJson();
	}

	@GET 
	@Path("me")
	@Produces("text/plain;charset=UTF-8")  
	public String me(@Context HttpServletRequest request){
		pritelnParameter(request);
		String code     = request.getParameter("code");
		ServiceResponse resp = null;
		if(code==null){
			resp = new ServiceResponse("code can not be null");
		}else{
			WechatSession session = WechatSessionManager.instance().getWechatSession("gh_f49bb9a333b3");
			UserToken token = session.getApp_manager().getUserToken(code);
			resp = new ServiceResponse(token);
		}
		return resp.toJson();
	}
}
