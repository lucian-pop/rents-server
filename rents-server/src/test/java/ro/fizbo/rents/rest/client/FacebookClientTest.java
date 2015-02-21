package ro.fizbo.rents.rest.client;

import ro.fizbo.rents.util.TestUtil;
import junit.framework.TestCase;

public class FacebookClientTest extends TestCase {
	
	public void testSuccessfullyGetAndroidAppAccessToken() {
		String appAccessToken = FacebookClient.getAndroidAppAccessToken();
		
		assertNotNull(appAccessToken);
	}
	
	public void testSuccessfullyGetIoSAppAccessToken() {
		String appAccessToken = FacebookClient.getAndroidAppAccessToken();
		
		assertNotNull(appAccessToken);
	}
	
	public void testValidateAndroidValidUserAccessToken() {
		boolean isValid = FacebookClient.validateUserAccessToken(TestUtil.FACEBOOK_ANDROID_EXTERNAL_ID,
				TestUtil.FACEBOOK_ANDROID_ACCESS_TOKEN, false);
		
		assertEquals(true, isValid);
	}
	
	public void testValidateIoSValidUserAccessToken() {
		boolean isValid = FacebookClient.validateUserAccessToken(TestUtil.FACEBOOK_IOS_EXTERNAL_ID,
				TestUtil.FACEBOOK_IOS_ACCESS_TOKEN, true);
		
		assertEquals(true, isValid);
	}
	
	public void testValidateAccessTokenWithInvalidAndroidUserId() {
		boolean isValid = FacebookClient.validateUserAccessToken("123455574545",
				TestUtil.FACEBOOK_ANDROID_ACCESS_TOKEN, false);
		
		assertEquals(false, isValid);
	}
	
	public void testValidateAccessTokenWithInvalidIoSUserId() {
		boolean isValid = FacebookClient.validateUserAccessToken("123455574545",
				TestUtil.FACEBOOK_ANDROID_ACCESS_TOKEN, false);
		
		assertEquals(false, isValid);
	}
	
	public void testValidateAccessTokenWithInvalidAndroidUserAccessToken() {
		boolean isValid = FacebookClient.validateUserAccessToken(TestUtil.FACEBOOK_ANDROID_EXTERNAL_ID,
				"dsfsdfSDFSDFSDFSDfsdfsdf", false);
		
		assertEquals(false, isValid);
	}
	
	public void testValidateAccessTokenWithInvalidIoSUserAccessToken() {
		boolean isValid = FacebookClient.validateUserAccessToken(TestUtil.FACEBOOK_IOS_EXTERNAL_ID,
				"dsfsdfSDFSDFSDFSDfsdfsdf", false);
		
		assertEquals(false, isValid);
	}
}
