package parser;

import java.io.File;

public class ParserCommons {
	
	/**
	 * This method returns the filename of a file without path or extension 
	 * Note: from here https://www.technicalkeeda.com/java-tutorials/get-filename-without-extension-using-java
	 * @param file
	 * @return
	 */
	protected static String getFileNameWithoutExtension(File file) {
        String fileName = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                fileName = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileName = "";
        }
 
        return fileName;
 
    }
}