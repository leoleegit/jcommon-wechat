package org.jcommon.com.wechat.jiaoka.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.db.bean.SearchResponse;
import org.jcommon.com.wechat.jiaoka.db.bean.ServiceResponse;
import org.jcommon.com.wechat.jiaoka.db.dao.CaseDao;
import org.jcommon.com.wechat.jiaoka.utils.JiaoKaUtils;

@Path("case")
public class CaseService extends Service{
	
	
	@GET 
	@Path("search")
	@Produces("text/plain;charset=UTF-8")  
	public String searchCase(@Context HttpServletRequest request){
		pritelnParameter(request);
		String status       = request.getParameter("status"); 
		String phone_number = request.getParameter("phone_number");	
		String nickname     = request.getParameter("nickname");	
		String addr         = request.getParameter("addr");	
		String from         = request.getParameter("from");
		String to 			= request.getParameter("to");
		
		int next      = JiaoKaUtils.isInteger(request.getParameter("next"))?Integer.valueOf(request.getParameter("next")):0;
		int number    = JiaoKaUtils.isInteger(request.getParameter("number"))?Integer.valueOf(request.getParameter("number")):0;
		
		if(number > MAX_NUMBER)
			number = MAX_NUMBER;
		if(number==0)
			number = DEFAULT_NUMBER;
		if("all".equals(status))
			status = null;
		
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
		CaseDao dao = new CaseDao();
		SearchResponse case_ = dao.searchCase(status,nickname,phone_number,addr,from_t,to_t,next,number);
		if(case_==null || case_.getDatas()==null)
			resp = new ServiceResponse("system error.");
		else
			resp = new ServiceResponse(case_);
		return resp.toJson();
	}
	
	@GET 
	@Path("update")
	@Produces("text/plain;charset=UTF-8")  
	public String updateCase(@Context HttpServletRequest request){
		String status = request.getParameter("status");	
		String handle_agent = request.getParameter("handle_agent"); 
		String note = request.getParameter("note");	
		String case_id = request.getParameter("case_id");	
		Timestamp update_time = new Timestamp(System.currentTimeMillis());
		
		
		logger.info(String.format("status:%s;handle_agent:%s;note:%s;case_id:%s", status,handle_agent,note,case_id));
		
		ServiceResponse resp = null;
		CaseDao dao = new CaseDao();
		boolean succ = dao.updateCase(status, handle_agent, note, case_id, update_time);
		if(succ)
			resp = new ServiceResponse((Case)null);
		else
			resp = new ServiceResponse("system error.");
		return resp.toJson();
	}
}
