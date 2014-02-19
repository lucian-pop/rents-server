package com.personal.rents.webservice.util;

import javax.servlet.http.HttpServletRequest;

import com.personal.rents.logic.TokenManager;

public final class AuthorizationUtil {
	
	public static boolean isAuthorized(HttpServletRequest request, int accountId) {
		String reqTokenKey = request.getHeader(ContextConstants.TOKEN_KEY);
		if(reqTokenKey == null) {
			return false;
		}
		
		String accountTokenKey = TokenManager.getAuthToken(accountId);
		
		if(!reqTokenKey.equals(accountTokenKey)) {
			return false;
		}

		return true;
	}
	
	public static boolean isAuthorized(HttpServletRequest request) {
		int accountId = Integer.parseInt(request.getHeader(ContextConstants.ACCOUNT_ID));

		return isAuthorized(request, accountId);
	}
}
