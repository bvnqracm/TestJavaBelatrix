package com.java.alexcm.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.java.alexcm.util.Tools;

/*
 * Author 			: Alex Cruz
 * Version			: 1.0
 * Creation Date	: 2019-01-30
 * Class			: Worker, this class inherits from Thread and writes matching results into output file with url's name.
 */
public class Worker extends Thread {
	
	// Static messages
	public static String OK_MESSAGE = "[Processed OK] Url: ";
	public static String ERROR_MESSAGE = "[Processed Error] Url: ";
	
	// Variables
	private String url;
	private String outputPath;
	private int numWorker;
	private Pattern expression;

	// Class constructor
	public Worker(Pattern expressionType, int numWorker, String url, String outputPath) {
		this.expression = expressionType;
		this.url = url;
		this.outputPath = outputPath;
		this.numWorker = numWorker;
	}

	@Override
	public void run() {
		if(processUrl()) {
			System.out.println(OK_MESSAGE + this.url);
		} else {
			System.out.println(ERROR_MESSAGE + this.url);
		}
	}

	/*
	 * This method will download Url's mainpage line by line and matching with pattern.
	 * If there was match, this will be saved in match list.
	 * Finally, method will write match list in output file.
	 */
	private boolean processUrl() {

		//System.out.println("Downloading url: " + this.url);
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		int numLine = 0;
		boolean status = false;
		
		try {
			url = new URL(this.url);
			is = url.openStream();
			br = new BufferedReader(new InputStreamReader(is));
			LinkedList<String> listMatchs = new LinkedList<String>();
			while ((line = br.readLine()) != null) {
				String match = this.searchForPattern(numLine, line);
				if (!match.equals(Tools.EMPTY_STRING)) {
					listMatchs.add(match);
				}					
				numLine++;
			}			
			this.writeOutputFile(listMatchs, this.url);
			is.close();
			status = true;
			//System.out.println("Downloaded url: " + this.url);			
		} catch (MalformedURLException urlException) {
			urlException.printStackTrace();			
		} catch (IOException writingException) {
			writingException.printStackTrace();	
		}
		return status;
	}
	
	/*
	 * Formatting line with result, contents thread, number of line, matching and all line matching
	 */
	private String searchForPattern(int numLine, String line) {
		final Matcher matcher = expression.matcher(line);
		if (matcher.find() && matcher.groupCount() > 0) {
			return Tools.outputFormat(this.numWorker, numLine, matcher.group(1), line);
		}
		return Tools.EMPTY_STRING;
	}

	/*
	 * Writting output file with result and with the name from url in user's path execution
	 */
	private void writeOutputFile(LinkedList<String> listaMatch, String urlDireccion) throws IOException {
		String nameFile = Tools.getNameFileFromUrl(urlDireccion);
		if(!nameFile.equals(Tools.EMPTY_STRING)) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputPath + nameFile + Tools.OUTPUT_FILE_EXTENSION));
			for (String match : listaMatch) {
				writer.write(match);
				writer.newLine();
			}
			writer.close();
		}		
	}

}
