package com.personal.rents.listeners;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

/**
 * Server startup listener that creates the sql session factory and returns it
 * whenever it's requested
 */
public class DatabaseSessionManager implements ServletContextListener {
	
	// database configuration resources
	private final String DATABASE_PROPERTIES_FILE = "database/database.properties";
	private final String DATABASE_CONFIGURATION_FILE = "database/mybatis-config.xml";
	
	private static Logger logger = Logger.getLogger(DatabaseSessionManager.class);
	
	private static SqlSessionFactory sqlSessionFactory;
	
	/**
	 * Creates and configures the sql session factory
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Create database session factory");
		try {
			Properties databaseProperties = Resources.getResourceAsProperties(
				DATABASE_PROPERTIES_FILE);
			Reader databaseConfiguration = Resources.getResourceAsReader(
				DATABASE_CONFIGURATION_FILE);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(databaseConfiguration,
				databaseProperties);
			
			logger.info("Database session factory created succesfully");
		} catch (IOException e) {
			logger.error("Read database configuration resources encountered an error", e);
			logger.warn("Cannot successfully create database session factory. " +
				"Create database session factory terminated abnormally.");
		}
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
}