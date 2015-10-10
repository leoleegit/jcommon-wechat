package org.jcommon.com.wechat.jiaoka.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jcommon.com.wechat.jiaoka.db.bean.SearchResponse;
import org.jcommon.com.wechat.jiaoka.db.bean.ServiceResponse;
import org.jcommon.com.wechat.jiaoka.db.dao.WeChatAuditLogDao;
import org.jcommon.com.wechat.jiaoka.utils.JiaoKaUtils;

@Path("auditlog")
public class AuditLogService extends Service{
	
	@GET 
	@Path("wechat/search")
	@Produces("text/plain;charset=UTF-8")  
	public String searchCase(@Context HttpServletRequest request){
		String openid       = request.getParameter("openid"); 
		
		int next      = JiaoKaUtils.isInteger(request.getParameter("next"))?Integer.valueOf(request.getParameter("next")):0;
		int number    = JiaoKaUtils.isInteger(request.getParameter("number"))?Integer.valueOf(request.getParameter("number")):0;
		
		logger.info(String.format("openid:%s;next:%s;number:%s", openid,next,number));
		
		if(number > MAX_NUMBER)
			number = MAX_NUMBER;
		if(number==0)
			number = DEFAULT_NUMBER;
		
		ServiceResponse resp = null;
		WeChatAuditLogDao dao = new WeChatAuditLogDao();
		SearchResponse case_ = dao.search(openid, next, number);
		if(case_==null || case_.getDatas()==null)
			resp = new ServiceResponse("system error.");
		else
			resp = new ServiceResponse(case_);
		return resp.toJson();
	}
}
