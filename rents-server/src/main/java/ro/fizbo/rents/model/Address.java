package ro.fizbo.rents.model;

public class Address {

	private Integer addressId;
	
	private String addressStreetNo;
	
	private String addressStreetName;
	
	private String addressNeighbourhood;
	
	private String addressSublocality;
	
	private String addressLocality;
	
	private String addressAdmAreaL1;
	
	private String addressCountry;
	
	private Double addressLatitude;
	
	private Double addressLongitude;
	
	private String addressBuilding;
	
	private String addressStaircase;
	
	private Short addressFloor;
	
	private String addressAp;

	public Address() {
	}
	
	public Address(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getAddressStreetNo() {
		return addressStreetNo;
	}

	public void setAddressStreetNo(String addressStreetNo) {
		this.addressStreetNo = addressStreetNo;
	}

	public String getAddressStreetName() {
		return addressStreetName;
	}

	public void setAddressStreetName(String addressStreetName) {
		this.addressStreetName = addressStreetName;
	}

	public String getAddressNeighbourhood() {
		return addressNeighbourhood;
	}

	public void setAddressNeighbourhood(String addressNeighbourhood) {
		this.addressNeighbourhood = addressNeighbourhood;
	}

	public String getAddressSublocality() {
		return addressSublocality;
	}

	public void setAddressSublocality(String addressSublocality) {
		this.addressSublocality = addressSublocality;
	}

	public String getAddressLocality() {
		return addressLocality;
	}

	public void setAddressLocality(String addressLocality) {
		this.addressLocality = addressLocality;
	}

	public String getAddressAdmAreaL1() {
		return addressAdmAreaL1;
	}

	public void setAddressAdmAreaL1(String addressAdmAreaL1) {
		this.addressAdmAreaL1 = addressAdmAreaL1;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public Double getAddressLatitude() {
		return addressLatitude;
	}

	public void setAddressLatitude(Double addressLatitude) {
		this.addressLatitude = addressLatitude;
	}

	public Double getAddressLongitude() {
		return addressLongitude;
	}

	public void setAddressLongitude(Double addressLongitude) {
		this.addressLongitude = addressLongitude;
	}

	public String getAddressBuilding() {
		return addressBuilding;
	}

	public void setAddressBuilding(String addressBuilding) {
		this.addressBuilding = addressBuilding;
	}

	public String getAddressStaircase() {
		return addressStaircase;
	}

	public void setAddressStaircase(String addressStaircase) {
		this.addressStaircase = addressStaircase;
	}

	public Short getAddressFloor() {
		return addressFloor;
	}

	public void setAddressFloor(Short addressFloor) {
		this.addressFloor = addressFloor;
	}

	public String getAddressAp() {
		return addressAp;
	}

	public void setAddressAp(String addressAp) {
		this.addressAp = addressAp;
	}
	
}
