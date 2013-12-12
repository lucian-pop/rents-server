package com.personal.rents.model;

import java.util.Date;
import java.util.List;

public class Rent {
	
	private Integer id;
	
	private Integer accountId;
	
	private Address address;
	
	private Integer price;
	
	private Integer surface;
	
	private Short rooms;
	
	private Short baths;
	
	private Byte party;
	
	private Byte rentType;
	
	private Byte architecture;
	
	private Short age;
	
	private String description;
	
	private boolean petsAllowed;
	
	private String phone;
	
	private Date creationDate;
	
	private Byte rentStatus;
	
	private List<String> imageURIs;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getAddressId() {
		return address.getId();
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getSurface() {
		return surface;
	}

	public void setSurface(Integer surface) {
		this.surface = surface;
	}

	public Short getRooms() {
		return rooms;
	}

	public void setRooms(Short rooms) {
		this.rooms = rooms;
	}

	public Short getBaths() {
		return baths;
	}

	public void setBaths(Short baths) {
		this.baths = baths;
	}

	public Byte getParty() {
		return party;
	}

	public void setParty(Byte party) {
		this.party = party;
	}

	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}

	public Byte getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Byte architecture) {
		this.architecture = architecture;
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPetsAllowed() {
		return petsAllowed;
	}

	public void setPetsAllowed(boolean petsAllowed) {
		this.petsAllowed = petsAllowed;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Byte getRentStatus() {
		return rentStatus;
	}

	public void setRentStatus(Byte rentStatus) {
		this.rentStatus = rentStatus;
	}

	public List<String> getImageURIs() {
		return imageURIs;
	}

	public void setImageURIs(List<String> imageURIs) {
		this.imageURIs = imageURIs;
	}

}
