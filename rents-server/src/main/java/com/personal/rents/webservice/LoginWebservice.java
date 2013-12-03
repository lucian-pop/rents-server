package com.personal.rents.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.personal.rents.logic.AccountManager;
import com.personal.rents.model.Account;

@Path("login")
public class LoginWebservice {
	
	private static Logger logger = Logger.getLogger(LoginWebservice.class);

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Account login(@FormParam("email") String email, @FormParam("password") String password) {
		logger.info("A login request was received for user with email  " + email);
		
		Account account = AccountManager.login(email, password);
		
		return account;
	}
}
