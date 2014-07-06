package ro.fizbo.rents.logic;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import ro.fizbo.rents.dao.AddressDAO;
import ro.fizbo.rents.dao.RentDAO;
import ro.fizbo.rents.dao.RentFavoriteDAO;
import ro.fizbo.rents.dao.param.RentsStatus;
import ro.fizbo.rents.dto.RentFavoriteViewsCounter;
import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.RentSearch;
import ro.fizbo.rents.model.view.RentFavoriteView;
import ro.fizbo.rents.webservice.exception.OperationFailedException;

public class RentManager {

	private static Logger logger = Logger.getLogger(RentManager.class);

	public static Rent addRent(Rent rent) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			AddressDAO addressDAO = session.getMapper(AddressDAO.class);
			addressDAO.insertAddress(rent.getAddress());
			session.commit();
			
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentDAO.insertRent(rent);
			session.commit();
		} catch (RuntimeException runtimeException) {
			logger.error("An error occured while adding rent to database", runtimeException);
			session.rollback();
			
			throw new OperationFailedException();
		} finally {
			session.close();
		}
		
		// do not expose accountId.
		rent.setAccountId(null);

		return rent;
	}
	
	public static int updateRent(Rent rent) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		int updateRentCount = -1;
		int updateAddressCount = -1;
		try {
			updateAddressCount = session.update("AddressMapper.updateAddress", rent.getAddress());
			updateRentCount = session.update("RentMapper.updateRent", rent);

			session.commit();
		} catch (RuntimeException runtimeException) {
			logger.error("Unable to update rent with id  " + rent.getRentId(), runtimeException);
			session.rollback();

			throw new OperationFailedException();
		} finally {
			session.close();
		}
		
		if(updateRentCount != 1 && updateAddressCount != 1) {
			logger.error("An error occured while updating rent '" + rent.getRentId());
			
			throw new OperationFailedException();
		}
		
		return 1;
	}
	
	public static Rent getDetailedRent(int rentId) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		Rent rent = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rent = rentDAO.getDetailedRent(rentId, ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		// do not expose accountId.
		rent.setAccountId(null);

		return rent;
	}
	
	public static RentsCounter getRentsByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, byte rentStatus, int pageSize) {
		RentsCounter rentsCounter = new RentsCounter();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentsCounter.counter = rentDAO.getNoOfRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, rentStatus);
			rentsCounter.rents = rentDAO.getRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, rentStatus, pageSize, 
					ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		return rentsCounter;
	}

	public static List<Rent> getRentsNextPageByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, Date lastRentDate, int lastRentId,
			byte rentStatus, int pageSize) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsNextPageByMapBoundaries(minLatitude, maxLatitude, minLongitude,
					maxLongitude, lastRentDate, lastRentId, rentStatus, pageSize,
					ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		return result;
	}
	
	public static RentsCounter search(RentSearch rentSearch) {
		RentsCounter rentsCounter = new RentsCounter();
		Rent lowRent = rentSearch.getLowRent();
		Rent highRent = rentSearch.getHighRent();
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
					lowRent.isRentPetsAllowed(), highRent.isRentPetsAllowed(), lowRent.getRentStatus());
			rentsCounter.rents = rentDAO.search(lowRent.getAddress().getAddressLatitude(), 
					highRent.getAddress().getAddressLatitude(), 
					lowRent.getAddress().getAddressLongitude(),
					highRent.getAddress().getAddressLongitude(), lowRent.getRentPrice(),
					highRent.getRentPrice(), lowRent.getRentSurface(), highRent.getRentSurface(),
					lowRent.getRentRooms(), highRent.getRentRooms(), lowRent.getRentBaths(),
					highRent.getRentBaths(), lowRent.getRentParty(), highRent.getRentParty(),
					lowRent.getRentType(), highRent.getRentType(), lowRent.getRentArchitecture(),
					highRent.getRentArchitecture(), lowRent.getRentAge(), highRent.getRentAge(),
					lowRent.isRentPetsAllowed(), highRent.isRentPetsAllowed(),
					lowRent.getRentStatus(), rentSearch.getPageSize(), ApplicationManager.getAppURL());
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
					lowRent.isRentPetsAllowed(), highRent.isRentPetsAllowed(), 
					lowRent.getRentStatus(), highRent.getRentAddDate(), highRent.getRentId(),
					rentSearch.getPageSize(), ApplicationManager.getAppURL());
		} finally {
			session.close();
		}

		return result;
	}
	
	public static RentsCounter getUserAddedRents(int accountId, byte rentStatus, int pageSize) {
		RentsCounter rentsCounter = new RentsCounter();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentsCounter.counter = rentDAO.getNoOfUserAddedRents(accountId, rentStatus);
			rentsCounter.rents = rentDAO.getUserAddedRents(accountId, rentStatus, pageSize,
					ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		return rentsCounter;
	}
	
	public static List<Rent> getUserAddedRentsNextPage(int accountId, byte rentStatus,
			Date lastRentDate, int lastRentId, int pageSize) {
		List<Rent> result = null;
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getUserAddedRentsNextPage(accountId, rentStatus, lastRentDate,
					lastRentId, pageSize, ApplicationManager.getAppURL());
		} finally {
			session.close();
		}

		return result;
	}
	
	public static int updateRentsStatus(List<Integer> rentIds, int rentStatus) {
		RentsStatus rentsStatus = new RentsStatus();
		rentsStatus.status = rentStatus;
		rentsStatus.rentIds = rentIds;

		int updatesCount = -1;
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			updatesCount = session.update("RentMapper.updateRentsStatus", rentsStatus);
			session.commit();
		} catch (RuntimeException runtimeException) {
			logger.error("Failed to update rents status", runtimeException);
			session.rollback();
			
			throw new OperationFailedException();
		} finally {
			session.close();
		}

		return updatesCount;
	}
	
	public static RentFavoriteViewsCounter getUserFavoriteRents(int accountId, int pageSize) {
		RentFavoriteViewsCounter rentFavoriteViewsCounter = new RentFavoriteViewsCounter();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentFavoriteDAO rentFavoriteDAO = session.getMapper(RentFavoriteDAO.class);
			rentFavoriteViewsCounter.counter = rentFavoriteDAO.getNoOfUserFavoriteRents(accountId);
			
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentFavoriteViewsCounter.rentFavoriteViews = rentDAO.getUserFavoriteRents(accountId,
					pageSize, ApplicationManager.getAppURL());
		} finally {
			session.close();
		}

		return rentFavoriteViewsCounter;
	}
	
	public static List<RentFavoriteView> getUserFavoriteRentsNextPage(int accountId,
			Date lastRentFavoriteDate, int pageSize) {
		List<RentFavoriteView> result = null;
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getUserFavoriteRentsNextPage(accountId, lastRentFavoriteDate,
					pageSize, ApplicationManager.getAppURL());
		} finally {
			session.close();
		}

		return result;
	}
}
