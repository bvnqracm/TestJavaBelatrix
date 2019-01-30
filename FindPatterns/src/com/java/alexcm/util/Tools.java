package com.java.alexcm.util;

//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/*
 * Author 			: Alex Cruz
 * Version			: 1.0
 * Creation Date	: 2019-01-30
 * Class			: Tools, its functionality is to give in the future news static methods to reuse logic.
 */
public class Tools {
	
	//Static variables
	public static String EMPTY_STRING = "";
	public static String OUTPUT_FILE_EXTENSION = ".txt";
	public static int MAX_LENGTH_NAME_URL = 20;
	public static String FILE_EXPRESSION_PROPERTIES = "expresiones.properties";

	/*
	 * Reading dinamic regular expressions from file, in the future it is possible to add more.
	 * Data is saved in HashMap object with key and value. 
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> readConfiguration(/*String outputPath*/) {

		HashMap<String, String> matriz = new HashMap<String, String>();

		try {
			Properties prop = new Properties();
			prop.load(new Tools().getInputStream(FILE_EXPRESSION_PROPERTIES));
			Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
			while (enums.hasMoreElements()) {
				String key = enums.nextElement();
				String value = prop.getProperty(key);
				matriz.put(key, value);
				System.out.println(key + " : " + value);
			}

		} catch (IOException e) {
			e.printStackTrace();
			matriz = null;
		}
		
		return matriz;
	}
	
	/*
	 * Formatting line with result, contents thread, number of line, match and all line match
	 */
	public static String outputFormat(int numWorker, int numLine, String result, String line) {
		String newLinea = "Worker(" + numWorker  + ")" + ", Line # " + numLine + ", Match '" + result + "' >> " + line.trim();
		//System.out.println(newLinea);
		return newLinea;
	}
	
	/*
	 * Formatting new number to output file from original url string
	 */
	public static String getNameFileFromUrl(String urlDirection) {
		String nameUrl, newNameUrl = Tools.EMPTY_STRING;
		
		try {
			nameUrl = urlDirection.replace("/", "").replace(".", "").replace(":", "");
			int len = nameUrl.length();
			if (len > MAX_LENGTH_NAME_URL) {
				newNameUrl = nameUrl.substring(0, MAX_LENGTH_NAME_URL - 1);
			} else {
				newNameUrl = nameUrl;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return newNameUrl;
	}
	
	public InputStream getInputStream(String propertiesFile) {		
		return getClass().getClassLoader().getResourceAsStream(propertiesFile);
	}


}
