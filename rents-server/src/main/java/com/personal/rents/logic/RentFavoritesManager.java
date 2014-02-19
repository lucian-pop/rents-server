package com.personal.rents.logic;

import org.apache.ibatis.session.SqlSession;

import com.personal.rents.dao.RentFavoritesDAO;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.RentFavorite;

public class RentFavoritesManager {
	public static boolean addRentToFavorites(int accountId, int rentId) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentFavoritesDAO rentFavoritesDAO = session.getMapper(RentFavoritesDAO.class);
			RentFavorite rentFavorite = rentFavoritesDAO.getEntry(accountId, rentId);
			if(rentFavorite != null) {
				return false;
			}

			rentFavoritesDAO.addEntry(accountId, rentId);
			session.commit();
		} finally {
			session.close();
		}
		
		return true;
	}
}
