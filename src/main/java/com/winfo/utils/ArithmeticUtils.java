package com.winfo.utils;

public class ArithmeticUtils {

	public static boolean numericComparision(Double number1, Double number2, String operator) {
		boolean isRightComparision = false;
		switch (operator) {
		case "<=":
			isRightComparision = number1 <= number2;
			break;
		case ">=":
			isRightComparision = number1 >= number2;
			break;
		case "<":
			isRightComparision = number1 < number2;
			break;
		case ">":
			isRightComparision = number1 > number2;
			break;
		case "=":
			isRightComparision = number1.equals(number2);
			break;

		case "!=":
			isRightComparision = !(number1.equals(number2));
			break;

		default:
			break;
		}
		return isRightComparision;
	}

	public static boolean stringComparision(String input1, String input2) {

		return input1.equalsIgnoreCase(input2);
	}

	private ArithmeticUtils() {
		throw new IllegalStateException("Utility class");
	}

}
