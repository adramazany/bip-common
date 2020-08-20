package aip.orm;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Settings;
import org.hibernate.internal.SessionImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import aip.util.NVL;
import aip.db.AIPDBUtil;

/**
 * Configures and provides access to Hibernate sessions, tied to the
 * current thread of execution.  Follows the Thread Local Session
 * pattern, see {@link http://hibernate.org/42.html }.
 */
public class HibernateSessionFactory {

    /** 
     * Location of hibernate.cfg.xml file.
     * Location should be on the classpath as Hibernate uses  
     * #resourceAsStream style lookup for its configuration file. 
     * The default classpath location of the hibernate config file is 
     * in the default package. Use #setConfigFile() to update 
     * the location of the configuration file for the current session.   
     */
    private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
    //private static String CONFIG_FILE_LOCATION = "E:/java/workspace6.5/AIPNIOPDCSIB/src/hibernate.cfg.xml";
	//private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private  static Configuration configuration = new Configuration();
    private static org.hibernate.SessionFactory sessionFactory;
    private static String configFile = CONFIG_FILE_LOCATION;
    
    private static boolean isConvertNamedQuery2SQLQuery=false;
    

    private HibernateSessionFactory() {
    }
	
	/**
     * Returns the ThreadLocal Session instance.  Lazy initialize
     * the <code>SessionFactory</code> if needed.
     *
     *  @return Session
     *  @throws HibernateException
     */
    public static boolean isNotAlive(Session session) {
    	boolean _isNotAlive = false;
    	try {
//    		if(session.connection().isClosed())_isNotAlive=true;

//    		if(!b)
    			//session.connection().createStatement().executeQuery("select count(*) from sib.sibuser where id = 0").close();
    			//FIXME:it may be don't work correctly in identify of losed session's then replace with upper code 
    			//session.createQuery("from UserENT").setMaxResults(0).uniqueResult();
    		
//    		else
//        		System.out.println("HibernateSessionFactory.isNotAlive(): session.connection().isClosed() = True");
    			
//    		return b;
		} catch (Exception e) {
			e.printStackTrace();
		}    	
		return _isNotAlive;
    }
    
    public static synchronized Session getSession() throws HibernateException {
//		System.out.println("----------------------------------------------------------------------------------------------------------");
//		System.out.println("HibernateSessionFactory.getSession(): Getting session ... ");
        Session session = null;//(Session) threadLocal.get();
		if (session == null || !session.isOpen() || isNotAlive(session)) {//!session.isConnected() || 
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			//session = (sessionFactory != null) ? sessionFactory.openSession() : null;
			if(sessionFactory != null){
				try {
					session = sessionFactory.getCurrentSession();
				} catch (org.hibernate.HibernateException he) {
					he.printStackTrace();
					session = sessionFactory.openSession() ;
				}
			}
			try {
				if(isNotAlive(session)) {
					if(session!=null){
						session.disconnect();
					}
					//Class.forName("com.mysql.jdbc.Driver");
					//Class.forName("oracle.jdbc.driver.OracleDriver");
//					Class.forName(configuration.getProperty("connection.driver_class"));
//					Connection conn = DriverManager.getConnection(configuration.getProperty("connection.url"),configuration.getProperty("connection.username"),configuration.getProperty("connection.password"));
//					Connection conn = AIPDBUtil.getDataSourceConnection("jdbc/aiplawds");
					//session.reconnect(session);
					//System.err.println("HibernateSessionFactory.getSession connection is not live!");
					throw new RuntimeException("connection is not live!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(isConvertNamedQuery2SQLQuery){
				session = new AIPSessoinOracle(session);
			}
			
			//threadLocal.set(session);
		}
//		System.out.println("HibernateSessionFactory.getSession(): Getting session done");
//		System.out.println("----------------------------------------------------------------------------------------------------------");
		return session;
    }

	/**
     *  Rebuild hibernate session factory
     *
     */
    static boolean isConfigFileLoaded=false;
	public static synchronized void rebuildSessionFactory() {
		try {
			if(!isConfigFileLoaded){
				configuration.configure(configFile);
				isConfigFileLoaded=true;
			}

			validateAndCorrectDataSource();
			
			sessionFactory = configuration.buildSessionFactory();
			
			isConvertNamedQuery2SQLQuery = NVL.getBool( configuration.getProperty("aip.hibernate.convert_namedquery_sqlquery") );
			
		} catch (Exception e) {
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	private static void validateAndCorrectDataSource() {
		try{
			String dataSourceName="connection.datasource";
			String dataSource = configuration.getProperty(dataSourceName);
			if(NVL.isEmpty(dataSource)){
				dataSourceName="hibernate.connection.datasource";
				dataSource = configuration.getProperty(dataSourceName);
			}
			
			
			if(!NVL.isEmpty(dataSource) && !dataSource.startsWith("java:comp/env")){
				/**
				 * if this code execute without exception means context is Tomcat (and need correction) else context is weblogic 
				 */
				Properties dataSource_properties = configuration.getProperties();
				Context ctx  = new InitialContext();
				Context ctx_comp_env = (Context) ctx.lookup("java:comp/env");
				ctx_comp_env.lookup(dataSource);
				//dataSource_properties.setProperty(dataSourceName, "java:comp/env/"+dataSource);
				dataSource_properties.setProperty("connection.datasource", "java:comp/env/"+dataSource);
				dataSource_properties.setProperty("hibernate.connection.datasource", "java:comp/env/"+dataSource);
				
				//configuration.mergeProperties(dataSource_properties);

				//configuration.buildSettings(dataSource_properties);
				ServiceRegistryBuilder builder=new ServiceRegistryBuilder();
				  builder.applySettings(dataSource_properties);//configuration.getProperties());
				  ServiceRegistry serviceRegistry=builder.buildServiceRegistry();
				  Settings settings=configuration.buildSettings(serviceRegistry);
					
				//Settings settings = configuration.buildSettings();
				//configuration.configure();//duplicate error
				//settings.getConnectionProvider().
			}
		}catch(Exception ex){
			//ex.printStackTrace();
			System.err.println(ex);
		}
		
	}

	/**
     *  Close the single hibernate session instance.
     *
     *  @throws HibernateException
     */
    public static void closeSession() throws HibernateException {
        Session session = getSession();//(Session) threadLocal.get();
        //threadLocal.set(null);
        if (session != null) {
            session.close();
        }
    }

	/**
     *  return session factory
     *
     */
	public static org.hibernate.SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
     *  return session factory
     *
     *	session factory will be rebuilded in the next call
     */
	public static void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	/**
     *  return hibernate configuration
     *
     */
	public static Configuration getConfiguration() {
		return configuration;
	}
	
	public static Connection getConnection() throws Exception{
//		Class.forName(configuration.getProperty("connection.driver_class"));
//		Connection conn = DriverManager.getConnection(configuration.getProperty("connection.url"),configuration.getProperty("connection.username"),configuration.getProperty("connection.password"));
		Connection conn = AIPDBUtil.getDataSourceConnection("jdbc/aiplawds");
		return conn;
	}
	


	
	public static final int REFTYPE_NONE=0; 
	public static final int REFTYPE_GET=1; 
	//public static final int REFTYPE_=2; 
	//public static final int REFTYPE_=4; 
	static int refreshtype = -1;
	public static int getRefreshType(){
		if(refreshtype<0){
			refreshtype=NVL.getInt(configuration.getProperty("aip.hibernate.refreshtype"));
		}
		return refreshtype;
	}
	public static boolean hasRefreshType(int reftype){
		return (reftype | getRefreshType()) == reftype;
	}
	public static boolean hasRefreshTypeGET(){
		return (REFTYPE_GET | getRefreshType()) == REFTYPE_GET;
	}
	
	public static String getProperty(String name){
		return configuration.getProperty(name);
	}

	
	public static String getDBType() {
		String dbtype=null;
		String dialect =  getProperty("dialect");
		String dialectLower = dialect.toLowerCase(); 
		if(dialectLower.indexOf("oracle")>=0){
			dbtype="oracle";
		}else if(dialectLower.indexOf("sqlserver")>=0){
			dbtype="sqlserver";
		}else if(dialectLower.indexOf("mysql")>=0){
			dbtype="mysql";
		}else{
			dbtype=dialect.replace("org.hibernate.dialect.", "").replace("Dialect", "");
		}
		return dbtype;
	}
	

}