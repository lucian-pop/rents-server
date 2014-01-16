package com.personal.rents.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.personal.rents.dto.RentsCounter;
import com.personal.rents.logic.RentManager;

@Path("rents")
public class GetRentsWebservice {

	@Path("light")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentsCounter getRentsByMapBoundaries(@QueryParam("minLatitude") double minLatitude,
			@QueryParam("maxLatitude") double maxLatitude, 
			@QueryParam("minLongitude") double minLongitude,
			@QueryParam("maxLongitude") double maxLongitude) {
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		RentsCounter rentsCounter = RentManager.getRentsByMapBoundaries(minLatitude, maxLatitude,
				minLongitude, maxLongitude);
		
		return rentsCounter;
	}

}
