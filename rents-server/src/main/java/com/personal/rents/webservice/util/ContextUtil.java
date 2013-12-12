package com.personal.rents.webservice.util;

import javax.servlet.http.HttpServletRequest;

public final class ContextUtil {

	private static String baseURL;
	
	
	public static String getBaseURL(HttpServletRequest request) {
		if(baseURL == null) {
			String url = request.getRequestURL().toString();
			baseURL = url.substring(0, url.length() - request.getRequestURI().length()) 
					+ request.getContextPath() + "/";
		}
		
		return baseURL;
	}

}
