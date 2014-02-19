package com.personal.rents.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class UnauthorizedException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super(Response.status(Status.UNAUTHORIZED).build());
	}

}
