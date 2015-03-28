package ro.fizbo.rents.logic;

import org.apache.ibatis.session.SqlSession;

import ro.fizbo.rents.dao.RentStatisticsDAO;
import ro.fizbo.rents.listener.ApplicationManager;

public final class RentStatisticsManager {
	
	private RentStatisticsManager() {
	}

	public static void incrementRentViewsNo(int rentId) {
		SqlSession session =  ApplicationManager.getSqlSessionFactory().openSession();
		try {
			session.getMapper(RentStatisticsDAO.class).updateInsert(rentId);
			session.commit();
		} finally {
			session.close();
		}
	}
}
