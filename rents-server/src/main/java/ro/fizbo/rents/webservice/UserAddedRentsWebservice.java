package ro.fizbo.rents.webservice;

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

import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.logic.CurrencyManager;
import ro.fizbo.rents.logic.RentManager;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentStatus;
import ro.fizbo.rents.model.Token;
import ro.fizbo.rents.util.Constants;
import ro.fizbo.rents.webservice.exception.InvalidDataException;
import ro.fizbo.rents.webservice.exception.UnauthorizedException;
import ro.fizbo.rents.webservice.util.AuthorizationUtil;
import ro.fizbo.rents.webservice.util.ContextUtil;

@Path("account/rents")
public class UserAddedRentsWebservice {

	@Path("added")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentsCounter getUserAddedRents(@QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request) {
		Token token = AuthorizationUtil.authorize(request);
		if(!AuthorizationUtil.isAuthorized(token)) {
			throw new UnauthorizedException();
		}

		RentsCounter rentsCounter = RentManager.getUserAddedRents(token.getAccountId(), 
				RentStatus.AVAILABLE.getStatus(), pageSize);
		CurrencyManager.convertRentsListPrices(ContextUtil.getCurrency(request), 
				rentsCounter.rents);
		return rentsCounter;
	}
	
	@Path("added/page")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rent> getUserAddedRentsNextPage(@QueryParam("lastRentDate") String lastRentDate,
			@QueryParam("lastRentId") int lastRentId, @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request) {
		Token token = AuthorizationUtil.authorize(request);
		if(!AuthorizationUtil.isAuthorized(token)) {
			throw new UnauthorizedException();
		}
		
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = null;
		try {
			date = dateFormat.parse(lastRentDate);
		} catch (ParseException e) {
			throw new InvalidDataException();
		}
		
		List<Rent> rents = RentManager.getUserAddedRentsNextPage(token.getAccountId(),
				RentStatus.AVAILABLE.getStatus(), date, lastRentId, pageSize);
		CurrencyManager.convertRentsListPrices(ContextUtil.getCurrency(request), rents);

		return rents;
	}
	
	@Path("added/delete")
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
