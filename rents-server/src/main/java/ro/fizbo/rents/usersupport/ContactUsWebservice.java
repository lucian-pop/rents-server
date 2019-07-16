package ro.fizbo.rents.usersupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;


@Path("usersupport")
public class ContactUsWebservice {
	private static Logger logger = Logger.getLogger(ContactUsWebservice.class);
	
	@Path("contactus")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean contactUs(@FormParam("userName") String userName, @FormParam("userEmail") String userEmail,
			@FormParam("userRequest") String userRequest) {
		logger.info("We have been contacted by a new user via the contact us form ");
		
		return true;
	}
}