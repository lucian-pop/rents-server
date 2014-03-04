package com.personal.rents.webservice;

import java.io.InputStream;	

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

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.personal.rents.logic.RentImageManager;
import com.personal.rents.model.RentImage;
import com.personal.rents.webservice.exception.UnauthorizedException;
import com.personal.rents.webservice.util.AuthorizationUtil;

@Path("rentimage")
public class RentImageWebservice {

	@Path("upload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public RentImage uploadImage(@FormDataParam("imageData") InputStream imageInputStream,
			@FormDataParam("rentId") int rentId, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}

		return RentImageManager.uploadRentImage(imageInputStream, rentId);
	}
	
	@Path("replace")
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public RentImage replaceImage(@FormDataParam("imageData") InputStream imageInputStream,
			@FormDataParam("rentImageId") int rentImageId, 
			@FormDataParam("rentImageURI") String rentImageURI, @FormDataParam("rentId") int rentId,
			@Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}
		
		return RentImageManager.replaceRentImage(imageInputStream, rentImageId, rentImageURI, rentId);
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
