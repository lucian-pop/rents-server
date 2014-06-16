package ro.fizbo.rents.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;

public class UnauthorizedException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super(Response.status(WebserviceResponseStatus.UNAUTHORIZED.getCode()).build());
	}

}
