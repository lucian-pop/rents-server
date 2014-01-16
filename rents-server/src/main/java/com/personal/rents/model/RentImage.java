package com.personal.rents.model;

public class RentImage {

	private Integer rentImageId;
	
	private Integer rentId;
	
	private String rentImageURI;

	public Integer getRentImageId() {
		return rentImageId;
	}

	public void setRentImageId(Integer rentImageId) {
		this.rentImageId = rentImageId;
	}

	public Integer getRentId() {
		return rentId;
	}

	public void setRentId(Integer rentId) {
		this.rentId = rentId;
	}

	public String getRentImageURI() {
		return rentImageURI;
	}

	public void setRentImageURI(String rentImageURI) {
		this.rentImageURI = rentImageURI;
	}

}
