package ro.fizbo.rents.webservice.response;

/**
 * Status codes that are used to construct/verify web service responses
 */
public enum WebserviceResponseStatus {
	OK(200),
	OK_NO_CONTENT(204),
	UNAUTHORIZED(401),
	INVALID_DATA(420),
	ACCOUNT_CONFLICT(421),
	BAD_CREDENTIALS(422),
	VERSION_OUTDATED(423),
	OPERATION_FAILED(510),
	SERVER_ERROR(500);
	
	private int code;
	
	private WebserviceResponseStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}