package com.personal.rents.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.logic.RentManager;
import com.personal.rents.model.Rent;
import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.GeneralConstants;

@Path("rents")
public class GetRentsWebservice {

	@Path("light")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentsCounter getRentsByMapBoundaries(@QueryParam("minLatitude") double minLatitude,
			@QueryParam("maxLatitude") double maxLatitude, 
			@QueryParam("minLongitude") double minLongitude,
			@QueryParam("maxLongitude") double maxLongitude, @QueryParam("pageSize") int pageSize) {
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		RentsCounter rentsCounter = RentManager.getRentsByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude, pageSize);
		
		return rentsCounter;
	}
	
	@Path("light/page")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rent> getRentsNextPageByMapBoundaries(@QueryParam("minLatitude") double minLatitude,
			@QueryParam("maxLatitude") double maxLatitude, 
			@QueryParam("minLongitude") double minLongitude,
			@QueryParam("maxLongitude") double maxLongitude, 
			@QueryParam("lastRentDate") String lastRentDate,
			@QueryParam("lastRentId") int lastRentId, @QueryParam("pageSize") int pageSize,
			@Context HttpServletResponse response) {
		DateFormat dateFormat = new SimpleDateFormat(GeneralConstants.DATE_FORMAT);
		Date date = null;
		try {
			date = dateFormat.parse(lastRentDate);
		} catch (ParseException e) {
			response.setStatus(WebserviceResponseStatus.INVALID_DATA.getCode());
			
			return null;
		}

		List<Rent> rents = RentManager.getRentsNextPageByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude, date, lastRentId, pageSize);
		
		System.out.println("Total number of retrieved rents is: " + rents.size());
		return rents;

	}

}
