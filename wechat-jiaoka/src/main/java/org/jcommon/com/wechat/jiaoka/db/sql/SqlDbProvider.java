package org.jcommon.com.wechat.jiaoka.db.sql;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
	private static Logger logger = Logger.getLogger(SqlDbProvider.class);
	
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
	public Object select(String sql, Class<?> clazz, Object bean)  {
		// TODO Auto-generated method stub
		List<Object> list = selectArray(sql,clazz,bean);
		if(list!=null && list.size()>0){
			return list.remove(0);
		}
		return null;
	}

	@Override
	public List<Object> selectArray(String sql, Class<?> clazz_, Object bean){
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
			return getResult(sql, clazz_, rs);
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
	
	public List<Object> getResult(String sql, Class<?> clazz_, ResultSet rs) throws SQLException{
		List<String> names = SqlDbProvider.getSearchNames(sql);
		List<Object> results = new ArrayList<Object>();
		while (rs.next()) {
			Object object = newInstance(clazz_);
			if(object==null)
				break;
			for(String name : names){
				if("*".equals(name)){
					Field[] fs = clazz_.getDeclaredFields();
					for(Field field : fs){
						name = field.getName();
						Method m = ConfigLoader.getMethod(object.getClass(), new StringBuilder().append("set").append(name).toString());
				        if (m == null)
				            m = ConfigLoader.getMethod(object.getClass(), new StringBuilder().append("is").append(name).toString());
				        if(m != null){
				        	Class<?> type = field.getType();
				        	Object args = null;
				        	if (String.class == type) {
				        		args = rs.getString(name);
				            } else if (Timestamp.class == type) {
				            	args = rs.getTimestamp(name);
				            } else if ((Integer.class == type) || (Integer.TYPE == type)) {
				            	args = rs.getInt(name);
				            } else if ((Boolean.class == type) || (Boolean.TYPE == type)) {
				            	args = rs.getBoolean(name);
				            } else if ((Long.class == type) || (Long.TYPE == type)) {
				            	args = rs.getLong(name);
				            } else if ((Float.class == type) || (Float.TYPE == type)) {
				            	args = rs.getFloat(name);
				            }
				        	
				        	try{
				                if (args != null)
				                  m.invoke(object, new Object[] { args });
				            } catch (Exception e) {
				                logger.warn(e);
				            }
				        }
					}
					break;
				}else{
					Method m = ConfigLoader.getMethod(object.getClass(), new StringBuilder().append("set").append(name).toString());
			        if (m == null)
			            m = ConfigLoader.getMethod(object.getClass(), new StringBuilder().append("is").append(name).toString());
			        Field field = getDeclaredField(name, clazz_.getDeclaredFields());
			        if(m != null && field!=null){
			        	Class<?> type = field.getType();
			        	 Object args = null;
			        	if (String.class == type) {
			        		args = rs.getString(name);
			            } else if (Timestamp.class == type) {
			            	args = rs.getTimestamp(name);
			            } else if ((Integer.class == type) || (Integer.TYPE == type)) {
			            	args = rs.getInt(name);
			            } else if ((Boolean.class == type) || (Boolean.TYPE == type)) {
			            	args = rs.getBoolean(name);
			            } else if ((Long.class == type) || (Long.TYPE == type)) {
			            	args = rs.getLong(name);
			            } else if ((Float.class == type) || (Float.TYPE == type)) {
			            	args = rs.getFloat(name);
			            }
			        	
			        	try{
			                if (args != null)
			                  m.invoke(object, new Object[] { args });
			            } catch (Exception e) {
			                logger.warn(e);
			            }
			        }
				}
			}
			results.add(object);
		}
		return results;
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
		}else if(sql.startsWith("update")){
			if(sql.indexOf(" where ")>0 && sql.indexOf(" set ")>0){
				String str = sql.substring(sql.indexOf(" set ")+5, sql.indexOf(" where "));
				if(str.indexOf(",")>=0){
					for(String ps : str.split(",")){
						if(ps.indexOf("=")>0){
							String name = ps.split("=")[0];
							name        = name.indexOf(".")>0?name.substring(name.lastIndexOf(".")+1):name;
							pars.add(name.trim());
						}
					}
				}else{
					str = str.trim();
					if(!"".equals(str))
						pars.add(str);
				}
				str = sql.substring(sql.indexOf(" where ")+7);
				if(str.indexOf(" ")>=0){
					for(String ps : str.split(" ")){
						if(ps.indexOf("=")>0){
							String name = ps.split("=")[0];
							name        = name.indexOf(".")>0?name.substring(name.lastIndexOf(".")+1):name;
							pars.add(name.trim());
						}
					}
				}else{
					str = str.trim();
					if(str.indexOf("=")>0){
						String name = str.split("=")[0];
						name        = name.indexOf(".")>0?name.substring(name.lastIndexOf(".")+1):name;
						pars.add(name.trim());
					}
				}
			}
		}else{
			if(sql.indexOf(" ")>=0){
				for(String ps : sql.split(" ")){
					if(ps.indexOf("=")>0){
						String name = ps.split("=")[0];
						name        = name.indexOf(".")>0?name.substring(name.lastIndexOf(".")+1):name;
						pars.add(name.trim());
					}
				}
			}
		}
		return pars;
	}
	
	public static List<String> getSearchNames(String sql){
		sql = sql.trim();
		sql = sql.toLowerCase();
		List<String> pars = new ArrayList<String>();
		if(sql.startsWith("select")){
			if(sql.indexOf(" from ")>0){
				String str = sql.substring(sql.indexOf("select")+6, sql.indexOf(" from "));
				if(str!=null){
					if(str.indexOf(" ")>=0){
						for(String name : str.split(" ")){
							name        = name.indexOf(".")>0?name.substring(name.lastIndexOf(".")+1):name;
							name        = name.trim();
							if(!"".equals(name))
								pars.add(name.trim());
						}
					}else
						pars.add(str.trim());
				}
			}
		}
		return pars;
	}
	
	private static Object newInstance(Class<?> class_) {
	    try {
	      Class<?>[] par = {};
	      Constructor<?> con = class_.getConstructor(par);
	      Object[] objs = {};
	      return con.newInstance(objs);
	    }
	    catch (SecurityException e) {
	      logger.warn(e);
	    }
	    catch (IllegalArgumentException e) {
	      logger.warn(e);
	    }
	    catch (NoSuchMethodException e) {
	      logger.warn(e);
	    }
	    catch (InstantiationException e) {
	      logger.warn(e);
	    }
	    catch (IllegalAccessException e) {
	      logger.warn(e);
	    }
	    catch (InvocationTargetException e) {
	      logger.warn(e);
	    }
	    return null;
	}
}
