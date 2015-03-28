package ro.fizbo.rents.logic;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.RentAbuse;
import ro.fizbo.rents.model.RentAbuseStatus;

public final class ReportRentAbuseManager {
	
	private static Logger logger = Logger.getLogger(ReportRentAbuseManager.class);
	
	private ReportRentAbuseManager() {
	}
	
	public static void reportRentAbuse(RentAbuse rentAbuse) {
		rentAbuse.setRentAbuseDate(new Date());
		rentAbuse.setRentAbuseTokenKey(TokenGenerator.generateToken());
		rentAbuse.setRentAbuseStatus(RentAbuseStatus.PENDING.getStatus());
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			session.insert("RentAbuseMapper.insertRentAbuse", rentAbuse);
			session.commit();
		} catch (RuntimeException runtimeException) {
			logger.error("An error occured while adding rent abuse for rent with id " 
					+ rentAbuse.getRent().getRentId(), runtimeException) ;
			session.rollback();
			return;
		} finally {
			session.close();
		}

		EmailManager.sendRentAbuseUserReportEmail(rentAbuse);
		EmailManager.sendRentAbuseReportEmail(rentAbuse);
	}

	public static void resolveRentAbuse(String rentAbuseTokenKey, String rentAbuseStatus,
			String rentAbuseResolutionComment) {
		RentAbuse rentAbuse = new RentAbuse();
		rentAbuse.setRentAbuseResolutionDate(new Date());
		rentAbuse.setRentAbuseResolutionComment(rentAbuseResolutionComment);
		rentAbuse.setRentAbuseStatus(Byte.valueOf(rentAbuseStatus));
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			session.update("RentAbuseMapper.updateRentAbuse", rentAbuse);
			if(rentAbuse.getRentAbuseStatus() > RentAbuseStatus.RESOLVED_OK.getStatus()) {
				session.update("RentMapper.disableRentForAbuse", rentAbuseTokenKey);
			}
			session.commit();
		} catch (RuntimeException runtimeException) {
			logger.error("An error occured while updating rent abuse with token key "
					+ rentAbuseTokenKey, runtimeException);
			session.rollback();
			return;
		} finally {
			session.close();
		}
	}
}