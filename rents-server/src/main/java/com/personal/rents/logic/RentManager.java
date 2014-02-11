package com.personal.rents.logic;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.personal.rents.dao.AddressDAO;
import com.personal.rents.dao.RentDAO;
import com.personal.rents.dao.RentImageDAO;
import com.personal.rents.dto.RentsCounter;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentImage;
import com.personal.rents.model.RentSearch;

public class RentManager {

	private static Logger logger = Logger.getLogger(RentManager.class);

	public static Rent addRent(Rent rent) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession(ExecutorType.BATCH);
		try {
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			addressDAO.insertAddress(rent.getAddress());
			session.commit();
			
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentDAO.insertRent(rent);
			session.commit();
			
			// add rent images in batches.
			if((rent.getRentImageURIs() != null) && (rent.getRentImageURIs().size() > 0)) {
				RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
				RentImage rentImage = null;
				for(String imageURI : rent.getRentImageURIs()) {
					rentImage = new RentImage();
					rentImage.setRentId(rent.getRentId());
					rentImage.setRentImageURI(imageURI);
					
					rentImageDAO.insertRentImage(rentImage);
				}
				session.commit();
			}
		} catch (Exception e) {
			logger.error("An error occured while adding rent to database", e);
			session.rollback();
			
			return null;
		} finally {
			session.close();
		}

		return rent;
	}
	
	public static RentsCounter getRentsByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, int rentStatus,int pageSize) {
		RentsCounter rentsCounter = new RentsCounter();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentsCounter.counter = rentDAO.getNoOfRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, rentStatus);
			rentsCounter.rents = rentDAO.getRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, rentStatus, pageSize);
		} finally {
			session.close();
		}
		
		return rentsCounter;
	}

	public static List<Rent> getRentsNextPageByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, Date lastRentDate, int lastRentId,
			int rentStatus, int pageSize) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsNextPageByMapBoundaries(minLatitude, maxLatitude, minLongitude,
					maxLongitude, lastRentDate, lastRentId, rentStatus, pageSize);
		} finally {
			session.close();
		}
		
		return result;
	}
	
	public static RentsCounter search(RentSearch rentSearch) {
		RentsCounter rentsCounter = new RentsCounter();
		Rent lowRent = rentSearch.getLowRent();
		Rent highRent = rentSearch.getHighRent();
//		System.out.println("********Min latitude is:" + lowRent.getAddress().getAddressLatitude());
//		System.out.println("********Max latitude is:" + highRent.getAddress().getAddressLatitude());
//		System.out.println("********Min longitude is:" + lowRent.getAddress().getAddressLongitude());
//		System.out.println("********Max longitude is:" + highRent.getAddress().getAddressLongitude());
//		System.out.println("********Min price is:" + lowRent.getRentPrice());
//		System.out.println("********Max price is:" + highRent.getRentPrice());
//		System.out.println("********Min surface is:" + lowRent.getRentSurface());
//		System.out.println("********Max surface is:" + highRent.getRentSurface());
//		System.out.println("********Min rooms is: " + lowRent.getRentRooms());
//		System.out.println("********Max rooms is: " + highRent.getRentRooms());
//		System.out.println("********Min baths is: " + lowRent.getRentBaths());
//		System.out.println("********Max baths is: " + highRent.getRentBaths());
//		System.out.println("********Min party is: " + lowRent.getRentParty());
//		System.out.println("********Max party is: " + highRent.getRentParty());
//		System.out.println("********Min type is: " + lowRent.getRentType());
//		System.out.println("********Max type is: " + highRent.getRentType());
//		System.out.println("********Min architecture is: " + lowRent.getRentArchitecture());
//		System.out.println("********Max architecture is: " + highRent.getRentArchitecture());
//		System.out.println("********Min age is: " + lowRent.getRentAge());
//		System.out.println("********Max age is: " + highRent.getRentAge());
//		System.out.println("********Are pets alowed: " + lowRent.isRentPetsAllowed());
//		System.out.println("********Rents status is: " + lowRent.getRentStatus());
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentsCounter.counter = rentDAO.searchResultSize(
					lowRent.getAddress().getAddressLatitude(), 
					highRent.getAddress().getAddressLatitude(), 
					lowRent.getAddress().getAddressLongitude(),
					highRent.getAddress().getAddressLongitude(), lowRent.getRentPrice(),
					highRent.getRentPrice(), lowRent.getRentSurface(), highRent.getRentSurface(),
					lowRent.getRentRooms(), highRent.getRentRooms(), lowRent.getRentBaths(),
					highRent.getRentBaths(), lowRent.getRentParty(), highRent.getRentParty(),
					lowRent.getRentType(), highRent.getRentType(), lowRent.getRentArchitecture(),
					highRent.getRentArchitecture(), lowRent.getRentAge(), highRent.getRentAge(),
					lowRent.isRentPetsAllowed(), lowRent.getRentStatus());
			rentsCounter.rents = rentDAO.search(lowRent.getAddress().getAddressLatitude(), 
					highRent.getAddress().getAddressLatitude(), 
					lowRent.getAddress().getAddressLongitude(),
					highRent.getAddress().getAddressLongitude(), lowRent.getRentPrice(),
					highRent.getRentPrice(), lowRent.getRentSurface(), highRent.getRentSurface(),
					lowRent.getRentRooms(), highRent.getRentRooms(), lowRent.getRentBaths(),
					highRent.getRentBaths(), lowRent.getRentParty(), highRent.getRentParty(),
					lowRent.getRentType(), highRent.getRentType(), lowRent.getRentArchitecture(),
					highRent.getRentArchitecture(), lowRent.getRentAge(), highRent.getRentAge(),
					lowRent.isRentPetsAllowed(), lowRent.getRentStatus(), rentSearch.getPageSize());
		} finally {
			session.close();
		}

		return rentsCounter;
	}
	
	public static List<Rent> searchNextPage(RentSearch rentSearch) {
		Rent lowRent = rentSearch.getLowRent();
		Rent highRent = rentSearch.getHighRent();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.searchNextPage(lowRent.getAddress().getAddressLatitude(), 
					highRent.getAddress().getAddressLatitude(), 
					lowRent.getAddress().getAddressLongitude(),
					highRent.getAddress().getAddressLongitude(), lowRent.getRentPrice(),
					highRent.getRentPrice(), lowRent.getRentSurface(), highRent.getRentSurface(),
					lowRent.getRentRooms(), highRent.getRentRooms(), lowRent.getRentBaths(),
					highRent.getRentBaths(), lowRent.getRentParty(), highRent.getRentParty(),
					lowRent.getRentType(), highRent.getRentType(), lowRent.getRentArchitecture(),
					highRent.getRentArchitecture(), lowRent.getRentAge(), highRent.getRentAge(),
					lowRent.isRentPetsAllowed(), lowRent.getRentStatus(), highRent.getRentAddDate(),
					highRent.getRentId(), rentSearch.getPageSize());
		} finally {
			session.close();
		}

		return result;
	}
}
