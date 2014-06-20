package ro.fizbo.rents.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import ro.fizbo.rents.dto.AccountUpdate;
import ro.fizbo.rents.logic.AccountManager;
import ro.fizbo.rents.model.Account;

@Path("account")
public class AccountWebservice {
	
	private static Logger logger = Logger.getLogger(AccountWebservice.class);
	
	@Path("signup")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Account signup(Account account) {
		logger.info("An sign-up request was received from " + account.getAccountFirstname() + " " 
				+ account.getAccountLastname() + " with email " + account.getAccountEmail());

		account = AccountManager.createAccount(account);
		
		return account;
	}
	
	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Account login(@FormParam("email") String email, @FormParam("password") String password) {
		Account account = AccountManager.login(email, password);
		
		return account;
	}
	
	@Path("update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Account updateAccount(AccountUpdate accountUpdate, @Context HttpServletRequest request) {
		return AccountManager.updateAccount(accountUpdate);
	}
	
	@Path("changepassword")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String changePassword(@FormParam("email") String email, 
			@FormParam("password") String password, @FormParam("newPassword") String newPassword,
			@Context HttpServletResponse response) {

		return AccountManager.changePassword(email, password, newPassword);
	}
}
