package ro.fizbo.rents.webservice.util;

import javax.servlet.http.HttpServletRequest;

import ro.fizbo.rents.logic.TokenManager;
import ro.fizbo.rents.model.Token;

public final class AuthorizationUtil {
	
	public static boolean isAuthorized(HttpServletRequest request) {
		String tokenKey = request.getHeader(HeadersConstants.TOKEN_KEY);
		if(tokenKey == null) {
			return false;
		}

		Token token = TokenManager.getToken(tokenKey);

		return isAuthorized(token);
	}

	public static Token authorize(HttpServletRequest request) {
		String tokenKey = request.getHeader(HeadersConstants.TOKEN_KEY);
		if(tokenKey == null) {
			return null;
		}

		return TokenManager.getToken(tokenKey);
	}
	
	public static boolean isAuthorized(Token token) {
		if(token == null) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isIosClient(HttpServletRequest request) {
		String userAgent = request.getHeader(HeadersConstants.USER_AGENT);
		if(userAgent != null && userAgent.toLowerCase().contains(HeadersConstants.IOS)) {
			return true;
		}

		return false;
	}
}
