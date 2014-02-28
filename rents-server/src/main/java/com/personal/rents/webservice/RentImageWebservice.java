package com.personal.rents.webservice;

import java.io.InputStream;	

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.Gson;
import com.personal.rents.logic.RentImageManager;
import com.personal.rents.webservice.exception.UnauthorizedException;
import com.personal.rents.webservice.util.AuthorizationUtil;

@Path("rentimage")
public class RentImageWebservice {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadImage(@FormDataParam("image") InputStream imageInputStream,
			@FormDataParam("rentId") int rentId, @Context HttpServletRequest request) {
		if(!AuthorizationUtil.isAuthorized(request)) {
			throw new UnauthorizedException();
		}

		String imageURI = RentImageManager.uploadRentImage(imageInputStream, rentId);

		return new Gson().toJson(imageURI);
	}
}
