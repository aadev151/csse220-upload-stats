package MapsTest;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.swing.JOptionPane;

import org.junit.AfterClass;
import org.junit.Test;

import MapsHW.RunAllTests;

public class RunAllTestsTearDown {

	public static String commitId = "";
	private static File preferencesFile = new File(
			"../../Administration/Csse220FileTool/ResearchShareData/researchShareDataPreferences.txt");
	private static File usernameFile = new File("../../Administration/Csse220FileTool/ResearchShareData/username.txt");

	@AfterClass
	public static void oneTimeTearDown() {
		double percentagePassed = (double) RunAllTests.allTestsPassedCount / (double) RunAllTests.allTestsExecutedCount
				* 100.0;
		System.out.println("------------------------------------------------------------------");
		System.out.printf("%5d   %8d   %10.1f%%   %-15s\n", RunAllTests.allTestsExecutedCount,
				RunAllTests.allTestsPassedCount, percentagePassed, "<-- Grand Totals");

		int runResearchShareDataResult = runResearchShareData();

		if (runResearchShareDataResult != 0 && runResearchShareDataResult != 1) {
			printAboutError();
		} else {
			printAboutOptingInOut(runResearchShareDataResult);
		}

	} // oneTimeTearDown

	@Test
	public void alwaysPasses() {
		assertTrue(true);
	} // alwaysPasses

	private static int runResearchShareData() {
		String preferences = RunAllTests.readFile(preferencesFile);
		switch (preferences) {
		case "":
			return askAboutResearchParticipation();
		case "0":
			return sendResearchDataToServer(RunAllTests.readFile(usernameFile));
		case "1":
			overridePrevCodeFile(false, false);
			sendOptOutRequest();
			return sendResearchDataToServer(RunAllTests.readFile(usernameFile));
		default:
			throw new IllegalArgumentException("Unexpected value: " + preferences);
		}

	}

	private static void overrideFileSizeFile() {
		RunAllTests.writeToFile("0", new File("src/TestResults/fileSize.txt"));
		RunAllTests.writeToFile("0", new File("src/TestResults/fileLength.txt"));
	}

	private static void sendOptOutRequest() {
		// + override pref file
		String response = sendRequest(RunAllTests.SERVER_URL + "/opt-out-from-eclipse/" + RunAllTests.readFile(usernameFile), "");
		if (!response.equals("success")) {
			RunAllTests.printErrorNoTraceback("failed to send sendOptOutRequest");
			printAboutError();
		}
	}

	private static void overridePrevCodeFile(boolean pushToServer, boolean alternative) {
		File prevCodeFile = new File("src/TestResults/prevCode.txt");

		if (prevCodeFile.exists()) {
			String x = RunAllTests.readFile(prevCodeFile).replaceAll("\\s+", "");
			String y = RunAllTests.readFile(new File(RunAllTests.PATH_TO_PROGRAM)).replaceAll("\\s+", "");
			RunAllTests.writeToFile(y, prevCodeFile);

			if (pushToServer) {
				String requestParameters = "{\"x\": \"" + x.replace('"', '$') + "\", \"y\" : \"" + y.replace('"', '$')
						+ "\", \"alternative\": " + (alternative ? 1 : 0) + ", \"commitId\": \"" + commitId + "\"}";
				sendRequest(RunAllTests.SERVER_URL + "upload-data-for-levenshtein", requestParameters);
			}
		} else {
			String x = RunAllTests.readFile(new File(RunAllTests.PATH_TO_PROGRAM)).replaceAll("\\s+", "");
			RunAllTests.writeToFile(x, prevCodeFile);

			if (pushToServer) {
				sendRequest(RunAllTests.SERVER_URL + "initial-levenshtein",
						"{\"x\": \"" + x.replace('"', '$') + "\", \"commitId\": \"" + commitId + "\"}");
			}
		}
	}

	private static int askAboutResearchParticipation() {
		int participate = JOptionPane.showConfirmDialog(null,
				"Do you want to share the data about your test cases with the\n"
						+ "Rose-Hulman CSSE Research team? The data will in no way impact either your\n"
						+ "assignment score or the overall course grade. By gathering this data, CSSE\n"
						+ "faculty and interested Rose students hope to identify assignment-taking\n"
						+ "patterns and improve the course in the future. The data collected is anonymous\n"
						+ "and is not connected to your name, RHIT username, GitHub profile, or other\n"
						+ "personally identifiable information.",
				"Research participation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (participate == JOptionPane.CLOSED_OPTION) {
			overrideFileSizeFile(); // The files are overridden by RunAllTestsRunner.java before this code executes.
									// Therefore, if the pop-up was closed initially, the files will have the data
									// for the current file, and 0 will be sent to the server
			return -1;
		}

		RunAllTests.writeToFile(String.valueOf(participate), preferencesFile);
		if (participate == JOptionPane.YES_OPTION) {
			String username = getUsername();
			return sendResearchDataToServer(username);

		} else {
			return 1;
		}
	}

	private static int sendResearchDataToServer(String username) {
		if (username == null || username.isBlank()) {
			username = getUsername();
		}

		String requestParameters = "{\"csvData\": \"" + RunAllTests.csvDataWithNewFeatures
				+ "\", \"assignmentName\" : \"" + RunAllTests.ASSIGNMENT_NAME + "\", \"username\": \"" + username
				+ "\", \"commitId\": \"" + commitId + "\"}";
		String response = sendRequest(RunAllTests.SERVER_URL + "upload", requestParameters);

		if (response.equals("0")) {
			calculateLevenshteinDistanceRemotely(false);
			return 0;
		} else if (response.equals("1")) {
			overridePrevCodeFile(false, false);
			RunAllTests.writeToFile("0", preferencesFile);
			return 1;
		} else {
			RunAllTests.printErrorNoTraceback("Server error: /upload returned neither 0 nor 1 but " + response);
			return -1;
		}

	}

	private static String getUsername() {
		String username = sendRequest(RunAllTests.SERVER_URL + "getusername", "");
		RunAllTests.writeToFile(username, usernameFile);
		return username;
	}

	private static String sendRequest(String urlString, String params) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			byte[] postData = params.getBytes(StandardCharsets.UTF_8);
			try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
				wr.write(postData);
			} catch (Exception e) {
				RunAllTests.printError("Unknown", e);
				printAboutError();
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}

				return response.toString();
			}
		} catch (Exception e) {
			RunAllTests.printError("Failed to establish a connection with " + urlString, e);
			printAboutError();
		}

		return null;

	}

	private static void calculateLevenshteinDistanceRemotely(boolean alternative) {
		overridePrevCodeFile(true, alternative);
	}

	private static void printAboutError() {
		System.out.println("Error sending data for research.");
		System.out.println(
				"Please contact Alex at anisima@rose-hulman.edu or talk with your instructor to resolve the issue");
		System.out.println("This error does not affect your code or grade in any way. It is an");
		System.out.println("issue with the code sharing your data with the research team.");
	}

	private static void printAboutOptingInOut(int runResearchShareDataResult) {
		String username = RunAllTests.readFile(usernameFile);

		if (username == null || username.isBlank()) {
			username = getUsername();
		}

		String optOutUrl = RunAllTests.OPT_OUT_URL + username;

		if (runResearchShareDataResult == 0) {
			System.out.println("Your stats are successfully shared with the research team. Thanks :)");
			System.out.println("To opt out, please visit the link: " + optOutUrl);
		} else if (runResearchShareDataResult == 1) {
			System.out.println("To opt back in research participation, please visit the link:");
			System.out.println(optOutUrl);
		} else {
			printAboutError();
			RunAllTests.printErrorNoTraceback("runResearchShareDataResult in printAboutOptingInOut is not 0 or 1 but "
					+ runResearchShareDataResult);
		}
	}

}
