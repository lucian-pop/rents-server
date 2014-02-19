package com.personal.rents.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.logic.RentManager;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentStatus;
import com.personal.rents.webservice.exception.InvalidDataException;
import com.personal.rents.webservice.exception.UnauthorizedException;
import com.personal.rents.webservice.util.AuthorizationUtil;
import com.personal.rents.webservice.util.GeneralConstants;

@Path("rents")
public class UserRentsWebservice {

	@Path("useradded")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentsCounter getUserAddedRents(@QueryParam("accountId") int accountId,
			@QueryParam("pageSize") int pageSize, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request, accountId)) {
			throw new UnauthorizedException();
		}

		return RentManager.getUserAddedRents(accountId,
				RentStatus.AVAILABLE.getStatus(), pageSize);
	}
	
	@Path("useradded/page")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rent> getUserAddedRentsNextPage(@QueryParam("accountId") int accountId, 
			@QueryParam("lastRentDate") String lastRentDate,
			@QueryParam("lastRentId") int lastRentId,
			@QueryParam("pageSize") int pageSize, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request, accountId)) {
			throw new UnauthorizedException();
		}
		
		DateFormat dateFormat = new SimpleDateFormat(GeneralConstants.DATE_FORMAT);
		Date date = null;
		try {
			date = dateFormat.parse(lastRentDate);
		} catch (ParseException e) {
			throw new InvalidDataException();
		}

		return RentManager.getUserAddedRentsNextPage(accountId, RentStatus.AVAILABLE.getStatus(),
				date, lastRentId, pageSize);
	}
	
	@Path("useradded/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int deleteUserAddedRents(List<Integer> rentIds, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}
		
		return RentManager.updateRentsStatus(rentIds, RentStatus.NOT_AVAILABLE.getStatus());
	}
}
