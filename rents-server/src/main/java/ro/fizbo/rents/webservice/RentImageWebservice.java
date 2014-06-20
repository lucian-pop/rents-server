package ro.fizbo.rents.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ro.fizbo.rents.logic.RentImageManager;
import ro.fizbo.rents.model.RentImage;
import ro.fizbo.rents.webservice.exception.UnauthorizedException;
import ro.fizbo.rents.webservice.util.AuthorizationUtil;
import ro.fizbo.rents.webservice.util.ContextConstants;

@Path("account/rentimage")
public class RentImageWebservice {

	@Path("upload")
	@POST
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.APPLICATION_JSON)
	public RentImage uploadImage(byte[] imageBytes, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}

		int rentId = request.getIntHeader(ContextConstants.RENT_ID);

		return RentImageManager.uploadRentImage(imageBytes, rentId);
	}
	
	@Path("replace")
	@PUT
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.APPLICATION_JSON)
	public RentImage replaceImage(byte[] imageBytes, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}
		
		int rentImageId = request.getIntHeader(ContextConstants.RENT_IMAGE_ID);
		String rentImageURI = request.getHeader(ContextConstants.RENT_IMAGE_URI);
		int rentId = request.getIntHeader(ContextConstants.RENT_ID);

		return RentImageManager.replaceRentImage(imageBytes, rentImageId, rentImageURI, rentId);
	}
	
	@Path("delete/{rentImageId}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int deleteImage(@PathParam("rentImageId") int rentImageId,
			@Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}
		
		return RentImageManager.deleteRentImageWithDiskCleanup(rentImageId);
	}
}
