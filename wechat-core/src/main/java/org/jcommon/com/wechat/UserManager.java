package org.jcommon.com.wechat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.wechat.data.Error;
import org.jcommon.com.wechat.data.Group;
import org.jcommon.com.wechat.data.OpenID;
import org.jcommon.com.wechat.data.User;
import org.jcommon.com.wechat.data.Users;
import org.jcommon.com.wechat.data.format.OutGroup;
import org.jcommon.com.wechat.data.format.OutUser;
import org.jcommon.com.wechat.utils.Lang;

public class UserManager extends ResponseHandler {
	private Logger logger = Logger.getLogger(getClass());
	private WechatSession session;
	private Map<String, User> user_cache = new HashMap<String, User>();
	private static final String CHECKTIME = "0359";
	private static final long expires = 12;
	private Timer expires_timer;

	public UserManager(WechatSession session) {
		this.session = session;
	}

	@Override
	public void onError(HttpRequest paramHttpRequest, Error paramError) {
		// TODO Auto-generated method stub
		if (paramHttpRequest.getAttibute(paramHttpRequest) != null) {
			UserManagerListener listener = (UserManagerListener) paramHttpRequest
					.getAttibute(paramHttpRequest);
			listener.onError(paramHttpRequest, paramError);
		}
	}

	@Override
	public void onOk(HttpRequest paramHttpRequest, Object paramObject) {
		// TODO Auto-generated method stub
		if (paramObject instanceof Group) {
			Group group = (Group) paramObject;
			// logger.info(group.getGroups().size());
			if (paramHttpRequest.getAttibute(paramHttpRequest) != null) {
				UserManagerListener listener = (UserManagerListener) paramHttpRequest
						.getAttibute(paramHttpRequest);
				listener.onGroup(paramHttpRequest, group.getGroups());
			}
		} else if (paramObject instanceof OutGroup) {
			OutGroup group = (OutGroup) paramObject;
			logger.info(group.getGroupid());
			if (paramHttpRequest.getAttibute(paramHttpRequest) != null) {
				UserManagerListener listener = (UserManagerListener) paramHttpRequest
						.getAttibute(paramHttpRequest);
				listener.onGroup(paramHttpRequest, new Group(
						group.getGroupid(), null));
			}
		} else if (paramObject instanceof User) {
			User user = (User) paramObject;
			logger.info(user);
			if (user.getOpenid() != null) {
				user_cache.put(user.getOpenid(), user);
			}
			if (paramHttpRequest.getAttibute(paramHttpRequest) != null) {
				UserManagerListener listener = (UserManagerListener) paramHttpRequest
						.getAttibute(paramHttpRequest);
				listener.onUser(paramHttpRequest, user);
			}
		} else if (paramObject instanceof OutUser) {
			OutUser user = (OutUser) paramObject;
			logger.info(user);
			if (paramHttpRequest.getAttibute(paramHttpRequest) != null) {
				UserManagerListener listener = (UserManagerListener) paramHttpRequest
						.getAttibute(paramHttpRequest);
				listener.onUsers(paramHttpRequest, user.getUser_info_list());
			}
		} else if (paramObject instanceof Users) {
			Users user = (Users) paramObject;
			logger.info(user);
			if (paramHttpRequest.getAttibute(paramHttpRequest) != null) {
				UserManagerListener listener = (UserManagerListener) paramHttpRequest
						.getAttibute(paramHttpRequest);
				listener.onUsers(paramHttpRequest, user);
			}
		}
	}

	public User getUserInfo(User user) {
		String open_id = user.getOpenid();

		if (open_id != null && user_cache.containsKey(open_id)) {
			return user_cache.get(open_id);
		}
		return null;
	}

	/**
	 * =========================== User Manager Start===========================
	 */
	/**
	 * require : openID
	 */
	public HttpRequest getUserInfo(User user, UserManagerListener listener) {
		if (user == null || user.getOpenid() == null) {
			logger.warn("user or user id is null");
			return null;
		}
		String open_id = user.getOpenid();

		Lang lang = Lang.getLang(user.getLanguage());
		HttpRequest request = RequestFactory.userInfoRequest(this, session
				.getApp().getAccess_token(), open_id, lang);
		if (listener != null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, User.class);
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	/**
	 * require : openID
	 */
	public HttpRequest getUserInfos(List<User> users,
			UserManagerListener listener) {
		if (users == null || users.size() == 0) {
			logger.warn("user or user id is null");
			return null;
		}
		OutUser user_format = new OutUser();
		user_format.setUser_list(users);
		HttpRequest request = RequestFactory.userInfosRequest(this, session
				.getApp().getAccess_token(), user_format.toJson());
		if (listener != null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, OutUser.class);
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	/**
	 * require : openID & remark
	 */
	public HttpRequest updateRemark(User user, RequestCallback callback) {
		if (user == null || user.getOpenid() == null) {
			logger.warn("user or user id is null");
			return null;
		}
		HttpRequest request = RequestFactory.updateRemarkRequest(callback,
				session.getApp().getAccess_token(), user.toJson());
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	public HttpRequest getUsers(String next_openid, UserManagerListener listener) {
		HttpRequest request = RequestFactory.getUsersReqeust(this, session
				.getApp().getAccess_token(), next_openid);
		if (listener != null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, Users.class);
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	/**
	 * =========================== User Manager End=============================
	 */

	/**
	 * =========================== Group Manager
	 * Start===========================
	 */
	/**
	 * require : group name
	 */
	public HttpRequest createGroup(Group group, RequestCallback callback) {
		if (group == null) {
			logger.warn("group or group id is null");
			return null;
		}
		OutGroup group_format = new OutGroup();
		group_format.setGroup(group);
		HttpRequest request = RequestFactory
				.createGroupsReqeust(callback, session.getApp()
						.getAccess_token(), group_format.updateFormat());
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	public HttpRequest getGroups(UserManagerListener listener) {
		HttpRequest request = RequestFactory.getGroupsReqeust(this, session
				.getApp().getAccess_token());
		if (listener != null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, Group.class);
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	/**
	 * require : openid
	 */
	public HttpRequest getGroupByUser(OpenID id, UserManagerListener listener) {
		OutGroup group_format = new OutGroup();
		group_format.setOpenid(id.getOpenid());
		HttpRequest request = RequestFactory.getGroupsByUserReqeust(this,
				session.getApp().getAccess_token(), group_format.toJson());
		if (listener != null)
			request.setAttribute(request, listener);
		super.addHandlerObject(request, OutGroup.class);
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	/**
	 * require : group id & group name
	 */
	public HttpRequest updateGroupName(Group group, RequestCallback callback) {
		if (group == null || group.getId() == null) {
			logger.warn("group or group id is null");
			return null;
		}
		OutGroup group_format = new OutGroup();
		group_format.setGroup(group);
		HttpRequest request = RequestFactory
				.updateGroupsReqeust(callback, session.getApp()
						.getAccess_token(), group_format.updateFormat());
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	public HttpRequest moveGroup(List<OpenID> ids, String to_groupid,
			RequestCallback callback) {
		if (ids == null || ids.size() == 0) {
			logger.warn("group or group id is null");
			return null;
		}
		OutGroup group_format = new OutGroup();
		group_format.setOpenid_list(ids);
		group_format.setTo_groupid(to_groupid);
		HttpRequest request = RequestFactory.moveGroupsReqeust(callback,
				session.getApp().getAccess_token(), group_format.moveFormat());
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	/**
	 * require : group id
	 */
	public HttpRequest delGroup(Group group, RequestCallback callback) {
		if (group == null || group.getId() == null) {
			logger.warn("group or group id is null");
			return null;
		}
		OutGroup group_format = new OutGroup();
		group_format.setId(group.getId());
		HttpRequest request = RequestFactory.delGroupsReqeust(callback, session
				.getApp().getAccess_token(), group_format.delFormat());
		logger.info(request.getUrl());
		session.execute(request);
		return request;
	}

	/**
	 * =========================== Group Manager
	 * End=============================
	 */

	public void startup() {
		super.startup();
		expireCheck();
	}

	public void shutdown() {
		closeTimer();
		super.shutdown();
	}

	private void closeTimer() {
		try {
			if (expires_timer != null) {
				expires_timer.cancel();
				expires_timer = null;
				logger.info("Login expire checker is stop");
			}
		} catch (Exception e) {
		}
	}

	public void expireCheck() {
		logger.info("Login expire checker is start");

		final long expire = 24 * 60 * 60 * 1000;
		long firstTime = getFirstTime();
		expires_timer = org.jcommon.com.util.thread.TimerTaskManger.instance()
				.schedule("Login expire Checker", new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						logger.info("Login expire Checker Start...");
						Set<User> expire_users = new HashSet<User>();

						long now = System.currentTimeMillis();
						long expire = expires * 60 * 60 * 1000;
						for (User user : user_cache.values()) {
							if (now - user.getCreate_time() > expire)
								expire_users.add(user);
						}
						for (User user : expire_users) {
							synchronized (user_cache) {
								user_cache.remove(user.getOpenid());
								logger.info("remove expire handler:"
										+ user.getOpenid());
							}
						}
						expire_users.clear();
						expire_users = null;
						logger.info("Login expire Checker End...");
					}

				}, firstTime, expire);
	}

	private long getFirstTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");
		Date now = new Date();
		String today = sdf.format(now);
		String check = today.substring(0, 10) + " " + CHECKTIME;
		logger.info(String.format("now:%s;check_time:%s", today, check));
		try {

			Date check_date = sdf.parse(check);
			long first_time = check_date.getTime();
			logger.info(String.format("now:%s;check_time:%s;", sdf.format(now),
					sdf.format(check_date)));
			if (first_time < now.getTime())
				first_time = first_time + (24 * 60 * 60 * 1000);
			return first_time - now.getTime();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		return 0;
	}
}
