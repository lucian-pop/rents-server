package com.personal.rents.webservice;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.personal.rents.logic.AccountManager;
import com.personal.rents.webservice.exception.ForbiddenException;

@Path("changepassword")
public class ChangePasswordWebservice {
	
	private static Logger logger = Logger.getLogger(LoginWebservice.class);
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String changePassword(@FormParam("email") String email, 
			@FormParam("password") String password, @FormParam("newPassword") String newPassword,
			@Context HttpServletResponse response) {
		logger.info("A change password request was received for user with email  " + email);
		
		String tokenKey = AccountManager.changePassword(email, password, newPassword);
		if(tokenKey == null) {
			throw new ForbiddenException();
		}

		return tokenKey;
	}

}
