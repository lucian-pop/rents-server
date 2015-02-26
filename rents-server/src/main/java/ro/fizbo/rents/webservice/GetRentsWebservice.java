package ro.fizbo.rents.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.logic.CurrencyManager;
import ro.fizbo.rents.logic.RentManager;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentForm;
import ro.fizbo.rents.model.RentStatus;
import ro.fizbo.rents.util.Constants;
import ro.fizbo.rents.webservice.exception.InvalidDataException;
import ro.fizbo.rents.webservice.util.ContextUtil;

@Path("rents")
public class GetRentsWebservice {

	@Path("map")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentsCounter getRentsByMapBoundaries(@QueryParam("minLatitude") double minLatitude,
			@QueryParam("maxLatitude") double maxLatitude, 
			@QueryParam("minLongitude") double minLongitude,
			@QueryParam("maxLongitude") double maxLongitude, 
			@QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request) {
		RentsCounter rentsCounter = RentManager.getRentsByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude, RentStatus.AVAILABLE.getStatus(), 
				RentForm.RENT.getForm(), pageSize);
		
		CurrencyManager.convertRentsListPrices(ContextUtil.getCurrency(request), rentsCounter.rents);
		
		return rentsCounter;
	}
	
	@Path("map/{rentForm}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentsCounter getRentsByMapBoundariesWithRentForm(
			@QueryParam("minLatitude") double minLatitude,
			@QueryParam("maxLatitude") double maxLatitude, 
			@QueryParam("minLongitude") double minLongitude,
			@QueryParam("maxLongitude") double maxLongitude, 
			@PathParam("rentForm") byte rentForm, @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request) {
		RentsCounter rentsCounter = RentManager.getRentsByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude, RentStatus.AVAILABLE.getStatus(), rentForm, pageSize);
		
		CurrencyManager.convertRentsListPrices(ContextUtil.getCurrency(request), rentsCounter.rents);

		return rentsCounter;
	}
	
	@Path("map/page")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rent> getRentsNextPageByMapBoundaries(@QueryParam("minLatitude") double minLatitude,
			@QueryParam("maxLatitude") double maxLatitude, 
			@QueryParam("minLongitude") double minLongitude,
			@QueryParam("maxLongitude") double maxLongitude, 
			@QueryParam("lastRentDate") String lastRentDate,
			@QueryParam("lastRentId") int lastRentId, @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request) {
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = null;
		try {
			date = dateFormat.parse(lastRentDate);
		} catch (ParseException e) {
			throw new InvalidDataException();
		}

		List<Rent> rents = RentManager.getRentsNextPageByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude, date, lastRentId, RentStatus.AVAILABLE.getStatus(),
				RentForm.RENT.getForm(), pageSize);
		
		CurrencyManager.convertRentsListPrices(ContextUtil.getCurrency(request), rents);
		
		return rents;
	}
	
	@Path("map/page/{rentForm}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rent> getRentsNextPageByMapBoundaries(@QueryParam("minLatitude") double minLatitude,
			@QueryParam("maxLatitude") double maxLatitude, 
			@QueryParam("minLongitude") double minLongitude,
			@QueryParam("maxLongitude") double maxLongitude, 
			@QueryParam("lastRentDate") String lastRentDate,
			@QueryParam("lastRentId") int lastRentId, @PathParam("rentForm") byte rentForm,
			@QueryParam("pageSize") int pageSize, @Context HttpServletRequest request) {
		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = null;
		try {
			date = dateFormat.parse(lastRentDate);
		} catch (ParseException e) {
			throw new InvalidDataException();
		}

		List<Rent> rents = RentManager.getRentsNextPageByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude, date, lastRentId, RentStatus.AVAILABLE.getStatus(),
				rentForm, pageSize);
		
		CurrencyManager.convertRentsListPrices(ContextUtil.getCurrency(request), rents);
		
		return rents;
	}

}
