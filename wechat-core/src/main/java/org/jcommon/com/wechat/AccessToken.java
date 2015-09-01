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
import org.jcommon.com.wechat.data.Event;
import org.jcommon.com.wechat.data.JsonObject;
import org.jcommon.com.wechat.utils.EventType;

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
	    			  outstr = token.toJson();
	    		  }else{logger.warn("app is null");}
	    	  }
	          PrintWriter servletOutput = response.getWriter();
	          response.setContentType("text/html");
	          servletOutput.println(outstr);
	      } else {
	          logger.warn("request verify failure!");
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
	        	Token token = new Token(post_data);
	        	String access_token = token.getAccess_token();
	        	long   expires_in   = token.getExpires_in();
	        	if(access_token==null){
	        		logger.warn("access_token is null");
	        		return;
	        	}
	        	
	        	Event event = new Event(null);
	     	    event.setAccess_token(access_token);
	     	    event.setExpires_in(expires_in);
	     	    event.setToUserName(token.getWechatID());
	     	    event.setMsgType(EventType.access_token.toString());
	    	    WechatSessionManager.instance().onCallback(signature, timestamp, nonce, event.toXml());
	    	}
	        else
	    	    logger.warn("Illegal Data!");
	    } catch (IOException e) {
	        logger.error("", e);
	    }
	  }

	  class Token extends JsonObject{
		  private String access_token;
		  private long expires_in;
		  private String wechatID;
		  
		  public Token(String access_token,long expires_in){
			  this.access_token = access_token;
			  this.expires_in   = expires_in;
		  }
		  
		  public Token(String json){
			  super(json);
		  }
		  
		  public String getAccess_token() {
			  return access_token;
		  }

		  public void setAccess_token(String access_token) {
			  this.access_token = access_token;
		  }
		 
		  public long getExpires_in() {
			  return expires_in;
		  }
		 
		  public void setExpires_in(long expires_in) {
			  this.expires_in = expires_in;
		  }

		  public String getWechatID() {
			  return wechatID;
		  }

		  public void setWechatID(String wechatID) {
			  this.wechatID = wechatID;
		  }
	  }
}