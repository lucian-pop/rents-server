package com.personal.rents.webservice;

import java.util.Random;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.ContextConstants;

import junit.framework.TestCase;

public class RentImageWebserviceTest extends TestCase {
	
	private static final int NO_OF_IMG_BYTES = 60*1024;
	
	private Account account;
	
	private Rent rent;
	
	private WebTarget target;
			
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		rent = TestUtil.addRent(account.getAccountId());
		target = TestUtil.buildMultiPartWebTarget();
	}
	
	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteRent(rent);
		TestUtil.deleteAccount(account);

		super.tearDown();
	}

	public void testSuccessfullyUploadImage() {
		byte[] imageBytes = new byte[NO_OF_IMG_BYTES];
		new Random().nextBytes(imageBytes);

		FormDataMultiPart formMultiPartData = new FormDataMultiPart();
		formMultiPartData.field("image", imageBytes, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		formMultiPartData.field("rentId", Integer.toString(rent.getRentId()));
		Response response = target.path("rentimage").request()
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.entity(formMultiPartData, formMultiPartData.getMediaType()));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}

}
