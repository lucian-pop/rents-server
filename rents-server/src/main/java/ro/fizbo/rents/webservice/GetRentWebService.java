package ro.fizbo.rents.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ro.fizbo.rents.logic.CurrencyManager;
import ro.fizbo.rents.logic.RentManager;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.webservice.util.ContextUtil;

@Path("rent")
public class GetRentWebService {

	@Path("detailed")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Rent getDetailedRent(@QueryParam("rentId") int rentId,
			@Context HttpServletRequest request) {
		Rent rent = RentManager.getDetailedRent(rentId);
		CurrencyManager.convertRentPrice(ContextUtil.getCurrency(request), rent);

		return rent;
	}
}
