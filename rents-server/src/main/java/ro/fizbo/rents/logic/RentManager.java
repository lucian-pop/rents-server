package ro.fizbo.rents.logic;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import ro.fizbo.rents.dao.AddressDAO;
import ro.fizbo.rents.dao.RentDAO;
import ro.fizbo.rents.dao.RentFavoriteDAO;
import ro.fizbo.rents.dao.RentStatisticsDAO;
import ro.fizbo.rents.dao.param.RentsStatus;
import ro.fizbo.rents.dto.RentFavoriteViewsCounter;
import ro.fizbo.rents.dto.RentsCounter;
import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.PropertyType;
import ro.fizbo.rents.model.Rent;
import ro.fizbo.rents.model.Currency;
import ro.fizbo.rents.model.RentArchitecture;
import ro.fizbo.rents.model.RentForm;
import ro.fizbo.rents.model.RentParty;
import ro.fizbo.rents.model.RentSearch;
import ro.fizbo.rents.model.view.RentFavoriteView;
import ro.fizbo.rents.webservice.exception.OperationFailedException;

public class RentManager {

	private static Logger logger = Logger.getLogger(RentManager.class);

	public static Rent addRent(Rent rent) {
		addSupportForOldClients(rent);
		addSupportForOldClientsAddSpecific(rent);
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
		addSupportForOldClients(rent);
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
	
	/** Support clients before adding accommodations.*/
	private static void addSupportForOldClients(Rent rent) {
		if(rent.getRentForm() == null) {
			rent.setRentForm(RentForm.RENT.getForm());
		}
		if(rent.getRentCurrency() == null) {
			rent.setRentCurrency(Currency.EUR.toString());
		}
	}
	
	/** Support clients after adding accommodations.*/
	private static void addSupportForOldClientsAddSpecific(Rent rent) {
		if(rent.isRentParkingPlace() == null) {
			rent.setRentParkingPlace(false);
		}
		if(rent.isRentSmokersAllowed() == null) {
			rent.setRentSmokersAllowed(false);
		}
	}

	/** Support clients after adding accommodations.*/
	private static void addSupportForOldClients(RentSearch rentSearch) {
		Rent lowRent = rentSearch.getLowRent();
		Rent highRent = rentSearch.getHighRent();
		if(lowRent.isRentParkingPlace() == null && highRent.isRentParkingPlace() == null) {
			lowRent.setRentParkingPlace(false);
			highRent.setRentParkingPlace(true);
		}
		if(lowRent.isRentSmokersAllowed() == null && highRent.isRentSmokersAllowed() == null) {
			lowRent.setRentSmokersAllowed(false);
			highRent.setRentSmokersAllowed(true);
		}
		if(lowRent.getRentBaths() != null && highRent.getRentBaths() != null 
				&& lowRent.getRentBaths() == 1 
				&& highRent.getRentBaths().shortValue() == Short.MAX_VALUE) {
			lowRent.setRentBaths(null);
			highRent.setRentBaths(null);
		}
		if(lowRent.getRentSurface() != null && highRent.getRentSurface() != null 
				&& lowRent.getRentSurface() == 0
				&& highRent.getRentSurface().intValue() == Integer.MAX_VALUE) {
			lowRent.setRentSurface(null);
			highRent.setRentSurface(null);
		}
		if(lowRent.getRentArchitecture() != null && highRent.getRentArchitecture() != null
				&& lowRent.getRentArchitecture() == RentArchitecture.DETACHED.getArchitecture()
				&& highRent.getRentArchitecture() == RentArchitecture.UNDETACHED.getArchitecture()) {
			lowRent.setRentArchitecture(null);
		}
		if(lowRent.getRentParty() != null && highRent.getRentParty() != null 
				&& lowRent.getRentParty() == RentParty.INDIVIDUAL.getParty()
				&& highRent.getRentParty() == RentParty.REALTOR.getParty()){
			lowRent.setRentParty(null);
		}
	}
	
	public static Rent getDetailedRent(int rentId) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		Rent rent = null;
		try {
			session.getMapper(RentStatisticsDAO.class).updateInsert(rentId);
			session.commit();
			rent = session.getMapper(RentDAO.class).getDetailedRent(rentId,
					ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		// do not expose accountId.
		rent.setAccountId(null);

		return rent;
	}
	
	public static RentsCounter getRentsByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, byte rentStatus, byte rentForm, int pageSize) {
		RentsCounter rentsCounter = new RentsCounter();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentsCounter.counter = rentDAO.getNoOfRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, rentStatus, rentForm, Byte.MAX_VALUE);
			rentsCounter.rents = rentDAO.getRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, rentStatus, rentForm,  Byte.MAX_VALUE, pageSize, 
					ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		return rentsCounter;
	}

	public static List<Rent> getRentsNextPageByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, Date lastRentDate, int lastRentId,
			byte rentStatus, byte rentForm, int pageSize) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsNextPageByMapBoundaries(minLatitude, maxLatitude, minLongitude,
					maxLongitude, lastRentDate, lastRentId, rentStatus, rentForm, Byte.MAX_VALUE,
					pageSize, ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		return result;
	}
	
	public static RentsCounter search(RentSearch rentSearch, String currency) {
		RentsCounter rentsCounter = new RentsCounter();
		Rent lowRent = rentSearch.getLowRent();		
		addSupportForOldClients(lowRent);
		addSupportForOldClients(rentSearch);
		convertPricesToBaseCurrency(rentSearch, currency);

		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			rentsCounter.counter = session.selectOne("RentMapper.searchCount", rentSearch);
			rentsCounter.rents = session.selectList("RentMapper.search", rentSearch);
		} finally {
			session.close();
		}

		return rentsCounter;
	}
	
	public static List<Rent> searchNextPage(RentSearch rentSearch, String currency) {
		Rent lowRent = rentSearch.getLowRent();
		addSupportForOldClients(lowRent);
		addSupportForOldClients(rentSearch);
		convertPricesToBaseCurrency(rentSearch, currency);

		List<Rent> result = null;
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			result = session.selectList("RentMapper.searchNextPage", rentSearch);
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
	
	/** Compatibility method for older version that do not support accommodation types.*/
	public static RentsCounter getOldRentsByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, byte rentStatus, byte rentForm, int pageSize) {
		RentsCounter rentsCounter = new RentsCounter();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			rentsCounter.counter = rentDAO.getNoOfRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, rentStatus, rentForm, PropertyType.OFFICE.getType());
			rentsCounter.rents = rentDAO.getRentsByMapBoundaries(minLatitude, maxLatitude,
					minLongitude, maxLongitude, rentStatus, rentForm,  PropertyType.OFFICE.getType(),
					pageSize, ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		return rentsCounter;
	}

	/** Compatibility method for older version that do not support accommodation types.*/
	public static List<Rent> getOldRentsNextPageByMapBoundaries(double minLatitude, double maxLatitude,
			double minLongitude, double maxLongitude, Date lastRentDate, int lastRentId,
			byte rentStatus, byte rentForm, int pageSize) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		List<Rent> result = null;
		try {
			RentDAO rentDAO = session.getMapper(RentDAO.class);
			result = rentDAO.getRentsNextPageByMapBoundaries(minLatitude, maxLatitude, minLongitude,
					maxLongitude, lastRentDate, lastRentId, rentStatus, rentForm, 
					PropertyType.OFFICE.getType(), pageSize, ApplicationManager.getAppURL());
		} finally {
			session.close();
		}
		
		return result;
	}
	
	private static void convertPricesToBaseCurrency(RentSearch rentSearch, String currency) {
		if(rentSearch.getLowRent().getRentPrice() != null) {
			rentSearch.getLowRent().setRentCurrency(currency);
			CurrencyManager.convertRentPrice(Currency.EUR.toString(), rentSearch.getLowRent());
		}
		if(rentSearch.getHighRent().getRentPrice() != null) {
			rentSearch.getHighRent().setRentCurrency(currency);
			CurrencyManager.convertRentPrice(Currency.EUR.toString(), rentSearch.getHighRent());
		}
	}
}
