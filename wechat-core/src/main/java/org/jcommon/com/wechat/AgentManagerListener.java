package org.jcommon.com.wechat;

import java.util.Set;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Agent;

public interface AgentManagerListener extends ErrorListener{
	public void onAgents(HttpRequest request, Set<Agent> agents);
}
