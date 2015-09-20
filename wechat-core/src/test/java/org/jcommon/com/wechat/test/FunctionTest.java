package org.jcommon.com.wechat.test;

import java.io.File;
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
import org.jcommon.com.wechat.data.Articles;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.IP;
import org.jcommon.com.wechat.data.MaterialsCount;
import org.jcommon.com.wechat.data.Media;
import org.jcommon.com.wechat.data.Mpnews;
import org.jcommon.com.wechat.data.OpenID;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.data.Users;
import org.jcommon.com.wechat.data.Video;
import org.jcommon.com.wechat.utils.Lang;
import org.jcommon.com.wechat.utils.MD5;
import org.jcommon.com.wechat.utils.MediaType;
import org.jcommon.com.wechat.utils.MsgType;

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
		String access_token = "j2F1vjJtpIP3tkhnkizyzpTMCoZNNkTA5xbxX5NbyvweUkfYgfvXMpL0llyNdcH5QJllg_AALRNPSzsdfyt1LTU-cWF-gERaCvoBOrVu4zQ";
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
	    //Media media          = new Media();
	   // media.setMedia_id("QJB2K1gqwHLYZILw2kU2c8RbQ1X44GE8x4OeCRpFILk");
	   //manager.downloadMedia(media, test);
	    //MediaManager.getMedia_factory().getMediaFromUrl("/634546cf1fbb14c2a8abc986dba3da6e/MN2qp-B2wYMUbdJyovde-Ey1HRLb179J--KQ_0VXrKTH_sTLB6lixkF8pCmqvYsH");
	    //media.setUrl("/634546cf1fbb14c2a8abc986dba3da6e/MN2qp-B2wYMUbdJyovde-Ey1HRLb179J--KQ_0VXrKTH_sTLB6lixkF8pCmqvYsH.jpg");
	    //media.setMedia(new File(System.getProperty("user.dir"),"test.jpg"));
	    //media.setType(MsgType.image.name());
	    //manager.uploadNewsImg(media, test);
	    
	    manager.getMaterials(MediaType.image.name(), 0, 10, test);
	    //manager.getMaterialCount(test);
	    //manager.getMaterial(media, test);
	    //manager.delMaterial(media, test);
	    
	    //manager.uploadMedia(media, test);
	    //Video video = new Video("VIDEO_TITLE","INTRODUCTION");
	    //video.setMedia(new File(System.getProperty("user.dir"),"d22acda6f7a24ec85f80ba2caa3c3f9d.mp4"));
	   // manager.uploadMaterialMedia(video,test);
	    List<Articles> articles = new ArrayList<Articles>();
	    Articles art = new Articles("TITLE11","content","digest","http://demo.protel.com.hk",
	  		  "leolee",1);
	    art.setThumb_media_id("fOE5ZBSeEpfRGQcGJjbocObGlHdPTsaC5fL76jiAtDM");
	    articles.add(art);
//	    art = new Articles("TITLE2","content","digest","http://demo.protel.com.hkm",
//		  		  "leolee");
//	    art.setThumb_media_id("7TlXZ_bwQbQD-r8hoJlinVWAZ_lIsAWLu8tvsHdfdSM");
//	    articles.add(art);
//	    art = new Articles("TITLE4","content","digest","http://demo.protel.com.hk",
//		  		  "leolee");
//	    art.setThumb_media_id("7TlXZ_bwQbQD-r8hoJlinZJAaD8kdDofqLdQUDjQhvg");
//	    articles.add(art);
	    //Mpnews mpnews = new Mpnews(articles);
	    //manager.uploadNews(mpnews, test);
	    //art.setMedia_id("QJB2K1gqwHLYZILw2kU2c8RbQ1X44GE8x4OeCRpFILk");
	    //manager.updateMaterial(art, 2, test);
	}

	@Override
	public void onError(HttpRequest reqeust,Error error) {
		// TODO Auto-generated method stub
		logger.info(error.toJson());
	}

	@Override
	public void onIPs(HttpRequest reqeust,Set<IP> ips) {
		// TODO Auto-generated method stub
		for(IP ip : ips)
			System.out.print(ip.getIp() + ";");
	}

	@Override
	public void onAgents(HttpRequest reqeust,Set<Agent> agents) {
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
	public void onGroup(HttpRequest request, List<Group> groups) {
		// TODO Auto-generated method stub
		for(Group group : groups)
			onGroup(request,group);
	}

	@Override
	public void onGroup(HttpRequest request, Group group) {
		// TODO Auto-generated method stub
		logger.info(group.toJson());
	}

	@Override
	public void onUser(HttpRequest request, User user) {
		// TODO Auto-generated method stub
		logger.info(user.toJson());
	}

	@Override
	public void onUsers(HttpRequest request, List<User> users) {
		// TODO Auto-generated method stub
		for(User group : users)
			onUser(request,group);
	}

	@Override
	public void onUsers(HttpRequest request, Users users) {
		// TODO Auto-generated method stub
		logger.info(users.getCount() + " / " + users.getTotal());
		for(OpenID user : users.getOpenids())
			logger.info(user.toJson());
		for(User group : users.getUsers())
			onUser(request,group);
	}

	@Override
	public void onMedia(HttpRequest request, Media media) {
		// TODO Auto-generated method stub
		logger.info(media.toJson());
	}

	@Override
	public void onMaterialsCount(HttpRequest request, MaterialsCount material) {
		// TODO Auto-generated method stub
		logger.info(material.toJson());
	}

	@Override
	public void onMpnews(HttpRequest request, Mpnews news) {
		// TODO Auto-generated method stub
		logger.info(news.toJson());
	}

}
