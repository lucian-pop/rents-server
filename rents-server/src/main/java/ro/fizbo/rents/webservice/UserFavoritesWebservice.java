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

import ro.fizbo.rents.dto.RentFavoriteViewsCounter;
import ro.fizbo.rents.logic.RentFavoriteManager;
import ro.fizbo.rents.logic.RentManager;
import ro.fizbo.rents.model.Token;
import ro.fizbo.rents.model.view.RentFavoriteView;
import ro.fizbo.rents.util.Constants;
import ro.fizbo.rents.webservice.exception.InvalidDataException;
import ro.fizbo.rents.webservice.exception.UnauthorizedException;
import ro.fizbo.rents.webservice.util.AuthorizationUtil;

@Path("account/rents")
public class UserFavoritesWebservice {
	
	@Path("favorites")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentFavoriteViewsCounter getUserFavoriteRents(@QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request) {
		Token token = AuthorizationUtil.authorize(request);
		if(!AuthorizationUtil.isAuthorized(token)) {
			throw new UnauthorizedException();
		}
		
		return RentManager.getUserFavoriteRents(token.getAccountId(), pageSize);
	}
	
	@Path("favorites/page")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<RentFavoriteView> getUserFavoriteRentsNextPage(
			@QueryParam("lastDate") String lastDate, @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request) {
		Token token = AuthorizationUtil.authorize(request);
		if(!AuthorizationUtil.isAuthorized(token)) {
			throw new UnauthorizedException();
		}
		
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = null;
		try {
			date = dateFormat.parse(lastDate);
		} catch (ParseException e) {
			throw new InvalidDataException();
		}
		
		return RentManager.getUserFavoriteRentsNextPage(token.getAccountId(), date, pageSize);
	}
	
	@Path("favorites/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addRentToFavorites(int rentId, @Context HttpServletRequest request) {
		Token token = AuthorizationUtil.authorize(request);
		if(!AuthorizationUtil.isAuthorized(token)) {
			throw new UnauthorizedException();
		}
		
		return RentFavoriteManager.addRentToFavorites(token.getAccountId(), rentId);
	}
	
	@Path("favorites/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int deleteUserFavoriteRents(List<Integer> rentIds, @Context HttpServletRequest request) {
		Token token = AuthorizationUtil.authorize(request);
		if(!AuthorizationUtil.isAuthorized(token)) {
			throw new UnauthorizedException();
		}
		
		return RentFavoriteManager.deleteUserFavoriteRents(token.getAccountId(), rentIds);
	}
}
