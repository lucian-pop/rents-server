package com.personal.rents.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.personal.rents.listener.ApplicationManager;
import com.personal.rents.webservice.util.ContextConstants;

public final class FileUtil {
	
	public static void saveFile(InputStream inputStream, String filepath, String filename)
			throws IOException {
		String absoluteFilepath = ApplicationManager.getAppRealPath() + filepath;

		OutputStream outputStream = null;
		try {
			File fileDirs = new File(absoluteFilepath);
			if(!fileDirs.exists()) {
				fileDirs.mkdirs();
			}

			outputStream = new FileOutputStream(new File(absoluteFilepath, filename));
			
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
	}
	
	public static boolean deleteFile(String filepath) {
		String absoluteFilepath = ApplicationManager.getAppRealPath() + filepath;
		File file = new File(absoluteFilepath);
		if(file.exists()) {
			return file.delete();
		}
		
		return false;
	}
}
