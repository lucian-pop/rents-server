package com.personal.rents.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.personal.rents.logic.RentFavoritesManager;
import com.personal.rents.webservice.exception.UnauthorizedException;
import com.personal.rents.webservice.util.AuthorizationUtil;
import com.personal.rents.webservice.util.ContextConstants;

@Path("rentfavorites")
public class RentFavoritesWebservice {
	
	@Path("addrent")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addRentToFavorites(int rentId, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}
		
		int accountId = request.getIntHeader(ContextConstants.ACCOUNT_ID);
		
		return RentFavoritesManager.addRentToFavorites(accountId, rentId);
	}

}
