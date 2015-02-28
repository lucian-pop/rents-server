package ro.fizbo.rents.model;

public enum RentArchitecture {
	DETACHED((byte) 0),
	SEMIDETACHED((byte) 1),
	UNDETACHED((byte) 2);
	
	private byte architecture;
	
	private RentArchitecture(byte architecture) {
		this.architecture = architecture;
	}
	
	public byte getArchitecture() {
		return architecture;
	}
}
