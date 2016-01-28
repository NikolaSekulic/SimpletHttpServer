package hr.fer.zemris.java.custom.scripting.exec.operations;

/**
 * Implements of {@link Operation} for comparison of two numbers.
 *
 * @author Nikola Sekulić
 *
 */
public class Comparison extends Operation {

	@Override
	/**
	 * Compares two number. If numbers are equals returns 0.
	 * If first number is lesser than second returns -1,
	 * otherwise returns +1.
	 *  Result is returned as instance of Integer.
	 */
	public Object doOperation(int number1, int number2) {

		if (number1 == number2) {
			return 0;
		}
		return number1 < number2 ? -1 : 1;
	}

	@Override
	/**
	 * Compares two number. If numbers are equals returns 0.
	 * If first number is lesser than second returns -1,
	 * otherwise returns +1.
	 *  Result is returned as instance of Integer.
	 */
	public Object doOperation(double number1, double number2) {

		if (number1 == number2) {
			return 0;
		}
		return number1 < number2 ? -1 : 1;
	}

	@Override
	/**
	 * Compares two number. If numbers are equals returns 0.
	 * If first number is lesser than second returns -1,
	 * otherwise returns +1.
	 *  Result is returned as instance of Integer.
	 */
	public Object doOperation(int number1, double number2) {

		if (number1 == number2) {
			return 0;
		}
		return number1 < number2 ? -1 : 1;
	}

	@Override
	/**
	 * Compares two number. If numbers are equals returns 0.
	 * If first number is lesser than second returns -1,
	 * otherwise returns +1.
	 *  Result is returned as instance of Integer.
	 */
	public Object doOperation(double number1, int number2) {

		if (number1 == number2) {
			return 0;
		}
		return number1 < number2 ? -1 : 1;
	}

}
