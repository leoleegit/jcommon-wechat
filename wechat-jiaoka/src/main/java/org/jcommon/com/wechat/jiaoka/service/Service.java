package org.jcommon.com.wechat.jiaoka.service;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class Service {
	protected static final int MAX_NUMBER = 100;
	protected static final int DEFAULT_NUMBER = 21;
	protected Logger logger = Logger.getLogger(getClass());
	
	protected void pritelnParameter(HttpServletRequest request) {
		Map<?,?> map = request.getParameterMap();
	    for (Iterator<?> i$ = map.keySet().iterator(); i$.hasNext(); ) { Object key = i$.next();
	        for (String value : (String[])map.get(key)) {
	            logger.info(new StringBuilder().append(key).append("\t:").append(value).toString());
	        }
	    }
	}
}
