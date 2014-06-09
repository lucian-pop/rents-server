package com.personal.rents.webservice;

import java.util.Random;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.personal.rents.model.Account;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentImage;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.ContextConstants;

import junit.framework.TestCase;

public class RentImageWebserviceTest extends TestCase {
	
	private static final int NO_OF_IMG_BYTES = 100*1024;
	
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

		Response response = target.path("rentimage/upload").request(MediaType.APPLICATION_OCTET_STREAM)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.header(ContextConstants.RENT_ID, Integer.toString(rent.getRentId()))
				.header("accept", "*/*")
				.post(Entity.entity(imageBytes, MediaType.APPLICATION_OCTET_STREAM));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		RentImage rentImage = response.readEntity(RentImage.class);
		assertNotNull(rentImage);
	}
	
	public void testSuccessfullyReplaceRentImage() {
		byte[] imageBytes = new byte[NO_OF_IMG_BYTES];
		new Random().nextBytes(imageBytes);
		
		Response response = target.path("rentimage/upload").request(MediaType.APPLICATION_OCTET_STREAM)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.header(ContextConstants.RENT_ID, Integer.toString(rent.getRentId()))
				.header("accept", "*/*")
				.post(Entity.entity(imageBytes, MediaType.APPLICATION_OCTET_STREAM));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		RentImage rentImage = response.readEntity(RentImage.class);
		assertNotNull(rentImage);
		
		response = target.path("rentimage/replace").request(MediaType.APPLICATION_OCTET_STREAM)
				.header(ContextConstants.RENT_IMAGE_ID, Integer.toString(rentImage.getRentImageId()))
				.header(ContextConstants.RENT_IMAGE_URI, rentImage.getRentImageURI())
				.header(ContextConstants.RENT_ID, Integer.toString(rent.getRentId()))
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.header("accept", "*/*")
				.put(Entity.entity(imageBytes, MediaType.APPLICATION_OCTET_STREAM));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		rentImage = response.readEntity(RentImage.class);
		assertNotNull(rentImage);
	}
	
	public void testSuccessfullyDeleteRentImage() {
		byte[] imageBytes = new byte[NO_OF_IMG_BYTES];
		new Random().nextBytes(imageBytes);

		Response response = target.path("rentimage/upload").request(MediaType.APPLICATION_OCTET_STREAM)
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.header(ContextConstants.RENT_ID, Integer.toString(rent.getRentId()))
				.header("accept", "*/*")
				.post(Entity.entity(imageBytes, MediaType.APPLICATION_OCTET_STREAM));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
		
		RentImage rentImage = response.readEntity(RentImage.class);
		assertNotNull(rentImage);
		
		response = target.path("rentimage/delete/" + rentImage.getRentImageId()).request()
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.delete();
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}
}
