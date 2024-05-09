package TwelveProblems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import TwelveProblemsTest.RunAllTestsTearDown;
import TwelveProblemsTest.TestDistanceFromOrigin;
import TwelveProblemsTest.TestDoubleDouble;
import TwelveProblemsTest.TestEndsWithUpperCaseLetter;
import TwelveProblemsTest.TestFirstDifference;
import TwelveProblemsTest.TestFirstDivisibleBy77;
import TwelveProblemsTest.TestMaxArray;
import TwelveProblemsTest.TestMostCommonCharacter;
import TwelveProblemsTest.TestPow;
import TwelveProblemsTest.TestPowersOfTwo;
import TwelveProblemsTest.TestSecondDigit5;
import TwelveProblemsTest.TestThreeCharacterStrings;
import TwelveProblemsTest.TestTimesOccur;

@RunWith(Suite.class)

@SuiteClasses({ TestDistanceFromOrigin.class, TestSecondDigit5.class, TestEndsWithUpperCaseLetter.class,
		TestPow.class, TestFirstDifference.class, TestMostCommonCharacter.class, TestFirstDivisibleBy77.class,
		TestPowersOfTwo.class, TestMaxArray.class, TestTimesOccur.class, TestDoubleDouble.class,
		TestThreeCharacterStrings.class, RunAllTestsTearDown.class })

public class RunAllTests {

	static public String timestamp = new Timestamp(System.currentTimeMillis()).toString();
	static public int allTestsPassedCount = 0;
	static public int allTestsExecutedCount = 0;
	

	// -----------------------------------------------
	// The following fields added to support pushing results.csv to server
	static public String csvData = "";
	static public String csvDataWithNewFeatures = "";
	static public final String ASSIGNMENT_NAME = "TwelveProblems";
	static public final String PATH_TO_PROGRAM = "src/TwelveProblems/TwelveProblems.java";
//	static public final String SERVER_URL = "http://csedresearch.csse.rose-hulman.edu:443/";
	static public final String SERVER_URL = "http://137.112.40.140:443/";
	static public final String OPT_OUT_URL = SERVER_URL + "opt-in-out/";
	static public boolean assignedCommitId = false;

	static private final boolean DEBUG_MODE = false; // set to true to debug errors sending data to the research team
	// -----------------------------------------------


	public static void outputResults(int testsPassed, int numberOfTests) {
		// Add to grand total
		RunAllTests.allTestsPassedCount += testsPassed;
		RunAllTests.allTestsExecutedCount += numberOfTests;

	} // outputResults
	
	// -----------------------------------------------
	// The following operations added to support pushing results.csv to server

	public static void exportFinalCSV(String csvData) {
		RunAllTests.csvData += csvData;
	}

	public static void exportFinalCSVWithNewFeatures(String csvData) {
		RunAllTests.csvDataWithNewFeatures += csvData;
	}

	public static void printError(String description, Exception e) {
		if (DEBUG_MODE) {
			System.out.println("Error sending data for research: " + description);
			e.printStackTrace();
			System.out.println("==============================================================");
		}
	}
	
	public static void printErrorNoTraceback(String description) {
		System.out.println("Error sending data for research: " + description);
		System.out.println("==============================================================");
	}

	public static String readFile(File file) {
		String data = "";
		try {
			FileReader reader = null;
			try {
				reader = new FileReader(file);
			} catch (FileNotFoundException e) {
				printError("File " + file.getCanonicalPath() + " not found", e);
			}

			int ch;

			while ((ch = reader.read()) != -1) {
				data += (char) ch;
			}
			reader.close();

		} catch (IOException e) {
			printError("Unknown exception: IOException", e);
		}

		return data;

	}

	public static void writeToFile(String data, File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			RunAllTests.printError("Unknown exception: IOException", e);
		}
	}
	// -----------------------------------------------

}
