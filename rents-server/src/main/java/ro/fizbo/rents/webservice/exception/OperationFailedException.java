package ro.fizbo.rents.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ro.fizbo.rents.webservice.response.WebserviceResponseStatus;

public class OperationFailedException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public OperationFailedException() {
		super(Response.status(WebserviceResponseStatus.OPERATION_FAILED.getCode()).build());
	}
}
