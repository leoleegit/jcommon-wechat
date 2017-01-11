package org.jcommon.com.wechat.manage.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class Service {
	protected static final int MAX_NUMBER = 100;
	protected static final int DEFAULT_NUMBER = 21;
	protected Logger logger = Logger.getLogger(getClass());
	
	protected String pritelnParameter(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Map<?,?> map = request.getParameterMap();
	    for (Iterator<?> i$ = map.keySet().iterator(); i$.hasNext(); ) { Object key = i$.next();
	        for (String value : (String[])map.get(key)) {
	        	sb.append(key).append("\t:").append(value).toString();
	            logger.info(new StringBuilder().append(key).append("\t:").append(value).toString());
	        }
	    }
	    logger.info("Referer:"+request.getHeader("Referer"));
	    return sb.toString();
	}
	
	protected String getRequestURL(HttpServletRequest request){
        StringBuffer sb = request.getRequestURL();
        sb.append("?");
        Enumeration<?> names = request.getParameterNames();
        while (names.hasMoreElements()) {
	        String name = (String)names.nextElement();
	        if(!"code".equalsIgnoreCase(name))
	      	    sb.append(name).append("=").append(request.getParameter(name)).append("&");
        }

        if ((sb.lastIndexOf("&") == sb.length() - 1) && (sb.length() > 0))
        	sb.deleteCharAt(sb.length() - 1);
        if ((sb.lastIndexOf("?") == sb.length() - 1) && (sb.length() > 0))
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
	protected String getParameterData(InputStream is){
		try {
		    StringBuilder xml = new StringBuilder();
		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    char[] cbuf = new char[1024];
		    int nRead;
		    while ((nRead=reader.read(cbuf))!=-1) {
		        xml.append(cbuf, 0, nRead);
		    }
		    reader.close();
		    String post_data = xml.toString();
		    logger.info(post_data);
		    
			return post_data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			return null;
		}
	}
}

