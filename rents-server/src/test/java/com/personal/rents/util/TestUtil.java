package com.personal.rents.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.personal.rents.dao.AccountDAO;
import com.personal.rents.dao.AddressDAO;
import com.personal.rents.dao.RentDAO;
import com.personal.rents.dao.RentImageDAO;
import com.personal.rents.dao.TokenDAO;
import com.personal.rents.logic.TokenGenerator;
import com.personal.rents.model.Account;
import com.personal.rents.model.Address;
import com.personal.rents.model.Rent;
import com.personal.rents.model.Token;
import com.personal.rents.webservice.jsonprovider.GsonMessageBodyHandler;

public class TestUtil {

	public static final String BASE_URI = "http://localhost:8080/rents-server/ws/";
	
	private static final String DATABASE_PROPERTIES_FILE = "database/database.properties";

	private static final String DATABASE_CONFIGURATION_FILE = "database/mybatis-config.xml";
	
	private static final String DATABASE_ENV = "rents-test";
	
	private static SqlSessionFactory sqlSessionFactory;
	
	private static Logger logger = Logger.getLogger(TestUtil.class);
	
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
				
				// Add mappers.
				sqlSessionFactory.getConfiguration().addMapper(AccountDAO.class);
				sqlSessionFactory.getConfiguration().addMapper(AddressDAO.class);
				sqlSessionFactory.getConfiguration().addMapper(RentDAO.class);
				sqlSessionFactory.getConfiguration().addMapper(RentImageDAO.class);
				sqlSessionFactory.getConfiguration().addMapper(TokenDAO.class);

				logger.info("Test database session factory created succesfully");
			} catch (IOException e) {
				logger.error("Read test database configuration resources encountered an error", e);
				logger.warn("Cannot successfully create test database session factory. " +
					"Create test database session factory terminated abnormally.");
			}
		}

		return sqlSessionFactory;
	}
	
	public static Account createAccount() {
		//Insert account into database
		Date date = new Date();
		
		Account account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setAccountEmail("initial.account@gmail.com");
		account.setAccountPassword("account password");
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone("+4 0100900900");
		account.setAccountSignupDate(date);

		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountMapper = session.getMapper(AccountDAO.class);
			accountMapper.insertAccount(account);
			session.commit();
			
			// Generate token
			String tokenKey = TokenGenerator.generateToken();
			Token token = new Token();
			token.setAccountId(account.getAccountId());
			token.setTokenKey(tokenKey);
			token.setTokenCreationDate(new Date());
					
			// insert token into database
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			tokenDAO.insertToken(token);
			session.commit();
			
			account.setTokenKey(tokenKey);
		} finally {
			session.close();
		}
		
		return account;
	}

	public static Account createAccountWithoutToken() {
		//Insert account into database
				Date date = new Date();
				
				Account account = new Account();
				account.setAccountType((byte) 0);
				account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
				account.setAccountEmail("initial.account@gmail.com");
				account.setAccountPassword("account password");
				account.setAccountFirstname("account firstname");
				account.setAccountLastname("account lastname");
				account.setAccountPhone("+4 0100900900");
				account.setAccountSignupDate(date);

				SqlSession session = TestUtil.getSqlSessionFactory().openSession();
				try {
					AccountDAO accountMapper = session.getMapper(AccountDAO.class);
					accountMapper.insertAccount(account);
					session.commit();
				} finally {
					session.close();
				}
				
				return account;
	}
	
	public static void deleteAccount(Account account) {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountMapper = session.getMapper(AccountDAO.class);
			accountMapper.deleteAccount(account.getAccountId());
			session.commit();	
		} finally {
			session.close();
		}
	}
	
	public static Address addAddress() {
		Address address = new Address();
		address.setAddressStreetNo("68A");
		address.setAddressStreetName("Observatorului");
		address.setAddressNeighbourhood("Zorilor");
		address.setAddressLocality("Cluj-Napoca");
		address.setAddressAdmAreaL1("Cluj");
		address.setAddressCountry("Romania");
		address.setAddressLatitude(46.7457380);
		address.setAddressLongitude(23.5833123);
		address.setAddressBuilding("C3");
		address.setAddressStaircase("2A");
		address.setAddressFloor((short) 4);
		address.setAddressAp("12B");
		
		SqlSession session = getSqlSessionFactory().openSession();
		try {
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			addressDAO.insertAddress(address);
			session.commit();
		} finally {
			session.close();
		}

		return address;
	}
	
	public static void deleteAddress(Address address) {
		SqlSession session = getSqlSessionFactory().openSession();
		try {
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			addressDAO.deleteAddress(address.getAddressId());
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public static Rent addRent(int accountId) {
		Address address = addAddress();
		
		Rent rent = new Rent();
		rent.setAccountId(accountId);
		rent.setAddress(address);
		rent.setRentPrice(500);
		rent.setRentSurface(120);
		rent.setRentRooms((short) 3);
		rent.setRentBaths((short) 3);
		rent.setRentParty((byte) 1);
		rent.setRentType((byte) 1);
		rent.setRentArchitecture((byte) 1);
		rent.setRentAge((short) 10);
		rent.setRentDescription("some dummy text here");
		rent.setRentPetsAllowed(true);
		rent.setRentPhone("0750110440");
		rent.setRentAddDate(new Date());
		rent.setRentStatus((byte) 0);
		
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentDAO.insertRent(rent);
			session.commit();
		} finally {
			session.close();
		}
		
		return rent;
	}
	
	public static void deleteRent(Rent rent) {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentDAO.deleteRent(rent.getRentId());
			
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			addressDAO.deleteAddress(rent.getAddress().getAddressId());

			session.commit();
		} finally {
			session.close();
		}
	}

	public static WebTarget buildWebTarget() {
		final Client client = ClientBuilder.newBuilder()
				.register(GsonMessageBodyHandler.class)
				.build();
		WebTarget target = client.target(BASE_URI);
		
		return target;
	}
	
	public static WebTarget buildMultiPartWebTarget() {
		final Client client = ClientBuilder.newBuilder()
				.register(GsonMessageBodyHandler.class)
				.register(MultiPartFeature.class)
				.build();
		WebTarget target = client.target(BASE_URI);
		
		return target;
	}

}
