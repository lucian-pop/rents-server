package ro.fizbo.rents.listener;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import ro.fizbo.rents.dao.AccountDAO;
import ro.fizbo.rents.dao.AddressDAO;
import ro.fizbo.rents.dao.CurrencyDAO;
import ro.fizbo.rents.dao.RentDAO;
import ro.fizbo.rents.dao.RentFavoriteDAO;
import ro.fizbo.rents.dao.RentImageDAO;
import ro.fizbo.rents.dao.RentStatisticsDAO;
import ro.fizbo.rents.dao.TokenDAO;
import ro.fizbo.rents.logic.AsyncTaskManager;
import ro.fizbo.rents.logic.CurrencyManager;
import ro.fizbo.rents.logic.SchedulerManager;

/**
 * Server startup listener that creates the sql session factory and returns it
 * whenever it's requested
 */
public class ApplicationManager implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(ApplicationManager.class);
	
	/** Database configuration resources. */
	private final String DATABASE_PROPERTIES_FILE = "database/database.properties";

	private final String DATABASE_CONFIGURATION_FILE = "database/mybatis-config.xml";
	
	private static SqlSessionFactory sqlSessionFactory;
	
	/** Paths. */
	private static final String DOMAIN_NAME="192.168.1.4:8080";
	
	private static String appRealPath;
	
	private static String appURL;
	
	/** Conversion rates map.*/
	private static Map<String, BigDecimal> conversionRatesMap = new HashMap<String, BigDecimal>();
	
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
			sqlSessionFactory.getConfiguration().addMapper(CurrencyDAO.class);
			sqlSessionFactory.getConfiguration().addMapper(RentStatisticsDAO.class);
			
			logger.info("Database session factory created succesfully");
		} catch (IOException e) {
			logger.error("Read database configuration resources encountered an error", e);
			logger.warn("Cannot successfully create database session factory. " +
				"Create database session factory terminated abnormally.");
		}
		
		logger.info("Store application paths");
		appRealPath = event.getServletContext().getRealPath("/");
		appURL = buildAppURL(event.getServletContext().getContextPath());
		logger.info("Stored application paths");
		
		logger.info("Get conversion rates and schedule update.");
		CurrencyManager.scheduleConversionRatesUpdate();
	}
	
	/**
	 * Releases the application resources.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Stop async task executor service.");
		AsyncTaskManager.stop();
		logger.info("Stop scheduler for conversions rates update.");
		SchedulerManager.stop();
		
		logger.info("Unregister database drivers.");
		sqlSessionFactory = null;
		Enumeration<Driver> drivers = DriverManager.getDrivers();     
        Driver driver = null;
        while(drivers.hasMoreElements()) {
            try {
                driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            } catch (SQLException ex) {
            	logger.error("Failed to unregister database driver.", ex);
            }
        }

        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException ie) {
        	logger.error("Failed while trying to shutdown database connection.", ie);
        }
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
	
	public static Map<String, BigDecimal> getConversionRatesMap() {
		return conversionRatesMap;
	}

	private static final String buildAppURL(String contextPath) {
		StringBuilder appURLBuilder = new StringBuilder();
		appURLBuilder.append("http://");
		appURLBuilder.append(DOMAIN_NAME);
		appURLBuilder.append(contextPath);
		
		return appURLBuilder.toString();
	}
	
}