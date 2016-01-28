package hr.fer.zemris.java.custom.scripting.exec.operations;

/**
 * Implementation of {@link IOperation} where operand are Objects that can
 * represents numbers. Integer object presents integer number, Double object
 * presents double number. String can presents integer or double number.
 *
 * @author Nikola Sekulić
 *
 */
public abstract class Operation implements IOperation {

	/**
	 * Executes operation between two integers.
	 *
	 * @param number1
	 *            first integer value
	 * @param number2
	 *            second integer value
	 * @return result of operation
	 */
	public abstract Object doOperation(int number1, int number2);

	/**
	 * Executes operation between two doubles.
	 *
	 * @param number1
	 *            first double value
	 * @param number2
	 *            second double value
	 * @return result of operation
	 */
	public abstract Object doOperation(double number1, double number2);

	/**
	 * Executes operation between integer and double.
	 *
	 * @param number1
	 *            integer value
	 * @param number2
	 *            double value
	 * @return result of operation
	 */
	public abstract Object doOperation(int number1, double number2);

	/**
	 * Executes operation between double and integer.
	 *
	 * @param number1
	 *            double value
	 * @param number2
	 *            integer value
	 * @return result of operation
	 */
	public abstract Object doOperation(double number1, int number2);

	@Override
	/**
	 * Executes operation between provided numbers.
	 * Throws RuntimeException if operands cannot be presented as numbers.
	 */
	public Object getResult(Object operand1, Object operand2) {

		operand1 = ValueConverters.converteValue(operand1);
		operand2 = ValueConverters.converteValue(operand2);

		if ((operand1 instanceof Integer) && (operand2 instanceof Integer)) {

			int o1 = (Integer) operand1;
			int o2 = (Integer) operand2;
			return doOperation(o1, o2);

		} else if ((operand1 instanceof Double) && (operand2 instanceof Double)) {

			double o1 = (Double) operand1;
			double o2 = (Double) operand2;
			return doOperation(o1, o2);

		} else if ((operand1 instanceof Integer)
				&& (operand2 instanceof Double)) {

			int o1 = (Integer) operand1;
			double o2 = (Double) operand2;
			return doOperation(o1, o2);

		} else if ((operand1 instanceof Double)
				&& (operand2 instanceof Integer)) {

			double o1 = (Double) operand1;
			int o2 = (Integer) operand2;
			return doOperation(o1, o2);

		}

		throw new RuntimeException("Unknown operation for "
				+ operand1.getClass() + " and " + operand2.getClass());

	}

}
