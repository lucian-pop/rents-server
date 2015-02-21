package ro.fizbo.rents.webservice;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.util.TestUtil;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;
import ro.fizbo.rents.webservice.util.ContextConstants;
import junit.framework.TestCase;

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
	
	public void testGetDetailedRentWithoutCurrency() {
		Response response = target.path("rent/detailed").queryParam("rentId", rent.getRentId())
				.request(MediaType.APPLICATION_JSON).get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		Rent result = response.readEntity(Rent.class);

		assertTrue(result != null);
		assertTrue(result.getAddress() != null);
	}
	
	public void testGetDetailedRentWithCurrency() {
		Response response = target.path("rent/detailed").queryParam("rentId", rent.getRentId())
				.request(MediaType.APPLICATION_JSON).header(ContextConstants.CURRENCY, Currency.RON)
					.get();
		
		assertTrue(response.getStatus()==WebserviceResponseStatus.OK.getCode());
		
		Rent result = response.readEntity(Rent.class);

		assertTrue(result != null);
		assertTrue(result.getAddress() != null);
	}
}
