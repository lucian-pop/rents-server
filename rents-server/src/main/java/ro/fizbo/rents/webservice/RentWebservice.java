package ro.fizbo.rents.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ro.fizbo.rents.logic.RentManager;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.Token;
import ro.fizbo.rents.webservice.exception.UnauthorizedException;
import ro.fizbo.rents.webservice.util.AuthorizationUtil;

@Path("rent")
public class RentWebservice {
	
	@Path("add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Rent addRent(Rent rent, @Context HttpServletRequest request) {
		Token token = AuthorizationUtil.authorize(request);
		if(!AuthorizationUtil.isAuthorized(token)) {
			throw new UnauthorizedException();
		}

		rent.setAccountId(token.getAccountId());

		return RentManager.addRent(rent);
	}
	
	@Path("update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int updateRent(Rent rent, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}

		return RentManager.updateRent(rent);
	}
}
