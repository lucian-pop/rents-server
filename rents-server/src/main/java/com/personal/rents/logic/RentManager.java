package com.personal.rents.logic;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.personal.rents.dao.AddressDAO;
import com.personal.rents.dao.RentDAO;
import com.personal.rents.dao.RentImageDAO;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.Rent;
import com.personal.rents.model.RentImage;

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
			if((rent.getImageURIs() != null) && (rent.getImageURIs().size() > 0)) {
				RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
				RentImage rentImage = null;
				for(String imageURI : rent.getImageURIs()) {
					rentImage = new RentImage();
					rentImage.setRentId(rent.getId());
					rentImage.setImageURI(imageURI);
					
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
}
