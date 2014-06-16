package ro.fizbo.rents.webservice;

import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.TestCase;

import org.junit.After;

import ro.fizbo.rents.dto.AccountUpdate;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.util.TestUtil;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;

public class AccountWebserviceTest extends TestCase {
	
	private static final String ACCOUNT_EMAIL = "initial.account@gmail.com";
	
	private static final String ACCOUNT_PASSWORD = "account password";

	private Account account;
	
	private WebTarget target;
	
	public AccountWebserviceTest() {
		target = TestUtil.buildWebTarget();
	}
	
	@After
	public void tearDown() throws Exception {
		if(account.getAccountEmail() != null) {
			TestUtil.deleteAccount(account);
		}
		
		super.tearDown();
	}

	public void testSignup() {
		account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setAccountEmail(ACCOUNT_EMAIL);
		account.setAccountPassword(ACCOUNT_PASSWORD);
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone("+4 0100900900");
		account.setAccountSignupDate(new Date());
		Response response = target.path("account/signup").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(account));
		
		assertTrue("Status should be OK" , response.getStatus() == 
				WebserviceResponseStatus.OK.getCode());
		
		account = response.readEntity(Account.class);

		assertNotNull(account);
	}
	
	public void testSignupWithSameAccount() {
		account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setAccountEmail(ACCOUNT_EMAIL);
		account.setAccountPassword(ACCOUNT_PASSWORD);
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone("+4 0100900900");
		account.setAccountSignupDate(new Date());
		Response response = target.path("account/signup").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(account));
		
		assertTrue("Status should be OK" , response.getStatus() == 
				WebserviceResponseStatus.OK.getCode());
		
		account = response.readEntity(Account.class);

		assertNotNull(account);
		
		// Signup with same account.
		account = new Account();
		account.setAccountType((byte) 0);
		account.setAccountExternalId("sadsadkjhfsdfsdfsdddddddddddddddf");
		account.setAccountEmail(ACCOUNT_EMAIL);
		account.setAccountPassword(ACCOUNT_PASSWORD);
		account.setAccountFirstname("account firstname");
		account.setAccountLastname("account lastname");
		account.setAccountPhone("+4 0100900900");
		account.setAccountSignupDate(new Date());
		response = target.path("account/signup").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(account));
		
		assertTrue("Status shouldn't be OK: " + response.getStatus(), response.getStatus() != 
				WebserviceResponseStatus.OK.getCode());
		System.out.println("Status shouldn't be OK: " + response.getStatus());
	}
	
	
	public void testLogin() {
		account = TestUtil.createAccount();

		Form form = new Form();
		form.param("email", ACCOUNT_EMAIL);
		form.param("password", ACCOUNT_PASSWORD);
		Response response = target.path("account/login").request(MediaType.APPLICATION_JSON).post(Entity.entity(form,
				MediaType.APPLICATION_FORM_URLENCODED));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		Account loginResult = response.readEntity(Account.class);
		
		assertNotNull(loginResult);
		assertTrue(loginResult.getAccountEmail().equals(account.getAccountEmail()));
	}

	public void testChangePassword() {
		account = TestUtil.createAccount();
		
		Form form = new Form();
		form.param("email", ACCOUNT_EMAIL);
		form.param("password", ACCOUNT_PASSWORD);
		form.param("newPassword", "some new password");
		
		Response response = target.path("account/changepassword").request(MediaType.APPLICATION_JSON).post(Entity.entity(form,
				MediaType.APPLICATION_FORM_URLENCODED));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		String tokenKey = response.readEntity(String.class);
		assertTrue(!account.getTokenKey().equals(tokenKey));
	}
	
	public void testUpdateAccount() {
		String updatedEmail = "updated.email@email.com";
		String updatedPhone = "0260222111";
		account = TestUtil.createAccount();
		account.setAccountEmail(updatedEmail);
		account.setAccountPhone(updatedPhone);
		account.setAccountPassword(ACCOUNT_PASSWORD);
		
		AccountUpdate accountUpdate = new AccountUpdate();
		accountUpdate.accountEmail = ACCOUNT_EMAIL;
		accountUpdate.account = account;
		
		Response response = target.path("account/updateaccount").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(accountUpdate));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		Account result = response.readEntity(Account.class);
		
		assertNotNull(result);
		assertEquals(updatedEmail, result.getAccountEmail());
		assertEquals(updatedPhone, result.getAccountPhone());
		assertNull(result.getAccountId());
		assertNull(result.getAccountPassword());
	}
	
	public void testUpdateAccountWithAccountConflict() {
		String updatedEmail = "updated.email@email.com";
		String updatedPhone = "0260222111";
		Account conflictAccount = TestUtil.createAccount(updatedEmail, updatedPhone);
		account = TestUtil.createAccount();
		account.setAccountEmail(updatedEmail);
		account.setAccountPhone(updatedPhone);
		account.setAccountPassword(ACCOUNT_PASSWORD);
		
		AccountUpdate accountUpdate = new AccountUpdate();
		accountUpdate.accountEmail = ACCOUNT_EMAIL;
		accountUpdate.account = account;
		
		Response response = target.path("account/updateaccount").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(accountUpdate));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.ACCOUNT_CONFLICT.getCode());
		
		// Cleanup accounts.
		TestUtil.deleteAccount(conflictAccount);
	}
	
	public void testUpdateAccountNothingToUpdate() {
		account = TestUtil.createAccount();
		account.setAccountPassword(ACCOUNT_PASSWORD);
		
		AccountUpdate accountUpdate = new AccountUpdate();
		accountUpdate.accountEmail = ACCOUNT_EMAIL;
		accountUpdate.account = account;
		
		Response response = target.path("account/updateaccount").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(accountUpdate));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}
	
	public void testUpdateWithWrongPassword() {
		String updatedEmail = "updated.email@email.com";
		String updatedPhone = "0260222111";
		account = TestUtil.createAccount();
		account.setAccountEmail(updatedEmail);
		account.setAccountPhone(updatedPhone);
		account.setAccountPassword("wrong_password");
		
		AccountUpdate accountUpdate = new AccountUpdate();
		accountUpdate.accountEmail = ACCOUNT_EMAIL;
		accountUpdate.account = account;
		
		Response response = target.path("account/updateaccount").request(MediaType.APPLICATION_JSON)
				.post(Entity.json(accountUpdate));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.BAD_CREDENTIALS.getCode());
		
		// Reset account email in order to be deleted.
		account.setAccountEmail(ACCOUNT_EMAIL);
	}
}
