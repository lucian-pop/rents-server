package ro.fizbo.rents.listener;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import ro.fizbo.rents.dao.AccountDAO;
import ro.fizbo.rents.dao.AddressDAO;
import ro.fizbo.rents.dao.RentDAO;
import ro.fizbo.rents.dao.RentFavoriteDAO;
import ro.fizbo.rents.dao.RentImageDAO;
import ro.fizbo.rents.dao.TokenDAO;

/**
 * Server startup listener that creates the sql session factory and returns it
 * whenever it's requested
 */
public class ApplicationManager implements ServletContextListener {
	
	// database configuration resources
	private final String DATABASE_PROPERTIES_FILE = "database/database.properties";

	private final String DATABASE_CONFIGURATION_FILE = "database/mybatis-config.xml";
	
	private static Logger logger = Logger.getLogger(ApplicationManager.class);
	
	private static SqlSessionFactory sqlSessionFactory;
	
	// paths
	private static final String DOMAIN_NAME="fizbo.ro";
	
	private static String appRealPath;
	
	private static String appURL;

	/**
	 * Creates and configures the sql session factory and store application paths.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("Create database session factory");
		try {
			Properties databaseProperties = Resources.getResourceAsProperties(
				DATABASE_PROPERTIES_FILE);
			Reader databaseConfiguration = Resources.getResourceAsReader(
				DATABASE_CONFIGURATION_FILE);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(databaseConfiguration,
				databaseProperties);
			
			// Add mappers.
			sqlSessionFactory.getConfiguration().addMapper(AccountDAO.class);
			sqlSessionFactory.getConfiguration().addMapper(AddressDAO.class);
			sqlSessionFactory.getConfiguration().addMapper(RentDAO.class);
			sqlSessionFactory.getConfiguration().addMapper(RentImageDAO.class);
			sqlSessionFactory.getConfiguration().addMapper(RentFavoriteDAO.class);
			sqlSessionFactory.getConfiguration().addMapper(TokenDAO.class);
			
			logger.info("Database session factory created succesfully");
		} catch (IOException e) {
			logger.error("Read database configuration resources encountered an error", e);
			logger.warn("Cannot successfully create database session factory. " +
				"Create database session factory terminated abnormally.");
		}
		
		logger.info("Store application paths");
		appRealPath = event.getServletContext().getRealPath("/");
		appURL = buildAppURL(event.getServletContext().getContextPath());
	}
	
	/**
	 * Destroys the sql session factory
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		sqlSessionFactory = null;
	}
	
	/**
	 * Returns the sql session factory
	 */
	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	
	/**
	 * Returns application real path.
	 */
	public static String getAppRealPath() {
		return appRealPath;
	}
	
	public static String getAppURL() {
		return appURL;
	}
	
	private static final String buildAppURL(String contextPath) {
		StringBuilder appURLBuilder = new StringBuilder();
		appURLBuilder.append("http://");
		appURLBuilder.append(DOMAIN_NAME);
		appURLBuilder.append(contextPath);
		
		return appURLBuilder.toString();
	}
}