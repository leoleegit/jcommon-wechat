// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Callback extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private static Logger logger   = Logger.getLogger(Callback.class);
  public static URL init_file_is = Callback.class.getResource("/wechat-log4j.xml");

  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
    logger.info("JcommonWechat running ...");
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
      String nonce = request.getParameter("nonce");
      String echostr = request.getParameter("echostr");
      if (WechatSessionManager.instance().appVerify(signature, timestamp, nonce)) {
        PrintWriter servletOutput = response.getWriter();
        response.setContentType("text/html");
        servletOutput.println(echostr);
      } else {
        logger.warn("request verify failure!");
      }
    } catch (IOException e) { logger.error("", e); }
  }

  @SuppressWarnings("rawtypes")
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  {
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
      if (WechatSessionManager.instance().appVerify(signature, timestamp, nonce)) 
    	  WechatSessionManager.instance().onCallback(signature, timestamp, nonce, post_data);
      else
    	  logger.warn("Illegal Data!");
    } catch (IOException e) {
      logger.error("", e);
    }
  }

  static
  {
    if (init_file_is != null)
      DOMConfigurator.configure(init_file_is);
  }
}