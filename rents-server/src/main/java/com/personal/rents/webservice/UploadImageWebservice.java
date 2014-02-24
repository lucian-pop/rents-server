package com.personal.rents.webservice;

import java.io.IOException;
import java.io.InputStream;	

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.Gson;
import com.personal.rents.logic.ImageManager;
import com.personal.rents.model.Token;
import com.personal.rents.webservice.exception.OperationStoppedException;
import com.personal.rents.webservice.exception.UnauthorizedException;
import com.personal.rents.webservice.util.AuthorizationUtil;

@Path("uploadimage")
public class UploadImageWebservice {
	
	private static Logger logger = Logger.getLogger(SignupWebservice.class);

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadImage(@FormDataParam("image") InputStream inputStream, 
			@FormDataParam("filename") String filename, @FormDataParam("datetime") String datetime,
			@Context HttpServletRequest request) {
		Token token = AuthorizationUtil.authorize(request);
		if(!AuthorizationUtil.isAuthorized(token)) {
			throw new UnauthorizedException();
		}

		String imageURI = null;
		try {
			imageURI = ImageManager.saveImage(inputStream, filename,
					Integer.toString(token.getAccountId()), datetime);
		} catch (IOException ioe) {
			logger.error("An error occured while uploading image '" + filename + "'", ioe);

			throw new OperationStoppedException();
		}

		return new Gson().toJson(imageURI);
	}
}
