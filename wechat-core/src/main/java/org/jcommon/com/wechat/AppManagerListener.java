package org.jcommon.com.wechat;

import java.util.Set;

import org.jcommon.com.wechat.data.IP;

public interface AppManagerListener extends ErrorListener{
	public void onIPs(Set<IP> ips);
}
