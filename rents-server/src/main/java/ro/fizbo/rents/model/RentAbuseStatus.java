package ro.fizbo.rents.model;

public enum RentAbuseStatus {

	PENDING((byte) 0),
	RESOLVED_OK((byte) 1),
	RESOLVED_NOT_OK((byte) 2),
	RESOLVED_AGENCY((byte) 3);
	
	private byte status;
	
	private RentAbuseStatus(byte status) {
		this.status = status;
	}
	
	public byte getStatus() {
		return status;
	}
}
