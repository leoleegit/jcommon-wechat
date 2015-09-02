package org.jcommon.com.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jcommon.com.wechat.data.App;
import org.jcommon.com.wechat.data.Token;

public class AccessToken extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger   = Logger.getLogger(AccessToken.class);
	
	public void init(ServletConfig config)
		    throws ServletException{
	    super.init(config);
	    logger.info("AccessToken up ...");
    }
	
	@SuppressWarnings("rawtypes")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	    try {
	    	request.setCharacterEncoding("utf-8");
	    	response.setCharacterEncoding("utf-8");
	    	
	      Map map = request.getParameterMap();
	      for (Iterator<?> i$ = map.keySet().iterator(); i$.hasNext(); ) { Object key = i$.next();
	        for (String value : (String[])map.get(key)) {
	          logger.info(new StringBuilder().append(key).append("\t:").append(value).toString());
	        }
	      }
	      String signature = request.getParameter("signature");
	      String timestamp = request.getParameter("timestamp");
	      String nonce     = request.getParameter("nonce");
	      String wechatID  = request.getParameter("wechatID");
	      if (WechatSessionManager.instance().appVerify(signature, timestamp, nonce)) {
	    	  WechatSession session = WechatSessionManager.instance().getWechatSession(wechatID);
	    	  String outstr         = "";
	    	  if(session!=null){
	    		  final App app = session.getApp();
	    		  if(app!=null){
	    			  Token token = new Token(app.getAccess_token(),app.getExpires());
	    			  token.setWechatID(wechatID);
	    			  outstr = token.toJson();//{access_token:'AFADFADFAF',expires_in:7200,wechatID:'gf-fdfasdfafa'}
	    		  }else{logger.warn("app is null");}
	    	  }
	          PrintWriter servletOutput = response.getWriter();
	          response.setContentType("text/html");
	          servletOutput.println(outstr);
	          return;
	      } else {
	          logger.warn("request verify failure!");
	          PrintWriter servletOutput = response.getWriter();
	          response.setContentType("text/html");
	          servletOutput.println("Illegal Request");
	      }
	    } catch (IOException e) { logger.error("", e); }
	   
	}
	
	@SuppressWarnings("rawtypes")
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
	    try {
	    	request.setCharacterEncoding("utf-8");
	        response.setCharacterEncoding("UTF-8");
	        StringBuilder xml = new StringBuilder();
	        BufferedReader reader = request.getReader();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            xml.append(line);
	        }
	        reader.close();
	      
	        String post_data = xml.toString();
	        post_data = org.jcommon.com.util.CoderUtils.decode(post_data);
	        logger.info(new StringBuilder().append(request.getRemoteHost()).append(" wechat:").append(post_data).toString());

	        Map map = request.getParameterMap();
	        for (Iterator<?> i$ = map.keySet().iterator(); i$.hasNext(); ) { Object key = i$.next();
	          for (String value : (String[])map.get(key)) {
	              logger.info(new StringBuilder().append(key).append("\t:").append(value).toString());
	          }
	        }
	      
	        response.getWriter().println("");
	        String signature = request.getParameter("signature");
	        String timestamp = request.getParameter("timestamp");
	        String nonce = request.getParameter("nonce");
	        if (WechatSessionManager.instance().appVerify(signature, timestamp, nonce)){ 
	    	    WechatSessionManager.instance().onToken(signature, timestamp, nonce, post_data);
	    	}
	        else
	    	    logger.warn("Illegal Data!");
	    } catch (IOException e) {
	        logger.error("", e);
	    }
	  }

}