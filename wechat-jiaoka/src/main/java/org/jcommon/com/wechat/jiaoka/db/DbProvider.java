package org.jcommon.com.wechat.jiaoka.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DbProvider {
	public boolean insert(String sql, Object bean);
	public boolean delete(String sql, Object bean);
	public boolean update(String sql, Object bean);
	public long selectCount(String sql, Object ...args);
	public List<Object> selectArray(String sql, Class<?> clazz_, Object ...args);
	public Object  select(String sql, Class<?> clazz, Object bean);
	public List<Object>  selectArray(String sql, Class<?> clazz, Object bean);
	public List<Object> getResult(String sql, Class<?> clazz_, ResultSet rs) throws SQLException;
}
