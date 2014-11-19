package ro.fizbo.rents.model;

public enum RentForm {
	NORMAL((byte) 0),
	HOTELIER((byte) 1);
	
	private byte form;
	
	private RentForm(byte form) {
		this.form = form;
	}
	
	public byte getForm() {
		return form;
	}
}