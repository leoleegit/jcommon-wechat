package org.jcommon.com.wechat.manage.service;

import java.util.HashSet;
import java.util.Set;


public class Application extends javax.ws.rs.core.Application {
	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(Token.class);
		return classes;
	}
}