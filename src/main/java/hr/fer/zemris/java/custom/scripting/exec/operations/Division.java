package hr.fer.zemris.java.custom.scripting.exec.operations;

/**
 * Implements of {@link Operation} for division.
 *
 * @author Nikola Sekulić
 *
 */
public class Division extends Operation {

	@Override
	/**
	 * Divides first number with second number.
	 * Result is instance of Integer.
	 */
	public Object doOperation(int number1, int number2) {

		Integer result = number1 / number2;
		return result;
	}

	@Override
	/**
	 * Divides first number with second number.
	 * Result is instance of Double.
	 */
	public Object doOperation(double number1, double number2) {

		Double result = number1 / number2;
		return result;
	}

	@Override
	/**
	 * Divides first number with second number.
	 * Result is instance of Double.
	 */
	public Object doOperation(int number1, double number2) {

		Double result = number1 / number2;
		return result;
	}

	@Override
	/**
	 * Divides first number with second number.
	 * Result is instance of Double.
	 */
	public Object doOperation(double number1, int number2) {

		Double result = number1 / number2;
		return result;
	}

}
