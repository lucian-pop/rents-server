package com.personal.rents.model.view;

import java.util.Date;

import com.personal.rents.model.Rent;

public class RentFavoriteView {
	
	private Rent rent;
	
	private Date rentFavoriteAddDate;

	public Rent getRent() {
		return rent;
	}

	public void setRent(Rent rent) {
		this.rent = rent;
	}

	public Date getRentFavoriteAddDate() {
		return rentFavoriteAddDate;
	}

	public void setRentFavoriteAddDate(Date rentFavoriteAddDate) {
		this.rentFavoriteAddDate = rentFavoriteAddDate;
	}
}
