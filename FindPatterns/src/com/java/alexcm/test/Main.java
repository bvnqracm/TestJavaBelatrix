package com.java.alexcm.test;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.java.alexcm.util.Tools;

/*
 * Author 			: Alex Cruz
 * Version			: 1.0
 * Creation Date	: 2019-01-30
 * Class			: Main, beginnig of program, which requests input data from user and validates arguments.
 */
public class Main {

	public static String NO_REGEX_SET = "There is not expression regular configured.";
	public static String INVALID_ARGUMENTS_NUMBER = "Invalid arguments number, it must be 2.\nFirst: Argument must be kind of regular expression.\nSecond: Argument must be valid input file.";
	public static String EXPRESSION_NO_CONFIGURATED = "Expression without configuration.";
	public static String INVALID_FILE_NO_EXISTS = "Invalid file or not exists.";
	public static String EMPTY_VALUE_EXPRESSION = "Empty value to regular expression.";
	public static String ERROR_WITH_REGULAR_EXPRESSION = "Invalid regular expression.";

	public static int MAX_ARGUMENTS_NUMBER = 2;
	public static int NUMBER_ZERO = 0;
	public static int NUMBER_ONE = 1;

	public static void main(String[] arguments) {
		
		/* Value for test withou command line
		arguments = new String[2];
		arguments[0] = "TWITTER";
		arguments[1] = "F:\\urls.txt";
		*/
		
		// Validating input arguments
		if (arguments.length != MAX_ARGUMENTS_NUMBER) {
			System.out.println(INVALID_ARGUMENTS_NUMBER);
			return;
		}

		// Validating if it is ok with input file
		String inputPath = arguments[NUMBER_ONE];
		File fileInput = new File(inputPath);
		if (!fileInput.isFile() || !fileInput.exists()) {
			System.out.println(INVALID_FILE_NO_EXISTS);
			return;
		}

		// Generating outputpath to result files
		String outputPath = fileInput.getParent();

		// Loading file with dinamic regular expressions
		HashMap<String, String> matriz = Tools.readConfiguration();
		if(matriz != null) {
			if (matriz.isEmpty()) {
				System.out.println(NO_REGEX_SET);
				return;
			}
		}		

		// Validating if regular expression exists
		String expression = arguments[NUMBER_ZERO];
		if (!matriz.containsKey(expression)) {
			System.out.println(EXPRESSION_NO_CONFIGURATED);
			return;
		}
		
		//Validating if regular expression is empty
		String value = matriz.get(expression).trim();
		if (value.equals(Tools.EMPTY_STRING)) {
			System.out.println(EMPTY_VALUE_EXPRESSION);
			return;
		}

		//Compiling type of dinamic regular expression
		Pattern expressionValue = null;
		try {
			expressionValue = Pattern.compile(value, Pattern.DOTALL);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(ERROR_WITH_REGULAR_EXPRESSION);
			return;
		}
				
		// Creating executor object and sending kind of regular expression in order to
		// process match
		Executor executor = new Executor(inputPath, outputPath);
		executor.process(expressionValue);

	}
		
}
