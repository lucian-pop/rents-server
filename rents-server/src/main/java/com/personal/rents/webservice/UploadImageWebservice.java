package com.personal.rents.webservice;

import java.io.IOException;
import java.io.InputStream;	

import javax.servlet.http.HttpServletResponse;
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
import com.personal.rents.webservice.response.WebserviceResponseStatus;


@Path("uploadimage")
public class UploadImageWebservice {
	
	private static Logger logger = Logger.getLogger(SignupWebservice.class);

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadImage(@FormDataParam("image") InputStream inputStream, 
			@FormDataParam("filename") String filename, @FormDataParam("accountId") String accountId,
			@FormDataParam("datetime") String datetime, @Context HttpServletResponse response) {
		String imageURI = null;
		try {
			imageURI = ImageManager.saveImage(inputStream, filename, accountId, datetime);
		} catch (IOException ioe) {
			logger.error("An error occured while uploading image '" + filename + "'", ioe);
			response.setStatus(WebserviceResponseStatus.OPERATION_STOPPED.getCode());
			
			return null;
		}
		
		response.setStatus(WebserviceResponseStatus.OK.getCode());

		return new Gson().toJson(imageURI);
	}

}
