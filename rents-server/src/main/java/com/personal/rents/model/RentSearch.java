package com.personal.rents.model;

public class RentSearch {
	
	private Rent lowRent;
	
	private Rent highRent;
	
	private Integer pageSize;
	
	private Byte sortBy;

	public Rent getLowRent() {
		return lowRent;
	}

	public void setLowRent(Rent lowRent) {
		this.lowRent = lowRent;
	}

	public Rent getHighRent() {
		return highRent;
	}

	public void setHighRent(Rent highRent) {
		this.highRent = highRent;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Byte getSortBy() {
		return sortBy;
	}

	public void setSortBy(Byte sortBy) {
		this.sortBy = sortBy;
	}
}
