package com.personal.rents.model;

import java.util.Date;
import java.util.List;

public class Rent {
	
	private Integer rentId;
	
	private Integer accountId;
	
	private Address address;
	
	private Integer rentPrice;
	
	private Integer rentSurface;
	
	private Short rentRooms;
	
	private Short rentBaths;
	
	private Byte rentParty;
	
	private Byte rentType;
	
	private Byte rentArchitecture;
	
	private Short rentAge;
	
	private String rentDescription;
	
	private Boolean rentPetsAllowed;
	
	private String rentPhone;
	
	private Date rentAddDate;
	
	private Byte rentStatus;
	
	private List<RentImage> rentImages;
	
	public Rent() {
	}
	
	public Rent(Integer rentId) {
		this.rentId = rentId;
	}

	public Integer getRentId() {
		return rentId;
	}

	public void setRentId(Integer rentId) {
		this.rentId = rentId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Address getAddress() {
		return address;
	}
	
	public Integer getAddressId() {
		return address.getAddressId();
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Integer getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(Integer rentPrice) {
		this.rentPrice = rentPrice;
	}

	public Integer getRentSurface() {
		return rentSurface;
	}

	public void setRentSurface(Integer rentSurface) {
		this.rentSurface = rentSurface;
	}

	public Short getRentRooms() {
		return rentRooms;
	}

	public void setRentRooms(Short rentRooms) {
		this.rentRooms = rentRooms;
	}

	public Short getRentBaths() {
		return rentBaths;
	}

	public void setRentBaths(Short rentBaths) {
		this.rentBaths = rentBaths;
	}

	public Byte getRentParty() {
		return rentParty;
	}

	public void setRentParty(Byte rentParty) {
		this.rentParty = rentParty;
	}

	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}

	public Byte getRentArchitecture() {
		return rentArchitecture;
	}

	public void setRentArchitecture(Byte rentArchitecture) {
		this.rentArchitecture = rentArchitecture;
	}

	public Short getRentAge() {
		return rentAge;
	}

	public void setRentAge(Short rentAge) {
		this.rentAge = rentAge;
	}

	public String getRentDescription() {
		return rentDescription;
	}

	public void setRentDescription(String rentDescription) {
		this.rentDescription = rentDescription;
	}

	public Boolean isRentPetsAllowed() {
		return rentPetsAllowed;
	}

	public void setRentPetsAllowed(Boolean rentPetsAllowed) {
		this.rentPetsAllowed = rentPetsAllowed;
	}

	public String getRentPhone() {
		return rentPhone;
	}

	public void setRentPhone(String rentPhone) {
		this.rentPhone = rentPhone;
	}

	public Date getRentAddDate() {
		return rentAddDate;
	}

	public void setRentAddDate(Date rentAddDate) {
		this.rentAddDate = rentAddDate;
	}

	public Byte getRentStatus() {
		return rentStatus;
	}

	public void setRentStatus(Byte rentStatus) {
		this.rentStatus = rentStatus;
	}

	public List<RentImage> getRentImages() {
		return rentImages;
	}

	public void setRentImages(List<RentImage> rentImages) {
		this.rentImages = rentImages;
	}
	
}
