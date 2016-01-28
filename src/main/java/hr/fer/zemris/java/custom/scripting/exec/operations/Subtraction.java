package hr.fer.zemris.java.custom.scripting.exec.operations;

/**
 * Implements of {@link Operation} for subtraction.
 *
 * @author Nikola Sekulić
 *
 */
public class Subtraction extends Operation {

	@Override
	/**
	 * Subtracts second number from first number.
	 * Result is instance of Integer.
	 */
	public Integer doOperation(int number1, int number2) {

		Integer result = number1 - number2;
		return result;
	}

	@Override
	/**
	 * Subtracts second number from first number.
	 * Result is instance of Double.
	 */
	public Double doOperation(double number1, double number2) {

		Double result = number1 - number2;
		return result;
	}

	@Override
	/**
	 * Subtracts second number from first number.
	 * Result is instance of Double.
	 */
	public Double doOperation(int number1, double number2) {

		Double result = number1 - number2;
		return result;
	}

	@Override
	/**
	 * Subtracts second number from first number.
	 * Result is instance of Double.
	 */
	public Double doOperation(double number1, int number2) {

		Double result = number1 - number2;
		return result;
	}

}
