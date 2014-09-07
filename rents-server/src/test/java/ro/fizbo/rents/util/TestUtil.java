package ro.fizbo.rents.util;

import java.io.IOException;
import java.io.Reader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

import ro.fizbo.rents.dao.AccountDAO;
import ro.fizbo.rents.dao.AddressDAO;
import ro.fizbo.rents.dao.RentDAO;
import ro.fizbo.rents.dao.RentFavoriteDAO;
import ro.fizbo.rents.dao.RentImageDAO;
import ro.fizbo.rents.dao.TokenDAO;
import ro.fizbo.rents.logic.TokenGenerator;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Address;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.Token;
import ro.fizbo.rents.util.PasswordHashing;
import ro.fizbo.rents.webservice.jsonprovider.GsonMessageBodyHandler;

public class TestUtil {
	
	public static final String ACCOUNT_EMAIL = "initial.account@gmail.com";
	
	public static final String ACCOUNT_NAME= "John Smith";
	
	public static final String USER_EXTERNAL_ID = "805939182760843";
	
	public static final String USER_ACCESS_TOKEN = "CAAFJNhhy1CYBAPQXYbVLT5poxOAcStBocYpDI2rSlnEB2F"
			+ "NVnIZB7VZCf3CgcbwQNu1qOAkl9ZBQqsZB1NmZCAX67w8x2bWZCDaNjLjrH261cNMen2Re7YjsdhBUgbuyoi"
			+ "xMJZAkR9gqbgPBwrGg0DfSwZCQxbONollObHXzk9ul4jZBxDmVKAQiZByCAPrVJwwgoIuk0we47USxuxgSAC"
			+ "29j6me1zi8q8BdHZCQJfpftpuSAZDZD";
	
	public static final String ACCOUNT_PHONE = "+40100900900";
	
	public static final String ACCOUNT_PASSWORD = "account password";

	public static final String BASE_URI = "http://192.168.1.2:8080/rents-server/ws/";
	
	public static final double MIN_LATITUDE = 46.7379424563698;

	public static final double MAX_LATITUDE = 46.76499396368981;

	public static final double MIN_LONGITUDE = 23.56791313737631;
	
	public static final double MAX_LONGITUDE = 23.59537862241268;
	
	public static final int PAGE_SIZE = 50;
	
	private static final double LATITUDE = 46.7457380;
	
	private static final double LONGITUDE = 23.5833123;
	
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
				sqlSessionFactory.getConfiguration().addMapper(RentFavoriteDAO.class);
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
		Account account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountEmail(ACCOUNT_EMAIL);
		try {
			account.setAccountPassword(PasswordHashing.createHashString(ACCOUNT_PASSWORD));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone(ACCOUNT_PHONE);
		account.setAccountSignupDate(new Date());

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
	
	public static Account createAccountWithExternalInfo() {
		//Insert account into database
		Account account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountEmail(ACCOUNT_EMAIL);
		account.setAccountExternalId(USER_EXTERNAL_ID);
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone(ACCOUNT_PHONE);
		account.setAccountSignupDate(new Date());

		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountMapper = session.getMapper(AccountDAO.class);
			accountMapper.insertAccount(account);
			session.commit();
			
			// Generate token
			Token token = new Token();
			token.setAccountId(account.getAccountId());
			token.setTokenKey(USER_ACCESS_TOKEN);
			token.setTokenCreationDate(new Date());
					
			// insert token into database
			TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
			tokenDAO.insertToken(token);
			session.commit();
			
			account.setTokenKey(USER_ACCESS_TOKEN);
		} finally {
			session.close();
		}
		
		return account;
	}
	
	public static Account createAccount(String email, String phone) {
		//Insert account into database
		Account account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setAccountEmail(email);
		try {
			account.setAccountPassword(PasswordHashing.createHashString(ACCOUNT_PASSWORD));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone(phone);
		account.setAccountSignupDate(new Date());

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
		Account account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setAccountEmail("initial.account@gmail.com");
		account.setAccountPassword("account password");
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone("+4 0100900900");
		account.setAccountSignupDate(new Date());

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
			accountMapper.deleteAccountByEmail(account.getAccountEmail());
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
		address.setAddressLatitude(LATITUDE);
		address.setAddressLongitude(LONGITUDE);
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
	
	public static void addRentFavorite(int accountId, int rentId) {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentFavoriteDAO rentFavoritesDAO = session.getMapper(RentFavoriteDAO.class);
			rentFavoritesDAO.addEntry(accountId, rentId, new Date());
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public static void deleteRentFavorite(int accountId, int rentId) {
		SqlSession session = TestUtil.getSqlSessionFactory().openSession();
		try {
			RentFavoriteDAO rentFavoritesDAO = session.getMapper(RentFavoriteDAO.class);
			rentFavoritesDAO.deleteEntry(accountId, rentId);
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
