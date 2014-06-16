package ro.fizbo.rents.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;

public class AccountConflictException extends WebApplicationException {

	private static final long serialVersionUID = 1L;
	
	public AccountConflictException() {
		super(Response.status(WebserviceResponseStatus.ACCOUNT_CONFLICT.getCode()).build());
	}
}
