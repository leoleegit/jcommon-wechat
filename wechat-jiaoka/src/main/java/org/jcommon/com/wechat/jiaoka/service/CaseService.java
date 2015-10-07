package org.jcommon.com.wechat.jiaoka.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.db.bean.ServiceResponse;
import org.jcommon.com.wechat.jiaoka.db.dao.CaseDao;
import org.jcommon.com.wechat.jiaoka.utils.JiaoKaUtils;

@Path("case")
public class CaseService {
	private static final int MAX_NUMBER = 100;
	private static final int DEFAULT_NUMBER = 20;
	private Logger logger = Logger.getLogger(getClass());
	
	private CaseDao dao = new CaseDao();
	
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
}
