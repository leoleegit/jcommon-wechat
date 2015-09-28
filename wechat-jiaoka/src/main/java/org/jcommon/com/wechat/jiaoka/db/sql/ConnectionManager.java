package org.jcommon.com.wechat.jiaoka.db.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class ConnectionManager extends org.jcommon.com.util.jdbc.ConnectionManager{
	private static Logger logger = Logger.getLogger(ConnectionManager.class);
	
	private static final String dbname = "jiaoka";
	private final static String CONFIG_FILENAME = "dbConfig.properties";
    private final static String CONFIG_DIRECTORY = System.getProperty("user.dir") + File.separator + "conf";
    
    private static ConnectionManager instance;
    public  static ConnectionManager instance(){
    	if(instance==null)
    		instance = new ConnectionManager();
    	return instance;
    }
    
    public ConnectionManager() {
		super(dbname);
		// TODO Auto-generated constructor stub
		init(CONFIG_FILENAME, CONFIG_DIRECTORY);
	}
    
    public static void release(Connection conn, PreparedStatement ps) {
    	try {
        	if (conn != null) {
        		conn.close();
        		conn = null;
            }
        } catch (Exception e) {
        	logger.error("", e);
        }
        try {
        	if (ps != null) {
                ps.close();
                ps=null;
            }
        } catch (Exception e) {
        	logger.error("", e);
        }
    }
    
    public static void release(Connection conn, PreparedStatement ps, ResultSet st) {
    	release(conn,ps);
        try {
        	if (st != null) {
        		st.close();
        		st=null;
            }
        } catch (Exception e) {
        	logger.error("", e);
        }
       
        
    }
}
