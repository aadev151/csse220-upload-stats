package MapsTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;

import MapsHW.RunAllTests;

public class RunAllTestsTestRunner extends BlockJUnit4ClassRunner {
	static private final String filepath = "src/TestResults/";
	static private final String csvFilename = "results.csv";

	private int jUnitTestCounter;
	private int testFailure;
	private int currentTestDidPass;
	private String oneJUnitTestResult;
	private String resultsOfAllJUnitTests;
	private ArrayList<String> resultsToOutput;
	private String didPassAllTests;
	private String errors;
	private boolean operationIsImplemented;
	private String commitId;

	private String getRandomAlphanumericStringOfLength(int targetStringLength) {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}

	@SuppressWarnings("rawtypes")
	public RunAllTestsTestRunner(Class testClass) throws org.junit.runners.model.InitializationError {
		super(testClass);

		this.jUnitTestCounter = 0;
		this.testFailure = 0;
		this.oneJUnitTestResult = "";
		this.resultsOfAllJUnitTests = "";
		this.resultsToOutput = new ArrayList<>();
		this.didPassAllTests = "1";
		this.errors = "";
		this.operationIsImplemented = true;

		if (!RunAllTests.assignedCommitId) {
			this.commitId = getRandomAlphanumericStringOfLength(10);
			RunAllTestsTearDown.commitId = this.commitId;
			RunAllTests.assignedCommitId = true;
		}

	}

	@Override
	public void run(RunNotifier ideJUnitRunner) {

		String name = this.getTestClass().getName();
		name = name.substring(name.indexOf(".") + 1, name.length());
		final String jUnitTestFileExecuted = name;

		// count tests with Decorator Pattern
		RunNotifier decorator = new RunNotifier() {
			@Override
			public void fireTestStarted(Description description) throws StoppedByUserException {
				// Sets the state of the current test running so it is updated for every test.
				currentTestDidPass = 1;
				jUnitTestCounter++;
				oneJUnitTestResult = jUnitTestCounter + "T";
				ideJUnitRunner.fireTestStarted(description);
			}

			@Override
			public void fireTestFailure(Failure failure) {
				testFailure++;
				currentTestDidPass = 0;
				String msg = failure.getMessage(); // write that

				errors += msg;

				if (msg != null && msg.equals("TODO: delete this statement and implement this operation.")) {
					operationIsImplemented = false;
				}

				oneJUnitTestResult = jUnitTestCounter + "F";
				didPassAllTests = "0";
				ideJUnitRunner.fireTestFailure(failure);
			}

			@Override
			public void fireTestFinished(Description description) {
				// Executes regardless whether the test passed.
				resultsOfAllJUnitTests = resultsOfAllJUnitTests + " " + oneJUnitTestResult;
				RunAllTests.outputResults(currentTestDidPass, 1);
				ideJUnitRunner.fireTestFinished(description);

			}
		};

		super.run(decorator);
		System.out.printf("%-40s %-1d Tests Executed\tFailures: %-3d\n\n", name, jUnitTestCounter, testFailure);

		if (operationIsImplemented) {
			resultsToOutput.add(jUnitTestFileExecuted);
			resultsToOutput.add(resultsOfAllJUnitTests);
			resultsToOutput.add(didPassAllTests);
			resultsToOutput.add(RunAllTests.timestamp);
			RunAllTests.exportFinalCSV(resultsToOutput.toString().substring(1, resultsToOutput.toString().length() - 1)
					.replace(",  ", ",").replace(", ", ",") + "\n");
			resultsToOutput.add(errors.replace("\n", "\\n"));
			getDiff();
			exportResults(resultsToOutput);
		}

	}

	private void exportResults(ArrayList<String> output) {

		try {
			File myObj = new File(filepath + csvFilename);
			myObj.createNewFile();

			try {
				FileOutputStream fos = new FileOutputStream(filepath + csvFilename, true);
				String outputString = output.toString().substring(1, output.toString().length() - 1) + "\n";

				// Software that does post processing of .csv file requires that there be no
				// spaces after the commas
				outputString = outputString.replace(",  ", ",");
				outputString = outputString.replace(", ", ",");

				fos.write(outputString.getBytes());
				fos.close();

				RunAllTests.exportFinalCSVWithNewFeatures(outputString);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private void getDiff() {
		resultsToOutput.add(String.valueOf(getFileSizeDifference()));
		resultsToOutput.add(String.valueOf(getFileLengthDifference()));
	}

	private long getFileSizeDifference() {
		File fileSizeFile = new File("src/TestResults/fileSize.txt");

		if (fileSizeFile.exists() && !fileSizeFile.isDirectory()) {
			long prevFileSize = Long.parseLong(RunAllTests.readFile(fileSizeFile));
			long curFileSize = new File(RunAllTests.PATH_TO_PROGRAM).length();
			RunAllTests.writeToFile(String.valueOf(curFileSize), fileSizeFile);

			return curFileSize - prevFileSize;

		} else {
			long curFileSize = new File(RunAllTests.PATH_TO_PROGRAM).length();
			RunAllTests.writeToFile(String.valueOf(curFileSize), fileSizeFile);

			return curFileSize;
		}
	}

	private int getFileLengthDifference() {
		// includes \n and \r

		File fileLengthFile = new File("src/TestResults/fileLength.txt");

		if (fileLengthFile.exists() && !fileLengthFile.isDirectory()) {
			int prevFileLength = Integer.parseInt(RunAllTests.readFile(fileLengthFile));
			int curFileLength = RunAllTests.readFile(new File(RunAllTests.PATH_TO_PROGRAM)).length();
			RunAllTests.writeToFile(String.valueOf(curFileLength), fileLengthFile);

			return curFileLength - prevFileLength;

		} else {
			int curFileLength = RunAllTests.readFile(new File(RunAllTests.PATH_TO_PROGRAM)).length();
			RunAllTests.writeToFile(String.valueOf(curFileLength), fileLengthFile);

			return curFileLength;
		}
	}

}
