package com.personal.rents.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.personal.rents.logic.RentManager;
import com.personal.rents.model.Rent;

@Path("rent")
public class GetRentWebService {

	@Path("detailed")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Rent getDetailedRent(@QueryParam("rentId") int rentId) {
		Rent rent = RentManager.getDetailedRent(rentId);

		return rent;
	}
}
