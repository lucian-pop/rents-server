package ro.fizbo.rents.model;

import java.util.Date;
import java.util.List;

public class Rent {
	
	private Integer rentId;
	
	private Integer accountId;
	
	private Integer rentPrice;
	
	private String rentCurrency;
	
	private Integer rentSurface;
	
	private Short rentRooms;
	
	private Short rentGuests;
	
	private Short rentSingleBeds;
	
	private Short rentDoubleBeds;
	
	private Short rentBaths;
	
	private Byte rentParty;
	
	private Byte rentType;
	
	private Byte rentArchitecture;
	
	private Short rentAge;
	
	private String rentDescription;
	
	private Boolean rentPetsAllowed;
	
	private Boolean rentParkingPlace;
	
	private Boolean rentSmokersAllowed;
	
	private String rentPhone;
	
	private Date rentAddDate;
	
	private Byte rentStatus;
	
	private Byte rentForm;
	
	private List<RentImage> rentImages;
	
	private Integer rentViewsNo;

	private Address address;
	
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
	
	public String getRentCurrency() {
		return rentCurrency;
	}
	
	public void setRentCurrency(String rentCurrency) {
		this.rentCurrency = rentCurrency;
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

	public Short getRentGuests() {
		return rentGuests;
	}

	public void setRentGuests(Short rentGuests) {
		this.rentGuests = rentGuests;
	}

	public Short getRentSingleBeds() {
		return rentSingleBeds;
	}

	public void setRentSingleBeds(Short rentSingleBeds) {
		this.rentSingleBeds = rentSingleBeds;
	}

	public Short getRentDoubleBeds() {
		return rentDoubleBeds;
	}

	public void setRentDoubleBeds(Short rentDoubleBeds) {
		this.rentDoubleBeds = rentDoubleBeds;
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

	public Boolean isRentParkingPlace() {
		return rentParkingPlace;
	}

	public void setRentParkingPlace(Boolean rentParkingPlace) {
		this.rentParkingPlace = rentParkingPlace;
	}

	public Boolean isRentSmokersAllowed() {
		return rentSmokersAllowed;
	}

	public void setRentSmokersAllowed(Boolean rentSmokersAllowed) {
		this.rentSmokersAllowed = rentSmokersAllowed;
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

	public Byte getRentForm() {
		return rentForm;
	}

	public void setRentForm(Byte rentForm) {
		this.rentForm = rentForm;
	}

	public List<RentImage> getRentImages() {
		return rentImages;
	}

	public void setRentImages(List<RentImage> rentImages) {
		this.rentImages = rentImages;
	}

	public Integer getRentViewsNo() {
		return rentViewsNo;
	}

	public void setRentViewsNo(Integer rentViewsNo) {
		this.rentViewsNo = rentViewsNo;
	}
}
