package com.personal.rents.webservice;

import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import com.personal.rents.model.Account;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

public class SignupWebserviceTest extends TestCase {

	private Account account;
	
	private WebTarget target;
	
	public SignupWebserviceTest() {
		target = TestUtil.buildWebTarget();
	}
	
	@Before
    public void setUp() throws Exception {
        Date date = new Date();
		
		account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setAccountEmail("initial.account@gmail.com");
		account.setAccountPassword("account password");
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone("+4 0100900900");
		account.setAccountSignupDate(date);
    }
	
	@After
	public void tearDown() throws Exception {
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	public void testSignup() {
		Response response = target.path("signup").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(account));
		
		assertTrue("Status should be OK" , response.getStatus() == 
				WebserviceResponseStatus.OK.getCode());
		
		account = response.readEntity(Account.class);

		assertNotNull(account);
	}
}
