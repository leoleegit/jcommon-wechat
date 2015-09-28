package org.jcommon.com.wechat.jiaoka.db.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.util.config.ConfigLoader;
import org.jcommon.com.wechat.jiaoka.db.DbProvider;

public class SqlDbProvider implements DbProvider {
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean insert(String sql, Object bean) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionManager.instance().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);

			Class<?> clazz = bean.getClass();
			List<String> pars = getParameters(sql);
			for(int i=0;i<pars.size();i++){
				String par  = pars.get(i);
				
				Field field = getDeclaredField(par, clazz.getDeclaredFields());
				if(field!=null){
					String name   = field.getName();
					Class<?> type = field.getType();
					
			        Method m = ConfigLoader.getMethod(bean.getClass(), new StringBuilder().append("get").append(name).toString());
			        if (m == null)
			            m = ConfigLoader.getMethod(bean.getClass(), new StringBuilder().append("is").append(name).toString());
			        if(m!=null){
			        	if (String.class == type) {
			                String value = (String)m.invoke(bean, new Object[0]);
			                ps.setString(i+1, value);
			            } else if (Timestamp.class == type) {
			            	Timestamp value = (Timestamp)m.invoke(bean, new Object[0]);
			                ps.setTimestamp(i+1, value);
			            } else if ((Integer.class == type) || (Integer.TYPE == type)) {
			                int value = (Integer)m.invoke(bean, new Object[0]);
			                ps.setInt(i+1, value);
			            } else if ((Boolean.class == type) || (Boolean.TYPE == type)) {
			                boolean value = (Boolean)m.invoke(bean, new Object[0]);
			                ps.setBoolean(i+1, value);
			            } else if ((Long.class == type) || (Long.TYPE == type)) {
			                long value = (Long)m.invoke(bean, new Object[0]);
			                ps.setLong(i+1, value);
			            } else if ((Float.class == type) || (Float.TYPE == type)) {
			                float value = (Float)m.invoke(bean, new Object[0]);
			                ps.setFloat(i+1, value);
			            }
			        }
				}
			}
			
			ps.executeUpdate();
			conn.commit();
			return true;
		} catch (Exception e) {
			try {
				logger.info("Exception and rollback.");
				if (conn != null)
					conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				logger.error("", e1);
			}
			logger.error("", e);
		} finally {
			ConnectionManager.release(conn, ps);
		}
		return false;
	}

	@Override
	public boolean delete(String sql, Object bean) {
		// TODO Auto-generated method stub
		return insert(sql,bean);
	}

	@Override
	public boolean update(String sql, Object bean) {
		// TODO Auto-generated method stub
		return insert(sql,bean);
	}

	@Override
	public Object select(String sql, Class<?> clazz, Object bean) throws SQLException {
		// TODO Auto-generated method stub
		List<Object> list = selectArray(sql,clazz,bean);
		if(list!=null && list.size()>0){
			return list.remove(0);
		}
		return null;
	}

	@Override
	public List<Object> selectArray(String sql, Class<?> clazz_, Object bean)
			throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.instance().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);

			Class<?> clazz = bean.getClass();
			List<String> pars = getParameters(sql);
			for(int i=0;i<pars.size();i++){
				String par  = pars.get(i);
				
				Field field = getDeclaredField(par, clazz.getDeclaredFields());
				if(field!=null){
					String name   = field.getName();
					Class<?> type = field.getType();
					
			        Method m = ConfigLoader.getMethod(bean.getClass(), new StringBuilder().append("get").append(name).toString());
			        if (m == null)
			            m = ConfigLoader.getMethod(bean.getClass(), new StringBuilder().append("is").append(name).toString());
			        if(m!=null){
			        	if (String.class == type) {
			                String value = (String)m.invoke(bean, new Object[0]);
			                ps.setString(i+1, value);
			            } else if (Timestamp.class == type) {
			            	Timestamp value = (Timestamp)m.invoke(bean, new Object[0]);
			                ps.setTimestamp(i+1, value);
			            } else if ((Integer.class == type) || (Integer.TYPE == type)) {
			                int value = (Integer)m.invoke(bean, new Object[0]);
			                ps.setInt(i+1, value);
			            } else if ((Boolean.class == type) || (Boolean.TYPE == type)) {
			                boolean value = (Boolean)m.invoke(bean, new Object[0]);
			                ps.setBoolean(i+1, value);
			            } else if ((Long.class == type) || (Long.TYPE == type)) {
			                long value = (Long)m.invoke(bean, new Object[0]);
			                ps.setLong(i+1, value);
			            } else if ((Float.class == type) || (Float.TYPE == type)) {
			                float value = (Float)m.invoke(bean, new Object[0]);
			                ps.setFloat(i+1, value);
			            }
			        }
				}
			}
			rs = ps.executeQuery();
			
			while (rs.next()) {
				
			}
		} catch (Exception e) {
			try {
				logger.info("Exception and rollback.");
				if (conn != null)
					conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				logger.error("", e1);
			}
			logger.error("", e);
		} finally {
			ConnectionManager.release(conn, ps, rs);
		}
		return null;
	}
	
	public static Field getDeclaredField(String name, Field[] fs){
		for(Field field : fs){
			if(field.getName().equalsIgnoreCase(name))
				return field;
		}
		return null;
	}

	public static List<String> getParameters(String sql){
		sql = sql.trim();
		sql = sql.toLowerCase();
		List<String> pars = new ArrayList<String>();
		if(sql.startsWith("insert")){
			if(sql.indexOf("(")>0 && sql.indexOf(")")>0){
				String str = sql.substring(sql.indexOf("(")+1, sql.indexOf(")"));
				if(str!=null){
					if(str.indexOf(",")>0){
						for(String ps : str.split(","))
							pars.add(ps.trim());
					}else
						pars.add(str);
				}
			}
		}else{
			if(sql.indexOf(" ")>0){
				for(String ps : sql.split(" ")){
					if(ps.indexOf("=")>0){
						String name = ps.split("=")[0];
						name        = name.indexOf(".")>0?name.substring(name.lastIndexOf(".")+1):name;
						pars.add(name);
					}
				}
			}
		}
		return pars;
	}
}
