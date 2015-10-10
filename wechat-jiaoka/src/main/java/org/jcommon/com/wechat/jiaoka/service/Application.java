package org.jcommon.com.wechat.jiaoka.service;

import java.util.HashSet;
import java.util.Set;

public class Application extends javax.ws.rs.core.Application {
	@Override
	  public Set<Class<?>> getClasses() {
		HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(CaseService.class);
        classes.add(UserService.class);
        classes.add(AuditLogService.class);
		return classes;
	  }
}
