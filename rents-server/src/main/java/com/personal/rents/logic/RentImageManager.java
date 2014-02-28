package com.personal.rents.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.personal.rents.dao.RentImageDAO;
import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.model.RentImage;
import com.personal.rents.util.FileUtil;
import com.personal.rents.webservice.exception.OperationFailedException;
import com.personal.rents.webservice.util.ContextConstants;

public class RentImageManager {
	
	private static Logger logger = Logger.getLogger(RentImageManager.class);

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
	
	public static String uploadRentImage(InputStream imageInputStream, int rentId) {
		String imagePath = buildImagePath(rentId);
		String imageFilename = buildImageFilename();
		String imageURI = buildImageURI(imagePath, imageFilename);

		RentImage rentImage = new RentImage();
		rentImage.setRentId(rentId);
		rentImage.setRentImageURI(imageURI);
		addRentImage(rentImage);

		if(rentImage.getRentImageId() == null) {
			throw new OperationFailedException();
		}
		
		try {
			FileUtil.saveFile(imageInputStream, imagePath, imageFilename);
		} catch (IOException ioe) {
			logger.error("An error occured while uploading image '" + imageURI + "'", ioe);
			deleteRentImage(rentImage.getRentImageId());
			
			throw new OperationFailedException();
		}
		
		return imageURI;
	}
	
	private static String buildImagePath(int rentId) {
		return ContextConstants.FILE_SEPARATOR + ContextConstants.IMAGES_PATH
				+ ContextConstants.FILE_SEPARATOR + rentId;
	}
	
	private static final String buildImageFilename() {
		return new Date().getTime() + ContextConstants.IMAGE_FILE_EXT;
	}
	
	private static String buildImageURI(String imagePath, String imageFilename) {
		return imagePath + ContextConstants.FILE_SEPARATOR + imageFilename;
	}
}
