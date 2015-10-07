package org.jcommon.com.wechat.jiaoka.service;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.db.bean.ServiceResponse;
import org.jcommon.com.wechat.jiaoka.db.dao.CaseDao;
import org.jcommon.com.wechat.jiaoka.utils.JiaoKaUtils;

@Path("case")
public class CaseService extends Service{
	
	
	@GET 
	@Path("search")
	@Produces("text/plain;charset=UTF-8")  
	public String searchCase(@Context HttpServletRequest request){
		String status = request.getParameter("status"); 
		String openid = request.getParameter("openid");	
		
		int next      = JiaoKaUtils.isInteger(request.getParameter("next"))?Integer.valueOf(request.getParameter("next")):0;
		int number    = JiaoKaUtils.isInteger(request.getParameter("number"))?Integer.valueOf(request.getParameter("number")):0;
		
		logger.info(String.format("status:%s;openid:%s;next:%s;number:%s", status,openid,next,number));
		
		if(number > MAX_NUMBER)
			number = MAX_NUMBER;
		if(number==0)
			number = DEFAULT_NUMBER;
		
		ServiceResponse resp = null;
		CaseDao dao = new CaseDao();
		if(status==null && openid==null){
			List<Case> case_ = dao.searchAllCase(next, number);
			if(case_==null)
				resp = new ServiceResponse("system error.");
			else
				resp = new ServiceResponse(case_);
		}else if(status!=null && openid!=null){
			List<Case> case_ = dao.searchAllCase(status,openid, next, number);
			if(case_==null)
				resp = new ServiceResponse("system error.");
			else
				resp = new ServiceResponse(case_);
		}else if(openid!=null){
			List<Case> case_ = dao.searchAllCaseByOpenid(openid, next, number);
			if(case_==null)
				resp = new ServiceResponse("system error.");
			else
				resp = new ServiceResponse(case_);
		}else if(status!=null){
			List<Case> case_ = dao.searchAllCaseByStatus(status, next, number);
			if(case_==null)
				resp = new ServiceResponse("system error.");
			else
				resp = new ServiceResponse(case_);
		}
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
