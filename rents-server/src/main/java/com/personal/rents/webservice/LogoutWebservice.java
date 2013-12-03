//package com.personal.rents.webservice;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import com.personal.rents.webservice.authorization.WebserviceAuthorization;
//import com.personal.rents.webservice.response.WebserviceResponseStatus;
//
///**
// * Webservice that performs user logout.
// *
// */
//@Path("logout")
//public class LogoutWebservice {
//
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response logout(Integer accountId, @Context HttpServletRequest request) {
//		if(!WebserviceAuthorization.isAuthorized(request, accountId)) {
//			return Response.status(WebserviceResponseStatus.FORBIDDEN.getCode()).build();
//		}
//		
//		return  Response.status(WebserviceResponseStatus.OK.getCode()).build();
//	}
//}
