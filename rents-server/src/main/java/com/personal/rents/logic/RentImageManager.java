package com.personal.rents.logic;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.dao.RentImageDAO;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.RentImage;

public class RentImageManager {

	public static void addRentImage(RentImage rentImage) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
			rentImageDAO.insertRentImage(rentImage);
			session.commit();
		} finally {
			session.close();
		}
	}
}
