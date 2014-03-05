package com.personal.rents.webservice;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.TestCase;

import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;

public class GetRentWebserviceTest extends TestCase{

	private WebTarget target;
	
	private Account account;

	private Rent rent ;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		
		rent  = TestUtil.addRent(account.getAccountId());

		target = TestUtil.buildWebTarget();
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteRent(rent);
		
		TestUtil.deleteAccount(account);

		super.tearDown();
	}
	
	public void testGetDetailedRent() {
		Response response = target.path("rent/detailed").queryParam("rentId", rent.getRentId())
				.request(MediaType.APPLICATION_JSON).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		Rent result = response.readEntity(Rent.class);

		assertTrue(result != null);
		assertTrue(result.getAddress() != null);
	}
}
