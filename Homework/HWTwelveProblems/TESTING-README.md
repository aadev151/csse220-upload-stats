# HWTwelveProblems

## Changes Joe made to get this to run as far as it does
- In RunAllTests.java, changed static variable declaration from URL to IP address
	static public final String SERVER_URL = "http://137.112.40.140:443/";
	
	
- In RunAllTestsTearDown.java changed these two static variable declarations to:
	private static File preferencesFile = new File("../../Administration/ResearchShareData/researchShareDataPreferences.txt");
	private static File usernameFile = new File("../../Administration/ResearchShareData/username.txt");

A Teams text message specified that the two .txt files were to reside in the folder: "../../Administration/ResearchShareData"

- I did create the folder "../../Administration/ResearchShareData" and I placed the two empty .txt files in them
- The researchShareDataPreferences.txt ended up storing a zero, I presume because I opted in to uploading my data

- I changed these static variable declarations to:
	static public final String ASSIGNMENT_NAME = "TwelveProblems";
	static public final String PATH_TO_PROGRAM = "src/TwelveProblems/TwelveProblems.java";