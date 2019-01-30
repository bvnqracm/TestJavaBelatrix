package com.java.alexcm.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

/*
 * Author 			: Alex Cruz
 * Version			: 1.0
 * Creation Date	: 2019-01-30
 * Class			: Executor which throws threads to process input url file.
 */
public class Executor {

	//Declaring class variables
	private String inputPath;
	private String outputPath;
	
	//Static variables
	public static String EMPTY_URLS_LIST = "There are not urls to process.";
	
	//Constructor class Executor
	public Executor(String inputPath, String outputPath) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}

	/*
	 * Main process which sets regular expression to throws threads and process matching.
	 * Program logic generates one thread by url read, so as to execute parallel process
	 */
	public void process(Pattern expression) {
		int numWorker = 1;
		LinkedList<String> listUrls = this.readInputFile(this.inputPath);
		if(listUrls != null) {
			if(listUrls.size() > 0) {
				for (String url : listUrls) {			
					Worker worker = new Worker(expression, numWorker, url, this.outputPath);			
					worker.start();
					numWorker ++;
				}
			} else {
				System.out.println(EMPTY_URLS_LIST);
			}			
		}		
	}

	/*
	 * Reading file and saving urls into LinkedList object
	 */	
	private LinkedList<String> readInputFile(String pathFile) {

		BufferedReader br = null;
		String line;
		LinkedList<String> listUrl = new LinkedList<String>();
		
		try { 				
			br = new BufferedReader(new FileReader(pathFile));			
			while ((line = br.readLine()) != null) {
				//System.out.println("Url read: " + line);
				listUrl.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			listUrl = null;
		} catch (IOException e) {
			e.printStackTrace();
			listUrl = null;
		} finally {
			try {
				br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				br = null;	
			}
		}

		return listUrl;
	}

}
