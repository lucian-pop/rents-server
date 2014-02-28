package com.personal.rents.webservice.util;

import javax.servlet.http.HttpServletRequest;

import com.personal.rents.logic.TokenManager;
import com.personal.rents.model.Token;

public final class AuthorizationUtil {
	
	public static boolean isAuthorized(HttpServletRequest request) {
		String tokenKey = request.getHeader(ContextConstants.TOKEN_KEY);
		if(tokenKey == null) {
			return false;
		}

		Token token = TokenManager.getToken(tokenKey);

		return isAuthorized(token);
	}

	public static Token authorize(HttpServletRequest request) {
		String tokenKey = request.getHeader(ContextConstants.TOKEN_KEY);
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
}
