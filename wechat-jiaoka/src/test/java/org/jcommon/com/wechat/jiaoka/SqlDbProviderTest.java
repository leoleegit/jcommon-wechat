package org.jcommon.com.wechat.jiaoka;

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
		
		//CaseDaoTest();
		//WeChatAuditLogDaoTest();
		//WeChatUserDaoTest();
		//new WeChatUserDao().updatePhoneNumber("15919065160", "of-YetzJFYxGTltb4eCvgccHzHF0");
		//System.out.println(DbProviderFaceory.createDbProvider().selectCount("SELECT * FROM wechat_case"));
		
		int i=0,j=0;
		i++;
		++j;
		System.out.println(i);
		System.out.println(j);
	}
	
	public static  void CaseDaoTest(){
		SearchResponse cases = new CaseDao().searchCase(null,null,"15919065", 0, 20);
		for(JsonObject c : cases.getDatas())
			System.out.println(c.toJson());
	}
	
	public static void WeChatAuditLogDaoTest(){
		SearchResponse cases = new WeChatAuditLogDao().search(null,2, 30);
		for(JsonObject c : cases.getDatas())
			System.out.println(c.toJson());
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
