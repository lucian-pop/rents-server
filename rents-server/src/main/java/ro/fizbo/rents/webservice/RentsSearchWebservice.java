package ro.fizbo.rents.webservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.logic.CurrencyManager;
import ro.fizbo.rents.logic.RentManager;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentSearch;
import ro.fizbo.rents.webservice.util.ContextUtil;

@Path("rents")
public class RentsSearchWebservice {

	@Path("search")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RentsCounter searchRents(RentSearch rentSearch, @Context HttpServletRequest request) {
		RentsCounter rentsCounter = RentManager.search(rentSearch);
		
		CurrencyManager.convertRentsListPrices(ContextUtil.getCurrency(request),
				rentsCounter.rents);
		
		return rentsCounter;
	}
	
	@Path("search/page")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rent> searchRentsNextPage(RentSearch rentSearch,
			@Context HttpServletRequest request) {
		List<Rent> rents = RentManager.searchNextPage(rentSearch);
		
		CurrencyManager.convertRentsListPrices(ContextUtil.getCurrency(request), rents);
		
		return rents;
	}
}
