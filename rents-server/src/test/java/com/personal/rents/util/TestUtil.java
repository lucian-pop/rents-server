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
		account.setExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setEmail("initial.account@gmail.com");
		account.setPassword("account password");
		account.setFirstname("account firstname");
		account.setLastname("account lastname");
		account.setPhone("+4 0100900900");
		account.setSignupDate(date);

		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountMapper = session.getMapper(AccountDAO.class);
			accountMapper.insertAccount(account);
			session.commit();
			
			// Generate token
			String tokenKey = TokenGenerator.generateToken();
			Token token = new Token();
			token.setAccountId(account.getId());
			token.setTokenKey(tokenKey);
			token.setCreationDate(new Date());
					
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
				account.setExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
				account.setEmail("initial.account@gmail.com");
				account.setPassword("account password");
				account.setFirstname("account firstname");
				account.setLastname("account lastname");
				account.setPhone("+4 0100900900");
				account.setSignupDate(date);

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
			accountMapper.deleteAccount(account.getId());
			session.commit();	
		} finally {
			session.close();
		}
	}
	
	public static Address addAddress() {
		Address address = new Address();
		address.setStreetNo("68A");
		address.setStreetName("Observatorului");
		address.setNeighbourhood("Zorilor");
		address.setLocality("Cluj-Napoca");
		address.setAdmAreaL1("Cluj");
		address.setCountry("Romania");
		address.setLatitude(46.7667);
		address.setLongitude(23.5833);
		address.setBuilding("C3");
		address.setStaircase("2A");
		address.setFloor((short) 4);
		address.setAp("12B");
		
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
			addressDAO.deleteAddress(address.getId());
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
		rent.setPrice(500);
		rent.setSurface(120);
		rent.setRooms((short) 3);
		rent.setBaths((short) 3);
		rent.setParty((byte) 1);
		rent.setRentType((byte) 1);
		rent.setArchitecture((byte) 1);
		rent.setAge((short) 10);
		rent.setDescription("some dummy text here");
		rent.setPetsAllowed(true);
		rent.setPhone("0750110440");
		rent.setCreationDate(new Date());
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
			rentDAO.deleteRent(rent.getId());
			
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			addressDAO.deleteAddress(rent.getAddress().getId());

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
