//package com.personal.rents.webservice;
//
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.Response;
//
//import junit.framework.TestCase;
//
//import com.personal.rents.model.Account;
//import com.personal.rents.util.TestUtil;
//import com.personal.rents.webservice.response.WebserviceResponseStatus;
//
//public class LogoutWebserviceTest extends TestCase {
//	
//	private Account account;
//	
//	private WebTarget target;
//	
//	public LogoutWebserviceTest() {
//		target = TestUtil.buildWebTarget();
//	}
//
//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//
//		account = TestUtil.createAccount();
//	}
//
//	@Override
//	protected void tearDown() throws Exception {
//		TestUtil.deleteAccount(account);
//		
//		super.tearDown();
//	}
//
//	public void testUnauthorizedLogout() {
//		Response response = target.path("logout").request().post(Entity.json(account.getId()));
//		
//		assertTrue("Response status should be FORBIDDEN", response.getStatus() == 
//				WebserviceResponseStatus.FORBIDDEN.getCode());
//	}
//	
//	public void testAuthorizedLogout() {
//		Response response = target.path("logout").request()
//				.header("tokenKey", account.getTokenKey()).post(Entity.json(account.getId()));
//		
//		assertTrue("Response status should be OK", response.getStatus() == 
//				WebserviceResponseStatus.OK.getCode());
//	}
//}
