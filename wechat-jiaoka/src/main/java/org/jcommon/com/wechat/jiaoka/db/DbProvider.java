package org.jcommon.com.wechat.jiaoka.db;

import java.sql.SQLException;
import java.util.List;

public interface DbProvider {
	public boolean insert(String sql, Object bean);
	public boolean delete(String sql, Object bean);
	public boolean update(String sql, Object bean);
	public Object  select(String sql, Class<?> clazz) throws SQLException;
	public List<Object>  selectArray(String sql, Class<?> clazz) throws SQLException;
}
