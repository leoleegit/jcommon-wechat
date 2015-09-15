package org.jcommon.com.wechat;

import java.util.List;

import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.data.Users;

public interface UserManagerListener extends ErrorListener{
	public void onGroup(List<Group> groups);
	public void onGroup(Group group);
	public void onUser(User user);
	public void onUsers(List<User> users);
	public void onUsers(Users users);
}
