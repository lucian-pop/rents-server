package ro.fizbo.rents.model;

public enum RentForm {
	RENT((byte) 0),
	ACCOMMODATION((byte) 1);
	
	private byte form;
	
	private RentForm(byte form) {
		this.form = form;
	}
	
	public byte getForm() {
		return form;
	}
}