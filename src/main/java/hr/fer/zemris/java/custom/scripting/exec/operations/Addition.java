package hr.fer.zemris.java.custom.scripting.exec.operations;

/**
 * Implements of {@link Operation} for addition between two numbers.
 *
 * @author Nikola Sekulić
 *
 */
public class Addition extends Operation {

	@Override
	/**
	 * Adds two number. Returns result as Instance of Integer.
	 */
	public Object doOperation(int number1, int number2) {

		Integer result = number1 + number2;
		return result;
	}

	@Override
	/**
	 * Adds two number. Returns result as Instance of Double.
	 */
	public Object doOperation(double number1, double number2) {

		Double result = number1 + number2;
		return result;
	}

	@Override
	/**
	 * Adds two number. Returns result as Instance of Integer.
	 */
	public Object doOperation(int number1, double number2) {

		Double result = number1 + number2;
		return result;
	}

	@Override
	/**
	 * Adds two number. Returns result as Instance of Integer.
	 */
	public Object doOperation(double number1, int number2) {

		Double result = number1 + number2;
		return result;
	}

}
