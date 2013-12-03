package com.personal.rents.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.personal.rents.logic.AccountManager;
import com.personal.rents.model.Account;

/**
 * Web service that performs user sign-up.
 *
 */
@Path("signup")
public class SignupWebservice {
	
	private static Logger logger = Logger.getLogger(SignupWebservice.class);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Account signup(Account account) {
		logger.info("An sign-up request was received from " + account.getFirstname() + " " 
				+ account.getLastname() + " with email " + account.getEmail());

		account = AccountManager.createAccount(account);
		
		return account;
	}
}
