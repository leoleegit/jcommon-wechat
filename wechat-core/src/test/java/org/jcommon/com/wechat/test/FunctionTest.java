package org.jcommon.com.wechat.test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.AgentManager;
import org.jcommon.com.wechat.AgentManagerListener;
import org.jcommon.com.wechat.AppManager;
import org.jcommon.com.wechat.AppManagerListener;
import org.jcommon.com.wechat.MediaManager;
import org.jcommon.com.wechat.MediaManagerListener;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.UserManager;
import org.jcommon.com.wechat.UserManagerListener;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Agent;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.IP;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.OpenID;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.data.Users;
import org.jcommon.com.wechat.utils.Lang;
import org.jcommon.com.wechat.utils.MD5;

public class FunctionTest extends TestBase implements AppManagerListener,
	AgentManagerListener,
	UserManagerListener,
	MediaManagerListener,
	RequestCallback{

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		FunctionTest test = new FunctionTest();
		String access_token = "q-P1N1mGRqpcMOByeI1jw33vxqbyMng2jfotZUGJ_xYZstZAl5yrvJv_dcooT1MaoGnssJvT5mMSB3A_YUZBiWkdlRb93dpuCSVQP5XyifY";
		App app = new App(access_token,"wx742941360129cd17", "37492ad273076440c0f123716865e1da", "spotlight-wechat");
	    WechatSession session = new WechatSession("gh_f49bb9a333b3", app, null);
	  
//	    AppManager appManager = new AppManager(session);  
//	    appManager.getIps(test);
	    
	    //AgentManager manager = new AgentManager(session);
	    //manager.getAgent(test);
	    //String kf_account,String nickname,String password
	    //Agent agent = new Agent("leolee@leolee-wechat","客服1",MD5.getMD5("leolee".getBytes()));
	    //manager.addAgent(agent, test);
	    
	    //UserManager manager = new UserManager(session);
	    //manager.getGroups(test);
	    //manager.createGroup(new Group(null,"test1"), test);
	    //manager.updateGroupName(new Group("100","test2"), test);
	    //manager.delGroup(new Group("100","test2"), test);
	    //manager.getGroupByUser(new OpenID("of-Yet8DPKkGqVfu_Ph4t3ty8P4A"), test);
	    
//	    List<User> users = new ArrayList<User>();
	    //User user = new User(null);
	    //user.setOpenid("of-YetzJFYxGTltb4eCvgccHzHF0");
	    //user.setLanguage(Lang.zh_CN.name());
//	    users.add(user);
//	    user = new User(null);
//	    user.setOpenid("of-Yet2V8ymhYEHi1N56AIEPgbZc");
//	    user.setLanguage(Lang.zh_CN.name());
//	    users.add(user);
//	    manager.getUserInfo(user, test);
	    //manager.getUserInfos(users, test);
//	    manager.getUsers(null, test);
//	    User user = new User();
//	    user.setOpenid("of-YetzJFYxGTltb4eCvgccHzHF0");
//	    user.setRemark("测试");
//	    manager.updateRemark(user, test);
	    
	    MediaManager manager = new MediaManager(session);
	    Media media          = new Media();
	    media.setMedia_id("MN2qp-B2wYMUbdJyovde-Ey1HRLb179J--KQ_0VXrKTH_sTLB6lixkF8pCmqvYsH");
	    manager.downloadMedia(media, test);
	}

	@Override
	public void onError(Error error) {
		// TODO Auto-generated method stub
		logger.info(error.toJson());
	}

	@Override
	public void onIPs(Set<IP> ips) {
		// TODO Auto-generated method stub
		for(IP ip : ips)
			System.out.print(ip.getIp() + ";");
	}

	@Override
	public void onAgents(Set<Agent> agents) {
		// TODO Auto-generated method stub
		for(Agent ip : agents)
			System.out.print(ip.getKf_account() + ";");
	}

	@Override
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		logger.info(sResult);
	}

	@Override
	public void onFailure(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		logger.info(sResult);
	}

	@Override
	public void onTimeout(HttpRequest reqeust) {
		// TODO Auto-generated method stub
		logger.info(reqeust);
	}

	@Override
	public void onException(HttpRequest reqeust, Exception e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
	}

	@Override
	public void onGroup(List<Group> groups) {
		// TODO Auto-generated method stub
		for(Group group : groups)
			onGroup(group);
	}

	@Override
	public void onGroup(Group group) {
		// TODO Auto-generated method stub
		logger.info(group.toJson());
	}

	@Override
	public void onUser(User user) {
		// TODO Auto-generated method stub
		logger.info(user.toJson());
	}

	@Override
	public void onUsers(List<User> users) {
		// TODO Auto-generated method stub
		for(User group : users)
			onUser(group);
	}

	@Override
	public void onUsers(Users users) {
		// TODO Auto-generated method stub
		logger.info(users.getCount() + " / " + users.getTotal());
		for(OpenID user : users.getOpenids())
			logger.info(user.toJson());
		for(User group : users.getUsers())
			onUser(group);
	}

	@Override
	public void onMedia(Media media) {
		// TODO Auto-generated method stub
		logger.info(media.toJson());
	}

}
