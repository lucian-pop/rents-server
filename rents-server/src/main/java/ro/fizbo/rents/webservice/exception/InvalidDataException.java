package ro.fizbo.rents.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;

public class InvalidDataException extends WebApplicationException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidDataException() {
		super(Response.status(WebserviceResponseStatus.INVALID_DATA.getCode()).build());
	}

}
