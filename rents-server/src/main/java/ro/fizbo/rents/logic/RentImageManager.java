package ro.fizbo.rents.logic;

import java.io.IOException;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import ro.fizbo.rents.dao.RentImageDAO;
import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.RentImage;
import ro.fizbo.rents.util.Constants;
import ro.fizbo.rents.util.FileUtil;
import ro.fizbo.rents.webservice.exception.OperationFailedException;

public class RentImageManager {
	
	private static Logger logger = Logger.getLogger(RentImageManager.class);

	public static void addRentImage(RentImage rentImage) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
			rentImageDAO.insertRentImage(rentImage);

			session.commit();
		} catch (RuntimeException runtimeException) {
			logger.error("An error occured while adding rent image to database", runtimeException);
			session.rollback();
			
			throw new OperationFailedException();
		} finally {
			session.close();
		}
	}
	
	public static int deleteRentImage(int rentImageId) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		int deleteCount = -1;
		try {
			RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
			deleteCount = rentImageDAO.deleteRentImage(rentImageId);
			
			session.commit();
		} finally {
			session.close();
		}

		return deleteCount;
	}
	
	public static int updateRentImageURI(int rentImageId, String rentImageURI) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		int updateCount = -1;
		try {
			RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
			updateCount = rentImageDAO.updateRentImageURI(rentImageId, rentImageURI);

			session.commit();
		} finally {
			session.close();
		}
		
		return updateCount;
	}
	
	public static RentImage uploadRentImage(byte[] imageBytes, int rentId) {
		String imagePath = buildImagePath(rentId);
		String imageFilename = buildImageFilename();
		String imageURI = buildImageURI(imagePath, imageFilename);
		
		try {
			FileUtil.saveFile(imageBytes, imagePath, imageFilename);
		} catch (IOException ioe) {
			logger.error("An error occured while saving image '" + imageURI + "'", ioe);
			
			throw new OperationFailedException();
		}
		
		RentImage rentImage = new RentImage();
		rentImage.setRentId(rentId);
		rentImage.setRentImageURI(imageURI);
		addRentImage(rentImage);

		if(rentImage.getRentImageId() == null) {
			FileUtil.deleteFile(imageURI);
			logger.error("An error occured while adding image for rent" + rentId);

			throw new OperationFailedException();
		}
		
		rentImage.setRentImageURI(ApplicationManager.getAppURL() + imageURI);
		
		return rentImage;
	}
	
	public static RentImage replaceRentImage(byte[] imageBytes, int rentImageId,
			String rentImageURI, int rentId) {
		String imagePath = buildImagePath(rentId);
		String imageFilename = buildImageFilename();
		String imageURI = buildImageURI(imagePath, imageFilename);
		
		try {
			FileUtil.saveFile(imageBytes, imagePath, imageFilename);
		} catch (IOException ioe) {
			logger.error("An error occured while saving image '" + imageURI + "'", ioe);
			
			throw new OperationFailedException();
		}
		
		int updateCount = updateRentImageURI(rentImageId, imageURI);
		if(updateCount != 1) {
			FileUtil.deleteFile(imageURI);
			logger.error("An error occured while replacing image '" + rentImageId);

			throw new OperationFailedException();
		}
		
		FileUtil.deleteFile(rentImageURI.replaceFirst(ApplicationManager.getAppURL(), ""));
		
		RentImage rentImage = new RentImage();
		rentImage.setRentImageId(rentImageId);
		rentImage.setRentId(rentId);
		rentImage.setRentImageURI(ApplicationManager.getAppURL() + imageURI);
		
		return rentImage;
	}
	
	public static int deleteRentImageWithDiskCleanup(int rentImageId) {
		RentImage rentImage = null;
		int deleteCount = -1;
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			RentImageDAO rentImageDAO = session.getMapper(RentImageDAO.class);
			rentImage = rentImageDAO.getRentImage(rentImageId);

			deleteCount = rentImageDAO.deleteRentImage(rentImageId);
			
			session.commit();
		} catch (RuntimeException runtimeException) {
			logger.error("An error occured while deleting rent image with id " + rentImageId,
					runtimeException);
			session.rollback();
			
			throw new OperationFailedException();
		} finally {
			session.close();
		}
		
		if(deleteCount != 1) {
			logger.error("An error occured while deleting image with id " + rentImageId);
			
			throw new OperationFailedException();
		}
		
		if(rentImage != null) {
			FileUtil.deleteFile(rentImage.getRentImageURI());
		}
		
		return deleteCount;
	}
	
	private static String buildImagePath(int rentId) {
		return Constants.RENTS_IMAGES_PATH  + Constants.FILE_SEPARATOR + rentId;
	}
	
	private static final String buildImageFilename() {
		return new Date().getTime() + Constants.IMAGE_FILE_EXT;
	}
	
	private static String buildImageURI(String imagePath, String imageFilename) {
		return Constants.FILE_SEPARATOR + imagePath + Constants.FILE_SEPARATOR + imageFilename;
	}
}
