package org.jcommon.com.wechat.jiaoka;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.jcommon.com.wechat.data.JsonObject;
import org.jcommon.com.wechat.jiaoka.db.DbProviderFaceory;
import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.db.bean.SearchResponse;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatUser;
import org.jcommon.com.wechat.jiaoka.db.dao.CaseDao;
import org.jcommon.com.wechat.jiaoka.db.dao.WeChatAuditLogDao;
import org.jcommon.com.wechat.jiaoka.db.dao.WeChatUserDao;
import org.jcommon.com.wechat.jiaoka.db.dao.WechatAgentDao;
import org.jcommon.com.wechat.jiaoka.db.sql.SqlDbProvider;

public class SqlDbProviderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String sql  = "select name  from1   hello   from User_in_role nt where nt.UserID=? ";
//	
//		for(String s : SqlDbProvider.getSearchNames(sql))
//			System.out.println(s);
//		
//		String openid = "of-YetzJFYxGTltb4eCvgccHzHF0";
////		WeChatUser user = new WeChatUserDao().searchUserByOpenid(openid);
//		//System.out.println(new WeChatUserDao().searchUserIDByOpenid(openid));
//		
//		String sql = "INSERT INTO wechatauditlog(type,from,logstr,create_time) VALUES (?,?,?,?)";
//		for(String s : SqlDbProvider.getParameters(sql))
//			System.out.println(s);
		
		CaseDaoTest();
		//WeChatAuditLogDaoTest();
		//WeChatUserDaoTest();
		//new WeChatUserDao().updatePhoneNumber("15919065160", "of-YetzJFYxGTltb4eCvgccHzHF0");
		//System.out.println(DbProviderFaceory.createDbProvider().selectCount("SELECT * FROM wechat_case"));
		
	}
	
	public static  void CaseDaoTest(){
		Timestamp from_t = null;
		Timestamp to_t   = null;
		
		try {
			Date from_d = org.jcommon.com.util.DateUtils.getDate(null, "yyyy-MM-dd HH:mm");
			Date to_d   = org.jcommon.com.util.DateUtils.getDate("2015-10-09 11:43", "yyyy-MM-dd HH:mm");
			if(from_d!=null && to_d!=null){
				from_t      = new Timestamp(from_d.getTime());
				to_t        = new Timestamp(to_d.getTime());
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SearchResponse cases = new CaseDao().searchCase(null,null,null,null,from_t,to_t, 0, 20);
		for(JsonObject c : cases.getDatas())
			System.out.println(c.toJson());
	}
	
	public static void WeChatAuditLogDaoTest(){
//		SearchResponse cases = new WeChatAuditLogDao().search(null,2, 30);
//		for(JsonObject c : cases.getDatas())
//			System.out.println(c.toJson());
	}
	
	public static void WechatAgentDaoTest(){
		System.out.println(new WechatAgentDao().searchAgentByOpenid("of-YetzJFYxGTltb4eCvgccHzHF0"));
	}
	
	public static void WeChatUserDaoTest(){
		SearchResponse cases = new WeChatUserDao().searchAllUser(null,"leolee 噢噢", 0, 20);
		for(JsonObject c : cases.getDatas())
			System.out.println(c.toJson());
	}

}
