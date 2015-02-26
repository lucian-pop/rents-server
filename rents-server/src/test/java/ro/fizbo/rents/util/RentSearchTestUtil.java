package ro.fizbo.rents.util;

import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.Address;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentArchitecture;
import ro.fizbo.rents.model.RentForm;
import ro.fizbo.rents.model.RentSearch;
import ro.fizbo.rents.model.RentStatus;
import ro.fizbo.rents.model.RentType;

public class RentSearchTestUtil {
	
	public static final int MIN_SURFACE = 20;
	
	public static final int MAX_SURFACE = 80;
	
	public static final int MIN_PRICE = 40;
	
	public static final int MAX_PRICE = 400;
	
	public static final short MIN_ROOMS = 1;
	
	public static final short MAX_ROOMS = 3;
	
	public static final short MIN_GUESTS = 1;
	
	public static final short MAX_GUESTS = 5;
	
	public static final short MIN_SINGLE_BEDS = 1;
	
	public static final short MAX_SINGLE_BEDS = 5;
	
	public static final short MIN_DOUBLE_BEDS = 1;
	
	public static final short MAX_DOUBLE_BEDS = 5;
	
	public static final short MIN_BATHS = 1;
	
	public static final short MAX_BATHS = 3;
	
	
	public static RentSearch getRentSearch() {
		Rent lowRent = new Rent();
		Address lowAddress = new Address();
		lowAddress.setAddressLatitude(TestUtil.MIN_LATITUDE);
		lowAddress.setAddressLongitude(TestUtil.MIN_LONGITUDE);
		lowRent.setAddress(lowAddress);
		lowRent.setRentSurface(MIN_SURFACE);
		lowRent.setRentPrice(MIN_PRICE);
		lowRent.setRentPetsAllowed(false);
		lowRent.setRentParkingPlace(false);
		lowRent.setRentSmokersAllowed(false);
		lowRent.setRentType(RentType.APARTMENT.getType());
		lowRent.setRentRooms(MIN_ROOMS);
		lowRent.setRentBaths(MIN_BATHS);
		lowRent.setRentArchitecture(RentArchitecture.DETACHED.getArchitecture());
		lowRent.setRentStatus(RentStatus.AVAILABLE.getStatus());
		lowRent.setRentForm(RentForm.RENT.getForm());
		
		Rent highRent = new Rent();
		Address highAddress = new Address();
		highAddress.setAddressLatitude(TestUtil.MAX_LATITUDE);
		highAddress.setAddressLongitude(TestUtil.MAX_LONGITUDE);
		highRent.setAddress(highAddress);
		highRent.setRentSurface(MAX_SURFACE);
		highRent.setRentPrice(MAX_PRICE);
		highRent.setRentPetsAllowed(true);
		highRent.setRentParkingPlace(true);
		highRent.setRentSmokersAllowed(true);
		highRent.setRentRooms(MAX_ROOMS);
		highRent.setRentBaths(MAX_BATHS);
		
		RentSearch rentSearch = new RentSearch();
		rentSearch.setPageSize(TestUtil.PAGE_SIZE);
		rentSearch.setLowRent(lowRent);
		rentSearch.setHighRent(highRent);
		rentSearch.setAppUrl(ApplicationManager.getAppURL());
		
		return rentSearch;
	}

}
