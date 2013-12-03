

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public class MyBatisTestCase extends TestCase {

	private static final String DATABASE_PROPERTIES_FILE = "database/database.properties";

	private static final String DATABASE_CONFIGURATION_FILE = "database/mybatis-config.xml";
	
	private static final String DATABASE_ENV = "rents-test";

	private static SqlSessionFactory sqlSessionFactory;
	
	private static Logger logger = Logger.getLogger(MyBatisTestCase.class);
	
	public static SqlSessionFactory getSqlSessionFactory() {
		if(sqlSessionFactory == null) {
			logger.info("Create test database session factory");
			try {
				Properties databaseProperties = Resources.getResourceAsProperties(
					DATABASE_PROPERTIES_FILE);
				Reader databaseConfiguration = Resources.getResourceAsReader(
					DATABASE_CONFIGURATION_FILE);
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(databaseConfiguration, 
						DATABASE_ENV, databaseProperties);

				logger.info("Test database session factory created succesfully");
			} catch (IOException e) {
				logger.error("Read test database configuration resources encountered an error", e);
				logger.warn("Cannot successfully create test database session factory. " +
					"Create test database session factory terminated abnormally.");
			}
		}

		return sqlSessionFactory;
	}

	public void testDummy() {
	}
}
