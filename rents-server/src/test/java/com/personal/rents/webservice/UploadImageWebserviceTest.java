package com.personal.rents.webservice;

import java.util.Random;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.personal.rents.model.Account;
import com.personal.rents.util.TestUtil;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.ContextConstants;

import junit.framework.TestCase;

public class UploadImageWebserviceTest extends TestCase {
	
	private static final int NO_OF_IMG_BYTES = 60*1024;
	
	private Account account;
	
	private WebTarget target;
			
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		account = TestUtil.createAccount();
		
		target = TestUtil.buildMultiPartWebTarget();
	}
	
	@Override
	protected void tearDown() throws Exception {
		TestUtil.deleteAccount(account);

		super.tearDown();
	}

	public void testSuccessfullyUploadImage() {
		byte[] imageBytes = new byte[NO_OF_IMG_BYTES];
		new Random().nextBytes(imageBytes);
		String filename = "1.jpg";
		String datetime = "12345678";

		FormDataMultiPart formMultiPartData = new FormDataMultiPart();
		formMultiPartData.field("image", imageBytes, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		formMultiPartData.field("filename", filename);
		formMultiPartData.field("datetime", datetime);
		Response response = target.path("uploadimage").request()
				.header(ContextConstants.TOKEN_KEY, account.getTokenKey())
				.post(Entity.entity(formMultiPartData, formMultiPartData.getMediaType()));
		
		assertTrue(response.getStatus() == WebserviceResponseStatus.OK.getCode());
	}

}
