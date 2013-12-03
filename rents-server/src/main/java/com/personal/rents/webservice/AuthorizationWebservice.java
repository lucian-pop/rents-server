package com.personal.rents.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.personal.rents.webservice.response.WebserviceResponseStatus;
import com.personal.rents.webservice.util.AuthorizationUtil;

@Path("auth")
public class AuthorizationWebservice {

	@GET
	public Response isAuthorized(@QueryParam("accountId") int accountId, 
			@Context HttpServletRequest request) {
		
		if(AuthorizationUtil.isAuthorized(request, accountId)) {
			return Response.status(WebserviceResponseStatus.OK.getCode()).build();
		}
		
		return Response.status(WebserviceResponseStatus.UNAUTHORIZED.getCode()).build();
	}
}
