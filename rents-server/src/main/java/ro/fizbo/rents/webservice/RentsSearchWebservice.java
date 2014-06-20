package ro.fizbo.rents.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.logic.RentManager;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentSearch;

@Path("rents")
public class RentsSearchWebservice {

	@Path("search")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentsCounter searchRents(RentSearch rentSearch) {
		RentsCounter rentsCounter = RentManager.search(rentSearch);
		
		return rentsCounter;
	}
	
	@Path("search/page")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rent> searchRentsNextPage(RentSearch rentSearch) {
		List<Rent> result = RentManager.searchNextPage(rentSearch);
		
		return result;
	}
}