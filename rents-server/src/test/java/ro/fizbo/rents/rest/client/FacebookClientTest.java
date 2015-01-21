package ro.fizbo.rents.rest.client;

import ro.fizbo.rents.util.TestUtil;
import junit.framework.TestCase;

public class FacebookClientTest extends TestCase {
	
	public void testSuccessfullyGetAppAccessToken() {
		String appAccessToken = FacebookClient.getAndroidAppAccessToken();
		
		assertNotNull(appAccessToken);
	}
	
	public void testValidateAndroidValidUserAccessToken() {
		boolean isValid = FacebookClient.validateUserAccessToken(TestUtil.USER_EXTERNAL_ID,
				TestUtil.USER_ACCESS_TOKEN, false);
		
		assertEquals(true, isValid);
	}
	
	public void testValidateIosValidUserAccessToken() {
		boolean isValid = FacebookClient.validateUserAccessToken(TestUtil.USER_EXTERNAL_ID,
				TestUtil.USER_ACCESS_TOKEN, true);
		
		assertEquals(true, isValid);
	}
	
	public void testValidateAccessTokenWithInvalidUserId() {
		boolean isValid = FacebookClient.validateUserAccessToken("123455574545",
				TestUtil.USER_ACCESS_TOKEN, false);
		
		assertEquals(false, isValid);
	}
	
	public void testValidateAccessTokenWithInvalidUserAccessToken() {
		boolean isValid = FacebookClient.validateUserAccessToken(TestUtil.USER_EXTERNAL_ID,
				"dsfsdfSDFSDFSDFSDfsdfsdf", false);
		
		assertEquals(false, isValid);
	}
}
