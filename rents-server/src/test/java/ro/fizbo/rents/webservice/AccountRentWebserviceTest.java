package ro.fizbo.rents.webservice;

import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.logic.TokenGenerator;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Address;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentForm;
import ro.fizbo.rents.util.TestUtil;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;
import ro.fizbo.rents.webservice.util.ContextConstants;
import junit.framework.TestCase;

public class AccountRentWebserviceTest extends TestCase {
	
	private Rent rent; 
	
	private Account account;
	
	private WebTarget target;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		target = TestUtil.buildWebTarget();
		
		Address address = new Address();
		address.setAddressStreetNo("68A");
		address.setAddressStreetName("Observatorului");
		address.setAddressNeighbourhood("Zorilor");
		address.setAddressLocality("Cluj-Napoca");
		address.setAddressAdmAreaL1("Cluj");
		address.setAddressCountry("Romania");
		address.setAddressLatitude(46.7667);
		address.setAddressLongitude(23.5833);
		address.setAddressBuilding("C3");
		address.setAddressStaircase("2A");
		address.setAddressFloor((short) 4);
		address.setAddressAp("12B");
		
		rent = new Rent();
		rent.setAccountId(account.getAccountId());
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
		rent.setRentForm(RentForm.NORMAL.getForm());
	}

	@Override
	protected void tearDown() throws Exception {
		if(rent.getRentId() != null) {
			TestUtil.deleteRent(rent);
		}
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	public void testAddRent() {
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
	}
	
	public void testAddHotelierRent() {
		rent.setRentForm(RentForm.HOTELIER.getForm());
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
		assertTrue(rent.getRentForm() == RentForm.HOTELIER.getForm());
	}
	
	public void testAddRentWithoutPrivileges() {
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, TokenGenerator.generateToken())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
	
	public void testUpdateRent() {
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		
		response = target.path("account/rent/update").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));
		
		Integer result = response.readEntity(Integer.class);
		assertTrue(result == 1);
	}
}
