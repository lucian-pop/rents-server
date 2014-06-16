package ro.fizbo.rents.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;

public class AuthenticationException extends WebApplicationException {

	private static final long serialVersionUID = 1L;
	
	public AuthenticationException() {
		super(Response.status(WebserviceResponseStatus.BAD_CREDENTIALS.getCode()).build());
	}

}
