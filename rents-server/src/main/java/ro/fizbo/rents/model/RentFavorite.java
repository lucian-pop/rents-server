package ro.fizbo.rents.model;

import java.util.Date;

public class RentFavorite {
	
	private Integer accountId;
	
	private Integer rentId;
	
	private Date rentFavoriteAddDate;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getRentId() {
		return rentId;
	}

	public void setRentId(Integer rentId) {
		this.rentId = rentId;
	}

	public Date getRentFavoriteAddDate() {
		return rentFavoriteAddDate;
	}

	public void setRentFavoriteAddDate(Date rentFavoriteAddDate) {
		this.rentFavoriteAddDate = rentFavoriteAddDate;
	}
}
