package org.jcommon.com.wechat;

import java.util.Set;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.IP;

public interface AppManagerListener extends ErrorListener{
	public void onIPs(HttpRequest request, Set<IP> ips);
}
