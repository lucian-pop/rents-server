package ro.fizbo.rents.logic;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.dao.RentFavoriteDAO;
import ro.fizbo.rents.dao.param.RentsFavorites;
import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.RentFavorite;

public class RentFavoriteManager {
	public static boolean addRentToFavorites(int accountId, int rentId) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentFavoriteDAO rentFavoritesDAO = session.getMapper(RentFavoriteDAO.class);
			RentFavorite rentFavorite = rentFavoritesDAO.getEntry(accountId, rentId);
			if(rentFavorite != null) {
				return false;
			}

			rentFavoritesDAO.addEntry(accountId, rentId, new Date());
			session.commit();
		} finally {
			session.close();
		}
		
		return true;
	}
	
	public static int deleteUserFavoriteRents(int accountId, List<Integer> rentIds) {
		RentsFavorites rentsFavorites = new RentsFavorites();
		rentsFavorites.accountId = accountId;
		rentsFavorites.rentIds = rentIds;

		int deletesCount = -1;
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			deletesCount = session.delete("RentFavoriteMapper.deleteFavoriteRents", rentsFavorites);
			session.commit();
		} finally {
			session.close();
		}

		return deletesCount;
	}
}
