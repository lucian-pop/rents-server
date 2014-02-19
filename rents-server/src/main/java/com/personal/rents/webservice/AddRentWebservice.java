package com.personal.rents.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.personal.rents.logic.RentManager;
import com.personal.rents.model.Rent;
import com.personal.rents.webservice.exception.UnauthorizedException;
import com.personal.rents.webservice.util.AuthorizationUtil;

@Path("addrent")
public class AddRentWebservice {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Rent addRent(Rent rent, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request, rent.getAccountId())) {
			throw new UnauthorizedException();
		}
		
		return RentManager.addRent(rent);
	}
}
