package ro.fizbo.rents.webservice.util;

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
	
	public static String getCurrency(HttpServletRequest request) {
		String currency = request.getHeader(ContextConstants.CURRENCY);
		if(currency == null || currency == "") {
			currency = ro.fizbo.rents.model.Currency.EUR.toString();
		}
		return currency;
	}
	
	public static int getVersion(HttpServletRequest request) {
		String version = request.getHeader(ContextConstants.VERSION);
		if(version == null || version == "") {
			version = "1";
		}
		return Integer.parseInt(version);
	}

}
