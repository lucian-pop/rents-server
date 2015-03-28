package ro.fizbo.rents.webservice;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.logic.ReportRentAbuseManager;
import ro.fizbo.rents.model.RentAbuse;

@Path("report")
public class ReportAbuseWebservice {
	
	private static Logger logger = Logger.getLogger(ReportAbuseWebservice.class);

	@Path("rent-abuse")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void reportRentAbuse(RentAbuse rentAbuse) {
		logger.info("A RENT ABUSE report was made for rent " + rentAbuse.getRent().getRentId() 
				+ ". Reporter has email " + rentAbuse.getRentAbuseEmail() + " and phone "
				+ rentAbuse.getRentAbusePhone());
		ReportRentAbuseManager.reportRentAbuse(rentAbuse);
	}
	
	@Path("rent-abuse/solved")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void resolveRentAbuse(@FormParam("rentAbuseTokenKey") String rentAbuseTokenKey,
			@FormParam("rentAbuseStatus") String rentAbuseStatus, 
			@FormParam("rentAbuseResolutionComment") String rentAbuseResolutionComment,
			@Context HttpServletResponse response) {
		logger.info("Rent abuse SOLVED with status " + rentAbuseStatus + " for token key " 
			+ rentAbuseTokenKey);
		ReportRentAbuseManager.resolveRentAbuse(rentAbuseTokenKey, rentAbuseStatus, 
				rentAbuseResolutionComment);
		try {
			response.sendRedirect(ApplicationManager.getAppURL() + "/report-validation-done.html");
		} catch (IOException e) {
			logger.error("Unable to redirect after solving abuse for token key " 
					+ rentAbuseTokenKey);
		}
	}
}
