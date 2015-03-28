package ro.fizbo.rents.webservice;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.logic.TokenGenerator;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentAbuse;
import ro.fizbo.rents.model.RentAbuseStatus;
import ro.fizbo.rents.util.EmailConstants;
import ro.fizbo.rents.util.TestUtil;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;
import ro.fizbo.rents.webservice.util.HeadersConstants;
import junit.framework.TestCase;

public class ReportAbuseWebserviceTest extends TestCase {
	
	private Account account;
	
	private Rent rent;
	
	private WebTarget target;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		target = TestUtil.buildWebTarget();
		account = TestUtil.createAccount();
		rent = TestUtil.addRent(account.getAccountId());
	}
	
	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteRent(rent);
		TestUtil.deleteAccount(account);
		
		super.tearDown();
	}

	public void testReportRentAbuse() {
		RentAbuse rentAbuse = new RentAbuse();
		rentAbuse.setRentAbuseEmail(EmailConstants.ADMIN_REPORT_EMAIL);
		rentAbuse.setRentAbusePhone("0741060776");
		rentAbuse.setRentAbuseDescription("Agentie imobiliara");
		rentAbuse.setRent(rent);
		Response response = target.path("report/rent-abuse").request(MediaType.APPLICATION_JSON)
				.header(HeadersConstants.VERSION, "1")
				.header(HeadersConstants.USER_AGENT, HeadersConstants.ANDROID)
				.post(Entity.json(rentAbuse));
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK_NO_CONTENT.getCode());
	}
	
	public void testRentAbuseSolved() {
		Form form = new Form();
		form.param("rentAbuseTokenKey", TokenGenerator.generateToken());
		form.param("rentAbuseStatus", Byte.toString(RentAbuseStatus.RESOLVED_NOT_OK.getStatus()));
		form.param("rentAbuseResolutionComment", "This is a dummy comment: ");
		Response response = target.path("report/rent-abuse/solved").request(MediaType.APPLICATION_JSON)
				.header(HeadersConstants.VERSION, "1")
				.header(HeadersConstants.USER_AGENT, HeadersConstants.ANDROID).post(Entity.entity(form,
				MediaType.APPLICATION_FORM_URLENCODED));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK_NO_CONTENT.getCode());
	}
}
