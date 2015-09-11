package org.jcommon.com.wechat.test;

import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.AgentManager;
import org.jcommon.com.wechat.AgentManagerListener;
import org.jcommon.com.wechat.AppManager;
import org.jcommon.com.wechat.AppManagerListener;
import org.jcommon.com.wechat.RequestCallback;
import org.jcommon.com.wechat.WechatSession;
import org.jcommon.com.wechat.data.Agent;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.IP;
import org.jcommon.com.wechat.utils.MD5;

public class FunctionTest extends TestBase implements AppManagerListener,
	AgentManagerListener,
	RequestCallback{

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		FunctionTest test = new FunctionTest();
		String access_token = "FmkU4ZdIi8DQRX9dkIo5RKE4ba0XVpLLStKW4FtHPWSf7oGE2_fdhJjQClZAL25YKDMY4F212iDlgmzfWiXbPOa0qmjtDST-B3CWhavGc5U";
		App app = new App(access_token,"wx742941360129cd17", "37492ad273076440c0f123716865e1da", "spotlight-wechat");
	    WechatSession session = new WechatSession("gh_f49bb9a333b3", app, null);
	  
//	    AppManager appManager = new AppManager(session);  
//	    appManager.getIps(test);
	    
	    AgentManager manager = new AgentManager(session);
	    //manager.getAgent(test);
	    //String kf_account,String nickname,String password
	    //Agent agent = new Agent("leolee@leolee-wechat","客服1",MD5.getMD5("leolee".getBytes()));
	    //manager.addAgent(agent, test);
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
		
	}

	@Override
	public void onTimeout(HttpRequest reqeust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(HttpRequest reqeust, Exception e) {
		// TODO Auto-generated method stub
		
	}

}
