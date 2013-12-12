package com.personal.rents.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.webservice.util.ContextConstants;

public class ImageManager {
	
	public static String saveImage(InputStream inputStream, String filename, String accountId,
			String datetime) throws IOException {
		String imagePath = ContextConstants.FILE_SEPARATOR + ContextConstants.IMAGES_PATH 
				+ ContextConstants.FILE_SEPARATOR + accountId + ContextConstants.FILE_SEPARATOR 
				+ datetime + ContextConstants.FILE_SEPARATOR + filename;

		OutputStream outputStream = null;
		try {
			String imgFolderPath = ApplicationManager.getAppRealPath() 
					+ ContextConstants.IMAGES_PATH + ContextConstants.FILE_SEPARATOR + accountId 
					+ ContextConstants.FILE_SEPARATOR + datetime;
			File imgDirs = new File(imgFolderPath);
			if(!imgDirs.exists()) {
				imgDirs.mkdirs();
			}

			outputStream = new FileOutputStream(new File(imgFolderPath, filename));
			
			int read = 0;
			byte[] bytes = new byte[10*1024];
			while ((read = inputStream.read(bytes)) > 0 ) {
				outputStream.write(bytes, 0, read);
			}

			outputStream.flush();
		} finally {
			outputStream.close();
			inputStream.close();
		}
		
		return imagePath;
	}

}
