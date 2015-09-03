package org.jcommon.com.wechat.router;

import org.jcommon.com.wechat.data.Token;

public interface RouterHandler {
	
	public void startup();
	
	public void shutdown();
	
	public void onRouter(Router router);
	
	public void onToken(Token token);
	
	public void addRouter(Object ... args);
	
	public void removeRouter(Object ... args);
}