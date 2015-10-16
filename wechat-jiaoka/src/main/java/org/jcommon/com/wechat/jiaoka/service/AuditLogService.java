package org.jcommon.com.wechat.jiaoka.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

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
		pritelnParameter(request);
	    
		String openid       = request.getParameter("openid"); 
		String type         = request.getParameter("type");	
		String from         = request.getParameter("from");
		String to 			= request.getParameter("to");
		
		int next      = JiaoKaUtils.isInteger(request.getParameter("next"))?Integer.valueOf(request.getParameter("next")):0;
		int number    = JiaoKaUtils.isInteger(request.getParameter("number"))?Integer.valueOf(request.getParameter("number")):0;
		
		
		
		if(number > MAX_NUMBER)
			number = MAX_NUMBER;
		if(number==0)
			number = DEFAULT_NUMBER;
		
		Timestamp from_t = null;
		Timestamp to_t   = null;
		
		try {
			Date from_d = org.jcommon.com.util.DateUtils.getDate(from, "yyyy-MM-dd HH:mm");
			Date to_d   = org.jcommon.com.util.DateUtils.getDate(to, "yyyy-MM-dd HH:mm");
			if(from_d!=null && to_d!=null){
				from_t      = new Timestamp(from_d.getTime());
				to_t        = new Timestamp(to_d.getTime());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		ServiceResponse resp = null;
		WeChatAuditLogDao dao = new WeChatAuditLogDao();
		SearchResponse case_ = dao.search(openid,type,from_t,to_t,next, number);
		if(case_==null || case_.getDatas()==null)
			resp = new ServiceResponse("system error.");
		else
			resp = new ServiceResponse(case_);
		return resp.toJson();
	}
}
