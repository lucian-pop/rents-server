package ro.fizbo.rents.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ro.fizbo.rents.listener.ApplicationManager;

public final class FileUtil {
	
	public static void saveFile(byte[] fileBytes, String filepath, String filename)
			throws IOException {
		String absoluteFilepath = ApplicationManager.getAppRealPath() + filepath;

		OutputStream outputStream = null;
		try {
			File fileDirs = new File(absoluteFilepath);
			if(!fileDirs.exists()) {
				fileDirs.mkdirs();
			}

			outputStream = new FileOutputStream(new File(absoluteFilepath, filename));
			outputStream.write(fileBytes, 0, fileBytes.length);
			outputStream.flush();
		} finally {
			outputStream.close();
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
