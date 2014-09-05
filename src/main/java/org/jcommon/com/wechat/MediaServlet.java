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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class MediaServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException {
		logger.info("media path is :"+ System.getProperty(WechatSession.WECHATMEDIAPATH));
	}
	
    // patch= /content type(md5)/openID/filename
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		logger.info("path:"+path);
		if(path!=null && path.startsWith("/"))
			path = path.substring(1);
		
		String errormsg = null;
		if(path==null)
			errormsg = "access path is null";
		else{
			String[] ps = path.split("/");
			if(ps.length>2){
				String file_name = ps[2];
				String open_id = ps[1];
				String content_type = ps[0];
				logger.info(String.format("file_name:%s;open_id:%s;content_type:%s", file_name,open_id,content_type));
				content_type = WechatSessionManager.instance().getContent_type_cache().getContentType(content_type);
				logger.info("content_type:"+content_type);
				java.io.File file  = new java.io.File(System.getProperty(WechatSession.WECHATMEDIAPATH),open_id+java.io.File.separator+file_name);
				if(!file.exists()){
					logger.warn("file not found:"+file.getAbsolutePath());
					errormsg = "file not found";
				}else{
					try{
						response.reset();  
						response.setContentType( content_type ); 
			            response.addHeader( "Content-Disposition" ,  "attachment;filename=\""   +   file_name+"\"");  
			            response.addHeader( "Content-Length" ,  ""   +  file.length());  
			            
						InputStream is = new FileInputStream(file);
						OutputStream out = response.getOutputStream();
						
						byte[] b = new byte[1024];
						int nRead;
						while((nRead = is.read(b, 0, 1024))>0){
						   out.write(b, 0, nRead);
						}
						try {
							out.close();
							out.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							throw e1;
						}
						try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							throw e;
						}
					}catch(IOException e){
						logger.error("", e);
						errormsg = e.getMessage();
					}			
				}
			}else{
				errormsg = "error request";
			}		
		}
		if(errormsg!=null){
			logger.info(errormsg);
			response.getWriter().print(errormsg);
		}
	}
}
