package org.jcommon.com.wechat.jiaoka.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jcommon.com.wechat.jiaoka.db.bean.ServiceResponse;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatUser;
import org.jcommon.com.wechat.jiaoka.db.dao.WeChatUserDao;
import org.jcommon.com.wechat.jiaoka.utils.JiaoKaUtils;

@Path("user")
public class UserService extends Service{
	
	@GET 
	@Path("search")
	@Produces("text/plain;charset=UTF-8")  
	public String searchUser(@Context HttpServletRequest request){
		int next      = JiaoKaUtils.isInteger(request.getParameter("next"))?Integer.valueOf(request.getParameter("next")):0;
		int number    = JiaoKaUtils.isInteger(request.getParameter("number"))?Integer.valueOf(request.getParameter("number")):0;
		
		logger.info(String.format("next:%s;number:%s", next,number));
		if(number > MAX_NUMBER)
			number = MAX_NUMBER;
		if(number==0)
			number = DEFAULT_NUMBER;
		
		ServiceResponse resp = null;
		WeChatUserDao dao    = new WeChatUserDao();
		List<WeChatUser> users = dao.searchAllUser(next, number);
		if(users==null){
			resp = new ServiceResponse("system error.");
		}else{
			resp = new ServiceResponse(users);
		}
		return resp.toJson();
	}
}
