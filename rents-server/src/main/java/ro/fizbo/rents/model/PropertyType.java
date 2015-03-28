package ro.fizbo.rents.model;

public enum PropertyType {
	
	APARTMENT((byte) 0),
	HOUSE((byte) 1),
	OFFICE((byte) 2),
	HOSTEL((byte) 3),
	BEDANDBREAKFAST((byte) 4);

	private byte type;
	
	private PropertyType(byte type) {
		this.type = type;
	}
	
	public byte getType() {
		return type;
	}
}
