package com.personal.rents.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.personal.rents.logic.RentManager;
import com.personal.rents.model.Rent;

@Path("addrent")
public class AddRentWebservice {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Rent addRent(Rent rent) {
		rent = RentManager.addRent(rent);
		
		return rent;
	}
}
