package org.jcommon.com.wechat.test;

import java.net.URL;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.system.SystemListener;
import org.jcommon.com.wechat.Callback;

public class TestBase implements SystemListener{
	public static URL init_file_is = Callback.class.getResource("/wechat-log4j.xml");

	static{
	    if (init_file_is != null)
	      DOMConfigurator.configure(init_file_is);
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}
}
