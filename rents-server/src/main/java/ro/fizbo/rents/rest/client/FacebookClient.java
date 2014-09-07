package ro.fizbo.rents.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.rest.model.FacebookDebugToken;
import ro.fizbo.rents.webservice.jsonprovider.GsonMessageBodyHandler;
import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;

public final class FacebookClient {
	
	private static final String FACEBOOK_GRAPH_URI = "https://graph.facebook.com/";
	
	private static final String FACEBOOK_APP_ID = "361971663950886";
	
	private static final String FACEBOOK_APP_SECRET = "c2397f1ca019e1340b7c803d58c826c0";
	
	private final static WebTarget facebookTarget;

	private FacebookClient() {
	}
	
	static {
		final Client client = ClientBuilder.newBuilder()
				.register(GsonMessageBodyHandler.class)
				.build();
		
		facebookTarget = client.target(FACEBOOK_GRAPH_URI);
	}
	
	public static String getAppAccessToken() {
		Response response = facebookTarget.path("oauth/access_token").queryParam("client_id", FACEBOOK_APP_ID)
				.queryParam("client_secret", FACEBOOK_APP_SECRET)
				.queryParam("grant_type", "client_credentials")
				.request(MediaType.APPLICATION_JSON)
				.get();
		
		if(response.getStatus() == WebserviceResponseStatus.OK.getCode()) {
			return response.readEntity(String.class).split("=")[1];
		}
		
		return null;
	}
	
	public static boolean validateUserAccessToken(String userId, String userAccessToken) {
		String appAccessToken = getAppAccessToken();
		
		if(appAccessToken == null) {
			return false;
		}

		Response response = facebookTarget.path("debug_token").queryParam("input_token", userAccessToken)
				.queryParam("access_token", appAccessToken)
				.request(MediaType.APPLICATION_JSON)
				.get();
		
		if(response.getStatus() == WebserviceResponseStatus.OK.getCode()) {
			FacebookDebugToken debugToken = response.readEntity(FacebookDebugToken.class);
			if(debugToken != null && debugToken.data.is_valid && debugToken.data.app_id.equals(FACEBOOK_APP_ID) 
					&& debugToken.data.user_id.equals(userId)) {
				return true;
			}
		}

		return false;
	}
	
}
