package TwelveProblems;

import java.util.ArrayList;

/**
 * Class: TwelveProblems
 * 
 * @author Purpose: Serve as code complexity checker instructor solution
 *         
 */
public class TwelveProblems {

	public static double distanceFromOrigin(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	public static boolean secondDigit5(int input) {
		return (input / 10) % 10 == 5;
	}


	public static boolean endsWithUpperCaseLetter(String input) {
		if (input.isEmpty()) {
			return false;
		} else {
			return Character.isUpperCase(input.charAt(input.length() - 1));
		}
	}

	public static double pow(int num, int power) {
		int total = 1;
		int abs = power;
		if (power < 0) {
			abs = -abs;
		}
		for (int i = 0; i < abs; i++) {
			total *= num; 
		} // end for
		if (power < 0) {
			return 1.0 / total;
		} else {
			return total;
		}
	}

	public static int firstDifference(String one, String two) {
		if (one.equals(two)) {
			return -1;
		} else {
			// Fact: !one.equals(two)
			int current = 0;
			while (one.charAt(current) == two.charAt(current)) {
				current++;
			} // end while
			return current;
		} // end if
	}

	public static char mostCommonCharacter(String input) {
		int winningSum = -1;
		char winningChar = 0;
		for (int i = 0, z = input.length(); i < z; i++) {
			int currentSum = 0;
			char currentChar = input.charAt(i);
			for (int j = i; j < input.length(); j++) {
				if (input.charAt(j) == currentChar) {
					currentSum += 1;
				} // end if
			} // end for
			if (currentSum > winningSum) {
				winningSum = currentSum;
				winningChar = currentChar;
			} // end if
		} // end for
		return winningChar;
	}

	public static int firstDivisibleBy77(int[] numbers) {
		int k = 0, z = numbers.length;
		while ((k < z) && (numbers[k] % 77 != 0)) {
			// Fact: no number in numbers[0..k] is divisible by 77
			k++;
		} // end while
		if (k == z) {
			// no number found
			return -1;
		} else {
			return numbers[k];
		}
	}

	public static int[] powersOfTwo(int maxExponent) {
		if (maxExponent < 0) {
			return new int[0];
		} else {
			int[] retVal = new int[maxExponent + 1];
			int currentPower = 1;
			for (int i = 0; i <= maxExponent; i++) {
				retVal[i] = currentPower;
				currentPower = currentPower * 2;
			} // end for
			return retVal;
		} // end if
	}

	public static int[] maxArray(int[] one, int[] two) {
		int[] retVal = new int[one.length];
		for(int k = 0; k < one.length; k++) {
			if (one[k] > two[k]) {
				retVal[k] = one[k];
			} else {
				retVal[k] = two[k];
			}
		} // end for
		return retVal;
	}

	public static int timesOccur(int[] shorter, int[] longer) {
		int timesMatched = 0;
		for(int i = 0; i < (longer.length - shorter.length + 1); i++) {
			boolean matches = true;
			for(int j = 0, z = shorter.length; j < z; j++) {
				matches = matches && (shorter[j] == longer[i+j]);
			} // end if
			if(matches) {
				timesMatched++;
			} // end if
		} // end for
		return timesMatched;
	}

	public static ArrayList<String> doubleDouble(ArrayList<String> input) {
		ArrayList<String> retVal = new ArrayList<String>();
		for(String s : input) {
			retVal.add(s);
			if(s.equals("double")) {
				retVal.add(s);
			} // end if
		} // end for
		return retVal;
	}

	public static ArrayList<String> threeCharacterStrings(String input) {
		ArrayList<String> retVal = new ArrayList<String>();
		while(input.length() > 2) {
			retVal.add(input.substring(0, 3));
			input = input.substring(1);
		} // end while
		return retVal;
	}
}
