package org.jcommon.com.wechat;

import java.util.List;

import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.data.Users;

public interface UserManagerListener extends ErrorListener {
	public void onGroup(HttpRequest request, List<Group> groups);

	public void onGroup(HttpRequest request, Group group);

	public void onUser(HttpRequest request, User user);

	public void onUsers(HttpRequest request, List<User> users);

	public void onUsers(HttpRequest request, Users users);
}
