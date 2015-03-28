package ro.fizbo.rents.model;

import java.util.Date;

public class RentAbuse {
	
	private Integer rentAbuseId;
	
	private String rentAbuseTokenKey;
	
	private String rentAbuseEmail;
	
	private String rentAbusePhone;
	
	private Date rentAbuseDate;
	
	private Date rentAbuseResolutionDate;
	
	private String rentAbuseResolutionComment;
	
	private String rentAbuseDescription;
	
	private Byte rentAbuseStatus;
	
	private Rent rent;

	public Integer getRentAbuseId() {
		return rentAbuseId;
	}

	public void setRentAbuseId(Integer rentAbuseId) {
		this.rentAbuseId = rentAbuseId;
	}

	public String getRentAbuseTokenKey() {
		return rentAbuseTokenKey;
	}

	public void setRentAbuseTokenKey(String rentAbuseTokenKey) {
		this.rentAbuseTokenKey = rentAbuseTokenKey;
	}

	public String getRentAbuseEmail() {
		return rentAbuseEmail;
	}

	public void setRentAbuseEmail(String rentAbuseEmail) {
		this.rentAbuseEmail = rentAbuseEmail;
	}

	public String getRentAbusePhone() {
		return rentAbusePhone;
	}

	public void setRentAbusePhone(String rentAbusePhone) {
		this.rentAbusePhone = rentAbusePhone;
	}

	public Date getRentAbuseDate() {
		return rentAbuseDate;
	}

	public void setRentAbuseDate(Date rentAbuseDate) {
		this.rentAbuseDate = rentAbuseDate;
	}

	public Date getRentAbuseResolutionDate() {
		return rentAbuseResolutionDate;
	}

	public void setRentAbuseResolutionDate(Date rentAbuseResolutionDate) {
		this.rentAbuseResolutionDate = rentAbuseResolutionDate;
	}

	public String getRentAbuseResolutionComment() {
		return rentAbuseResolutionComment;
	}

	public void setRentAbuseResolutionComment(String rentAbuseResolutionComment) {
		this.rentAbuseResolutionComment = rentAbuseResolutionComment;
	}

	public String getRentAbuseDescription() {
		return rentAbuseDescription;
	}

	public void setRentAbuseDescription(String rentAbuseDescription) {
		this.rentAbuseDescription = rentAbuseDescription;
	}

	public Byte getRentAbuseStatus() {
		return rentAbuseStatus;
	}

	public void setRentAbuseStatus(Byte rentAbuseStatus) {
		this.rentAbuseStatus = rentAbuseStatus;
	}

	public Rent getRent() {
		return rent;
	}

	public void setRent(Rent rent) {
		this.rent = rent;
	}
}
