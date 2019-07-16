package ro.fizbo.rents.leads;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;


@Path("leads")
public class LeadCaptureWebservice {
	
	private static Logger logger = Logger.getLogger(LeadCaptureWebservice.class);
	
	@Path("capture")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean captureLead(@FormParam("leadEmail") String leadEmail, @FormParam("leadType") String leadType) {
		logger.info("We have a new lead from a " + leadType);
		
		return true;
	}

}
