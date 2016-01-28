package hr.fer.zemris.java.custom.scripting.exec.operations;

/**
 * Implements of {@link Operation} for multiplication.
 *
 * @author Nikola Sekulić
 *
 */
public class Multiplication extends Operation {

	@Override
	/**
	 * Multiplies first number with second number.
	 * Result is instance of Integer.
	 */
	public Integer doOperation(int number1, int number2) {

		Integer result = number1 * number2;
		return result;
	}

	@Override
	/**
	 * Multiplies first number with second number.
	 * Result is instance of Double.
	 */
	public Double doOperation(double number1, double number2) {

		Double result = number1 * number2;
		return result;
	}

	@Override
	/**
	 * Multiplies first number with second number.
	 * Result is instance of Double.
	 */
	public Double doOperation(int number1, double number2) {

		Double result = number1 * number2;
		return result;
	}

	@Override
	/**
	 * Multiplies first number with second number.
	 * Result is instance of Double.
	 */
	public Double doOperation(double number1, int number2) {

		Double result = number1 * number2;
		return result;
	}

}
