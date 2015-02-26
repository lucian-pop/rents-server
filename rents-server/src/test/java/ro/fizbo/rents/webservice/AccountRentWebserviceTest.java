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
import ro.fizbo.rents.model.RentArchitecture;
import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.model.RentForm;
import ro.fizbo.rents.model.RentParty;
import ro.fizbo.rents.model.RentType;
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
		rent.setRentParty((byte) 0);
		rent.setRentType((byte) 1);
		rent.setRentArchitecture((byte) 1);
		rent.setRentAge((short) 10);
		rent.setRentDescription("some dummy text here");
		rent.setRentPetsAllowed(true);
		rent.setRentPhone("0750110440");
		rent.setRentAddDate(new Date());
		rent.setRentStatus((byte) 0);
	}

	@Override
	protected void tearDown() throws Exception {
		if(rent.getRentId() != null) {
			TestUtil.deleteRent(rent);
		}
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	/** Test adding rent with old rent model structure. */
	public void testAddRentWithOldStructure() {
		rent.setRentForm(RentForm.RENT.getForm());
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
	}
	
	public void testAddAccommodationWithOldStructure() {
		rent.setRentForm(RentForm.ACCOMMODATION.getForm());
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
		assertTrue(rent.getRentForm() == RentForm.ACCOMMODATION.getForm());
	}
	
	/** Test adding rent without old rent required fields that are no longer required. */
	public void testAddRentWithNewStructureWithoutPartyAndAge() {
		rent.setRentForm(RentForm.RENT.getForm());
		rent.setRentParty(null);
		rent.setRentAge(null);
		rent.setRentCurrency(Currency.EUR.toString());
		
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
	}
	
	/** Test adding rent for the new structure and just with required fields.*/
	public void testAddRentWithNewStructureWithJustRequiredValues() {
		rent.setRentForm(RentForm.RENT.getForm());
		rent.setRentParty(null);
		rent.setRentAge(null);
		rent.setRentBaths(null);
		rent.setRentArchitecture(null);
		rent.setRentSurface(null);
		rent.setRentGuests(null);
		rent.setRentSingleBeds(null);
		rent.setRentDoubleBeds(null);
		rent.setRentPetsAllowed(true);
		rent.setRentParkingPlace(null);
		rent.setRentSmokersAllowed(null);
		rent.setRentCurrency(Currency.EUR.toString());
		
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
	}
	
	public void testtAddRentWithNewStructureAndAllFields() {
		rent.setRentForm(RentForm.RENT.getForm());
		rent.setRentBaths((short) 2);
		rent.setRentArchitecture((byte) 1);
		rent.setRentSurface(60);
		rent.setRentDescription("Dummy description");
		rent.setRentPetsAllowed(true);
		rent.setRentParkingPlace(true);
		rent.setRentSmokersAllowed(true);
		rent.setRentCurrency(Currency.EUR.toString());
		
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
	}
	
	public void testAddAccommodationWithNewStructureAndAllFields() {
		rent.setRentForm(RentForm.ACCOMMODATION.getForm());
		rent.setRentBaths((short) 2);
		rent.setRentArchitecture((byte) 1);
		rent.setRentSurface(60);
		rent.setRentGuests((short) 5);
		rent.setRentSingleBeds((short) 1);
		rent.setRentDoubleBeds((short) 2);
		rent.setRentDescription("Dummy description");
		rent.setRentPetsAllowed(true);
		rent.setRentParkingPlace(true);
		rent.setRentSmokersAllowed(true);
		rent.setRentCurrency(Currency.EUR.toString());
		
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		assertTrue(rent.getRentId() != null);
	}
	
	public void testAddRentWithoutPrivileges() {
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, TokenGenerator.generateToken())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.UNAUTHORIZED.getCode());
	}
	
	public void testUpdateRentWithOldStructure() {
		rent.setRentForm(RentForm.RENT.getForm());
		rent.setRentCurrency(Currency.EUR.toString());
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
	
	public void testUpdateRentWithOldStructureChangeRentForm() {
		rent.setRentForm(RentForm.RENT.getForm());
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		rent.setRentForm(RentForm.ACCOMMODATION.getForm());

		response = target.path("account/rent/update").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));
		
		Integer result = response.readEntity(Integer.class);
		assertTrue(result == 1);
	}
	
	/** Test update rent without old rent required fields that are no longer required. */
	public void testUpdateRentWithNewStructureWithoutPartyAndAge() {
		rent.setRentForm(RentForm.RENT.getForm());
		rent.setRentCurrency(Currency.EUR.toString());
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		rent.setRentParty(null);
		rent.setRentAge(null);

		response = target.path("account/rent/update").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));
		
		Integer result = response.readEntity(Integer.class);
		assertTrue(result == 1);
	}
	
	public void testUpdateRentWithNewStructureAndJustWithRequiredFields() {
		rent.setRentForm(RentForm.RENT.getForm());
		rent.setRentCurrency(Currency.EUR.toString());
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		rent.setRentParty(null);
		rent.setRentAge(null);
		rent.setRentBaths(null);
		rent.setRentArchitecture(null);
		rent.setRentSurface(null);
		rent.setRentGuests(null);
		rent.setRentSingleBeds(null);
		rent.setRentDoubleBeds(null);
		rent.setRentPetsAllowed(true);
		rent.setRentParkingPlace(null);
		rent.setRentSmokersAllowed(null);

		response = target.path("account/rent/update").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));
		
		Integer result = response.readEntity(Integer.class);
		assertTrue(result == 1);
	}
	
	public void testUpdateRentWithNewStructureAndAllFields() {
		rent.setRentForm(RentForm.RENT.getForm());
		rent.setRentCurrency(Currency.EUR.toString());
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		rent.setRentType(RentType.APARTMENT.getType());
		rent.setRentRooms((short) 3);
		rent.setRentBaths((short) 1);
		rent.setRentArchitecture(RentArchitecture.DETACHED.getArchitecture());
		rent.setRentSurface(80);
		rent.setRentDescription("Dummy description");
		rent.setRentPetsAllowed(true);
		rent.setRentParkingPlace(true);
		rent.setRentSmokersAllowed(true);

		response = target.path("account/rent/update").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));
		
		Integer result = response.readEntity(Integer.class);
		assertTrue(result == 1);
	}
	
	public void testUpdateAccommodationWithNewStructureAndAllFields() {
		rent.setRentForm(RentForm.RENT.getForm());
		rent.setRentCurrency(Currency.EUR.toString());
		Response response = target.path("account/rent/add").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));

		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());

		rent = response.readEntity(Rent.class);
		rent.setRentType(RentType.APARTMENT.getType());
		rent.setRentRooms((short) 3);
		rent.setRentGuests((short) 5);
		rent.setRentSingleBeds((short) 3);
		rent.setRentDoubleBeds((short) 1);
		rent.setRentBaths((short) 1);
		rent.setRentArchitecture(RentArchitecture.DETACHED.getArchitecture());
		rent.setRentSurface(80);
		rent.setRentDescription("Dummy description");
		rent.setRentPetsAllowed(true);
		rent.setRentParkingPlace(true);
		rent.setRentSmokersAllowed(true);

		response = target.path("account/rent/update").request(MediaType.APPLICATION_JSON)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.json(rent));
		
		Integer result = response.readEntity(Integer.class);
		assertTrue(result == 1);
	}
}
