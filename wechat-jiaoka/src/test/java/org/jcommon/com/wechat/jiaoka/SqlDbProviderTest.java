package org.jcommon.com.wechat.jiaoka;

import java.util.List;

import org.jcommon.com.wechat.jiaoka.db.bean.Case;
import org.jcommon.com.wechat.jiaoka.db.bean.WeChatUser;
import org.jcommon.com.wechat.jiaoka.db.dao.CaseDao;
import org.jcommon.com.wechat.jiaoka.db.dao.WeChatUserDao;
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
	}
	
	public static  void CaseDaoTest(){
		List<Case> cases = new CaseDao().searchAllCase(0, 0);
		for(Case c : cases)
			System.out.println(c);
	}

}
