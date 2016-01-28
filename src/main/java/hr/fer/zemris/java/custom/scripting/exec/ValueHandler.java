package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Service for arithmetic operations with objects that can represent number.
 * 
 * @author Nikola Sekulić
 * 
 */
public class ValueHandler {

	/**
	 * Type of operation.
	 * 
	 * @author Nikola Sekulić
	 * 
	 */
	public static enum Operation {

		/**
		 * Addition
		 */
		ADD,

		/**
		 * Multiplication
		 */
		MUL,

		/**
		 * Subtraction
		 */
		SUB,

		/**
		 * Division
		 */
		DIV,

		/**
		 * Comparison
		 */
		COM
	}

	/**
	 * Gets integer if object can presents integer value.
	 * 
	 * @param value
	 *            object that represents integer
	 * 
	 * @return If value is null, returns 0. If value is instance of Integer,
	 *         value from that integer. If value is instance of String, tries
	 *         parse integer from string and on success returns that integer. In
	 *         all other cases returns null.
	 */
	private static Integer getInteger(Object value) {

		if (value == null) {
			return 0;
		}

		if (value instanceof ValueWrapper) {
			value = ((ValueWrapper) value).getValue();
		}

		if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof String) {
			try {
				final int i = Integer.parseInt((String) value);
				return i;
			} catch (final NumberFormatException ignorable) {
			}
		}

		return null;
	}

	/**
	 * Gets double value if object can presents decimal number.
	 * 
	 * @param value
	 *            object that represents decimal number
	 * 
	 * @return If value is instance of Double or Integer, value from Double or
	 *         Integer. If value is instance of String, tries parse double value
	 *         from string and on success returns that double value. In all
	 *         other cases returns null.
	 */
	private static Double getDouble(Object value) {

		if (value == null) {
			return 0.0;
		}

		if (value instanceof ValueWrapper) {
			value = ((ValueWrapper) value).getValue();
		}

		if (value instanceof Double) {
			return (Double) value;
		} else if (value instanceof String) {
			try {
				final double d = Double.parseDouble((String) value);
				return d;
			} catch (final NumberFormatException ignorable) {
			}
		} else if (value instanceof Integer) {
			return (double) (Integer) value;
		}

		return null;
	}

	/**
	 * Returns object of Integer or Double class, if object can represents
	 * integer or decimal value.
	 * 
	 * @param value
	 *            object that represents number, object can be ValueWrapper
	 *            instance.
	 * @return Instance of Integer with value of number if object represents
	 *         integer number. Instance of Double with value of number if object
	 *         represents decimal number. If value can represents integer it
	 *         also can represents decimal number. In that case instance of
	 *         Integer is returned. If value is null, 0.0 is returned.
	 * 
	 * @throws RuntimeException
	 *             if value cannot represent number.
	 */
	public static Object getOperationValue(Object value) {

		Object opValue = getInteger(value);

		if (opValue == null) {
			opValue = getDouble(value);
		}

		if (opValue == null) {
			throw new RuntimeException(
					"Cannot parse double or integer from provided object");
		}

		return opValue;

	}

	/**
	 * Checks if result of mathematical operation of two objects is Integer
	 * instance.
	 * 
	 * @param value1
	 *            first object
	 * @param value2
	 *            second object
	 * @return true if and only if value1 and value2 are instances of Integer
	 */
	private static boolean isResultInteger(Object value1, Object value2) {

		value1 = getOperationValue(value1);
		value2 = getOperationValue(value2);

		if (value1 instanceof Integer && value2 instanceof Integer) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if result of mathematical operation of two objects is Double
	 * instance.
	 * 
	 * @param value1
	 *            first object
	 * @param value2
	 *            second object
	 * @return true if and only if one value is Instance of Double and other is
	 *         instance of Double or Integer
	 */
	private static boolean isResultDouble(Object value1, Object value2) {

		value1 = getOperationValue(value1);
		value2 = getOperationValue(value2);

		if (value1 instanceof Double) {
			if (value2 instanceof Integer || value2 instanceof Double) {
				return true;
			}
		}

		if (value2 instanceof Double) {
			if (value1 instanceof Integer || value1 instanceof Double) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Performs operation defined with operation type. If operand of operation
	 * is null, it is interpreted as integer value 0. If it is instance of
	 * String, operand is interpreted like number of integer or decimal number
	 * if that number can be parsed from String. If value is instance of Integer
	 * it is interpreted as integer number, if it is instance of Double, it is
	 * interpreted as decimal number. Result of operation is Instance of Integer
	 * if both operands can be interpreted as integer number. It is instance of
	 * Double if at least one operand cannot be interpreted as integer value,
	 * but can be interpreted as decimal number.
	 * 
	 * @param value1
	 *            first operand
	 * @param value2
	 *            second operand
	 * @param o
	 *            operation type
	 * @return result of operation
	 * 
	 * @throws RuntimeException
	 *             if one of operand cannot be represented as number.
	 * 
	 * @throws ArithmeticException
	 *             if operation type is DIV, both operands are integer number,
	 *             and value of second operand is zero.
	 */
	public static Object operation(Object value1, Object value2, Operation o) {

		final boolean resultIsInt = isResultInteger(value1, value2);
		final boolean resultIsDouble = isResultDouble(value1, value2);

		switch (o) {
			case ADD :
				if (resultIsInt) {
					return getInteger(value1) + getInteger(value2);
				}

				if (resultIsDouble) {
					return getDouble(value1) + getDouble(value2);
				}
			case SUB :
				if (resultIsInt) {
					return getInteger(value1) - getInteger(value2);
				}

				if (resultIsDouble) {
					return getDouble(value1) - getDouble(value2);
				}
			case MUL :
				if (resultIsInt) {
					return getInteger(value1) * getInteger(value2);
				}

				if (resultIsDouble) {
					return getDouble(value1) * getDouble(value2);
				}
			case DIV :
				if (resultIsInt) {
					return getInteger(value1) / getInteger(value2);
				}

				if (resultIsDouble) {
					return getDouble(value1) / getDouble(value2);
				}
			case COM :
				if (resultIsInt) {
					return getInteger(value1).compareTo(getInteger(value2));
				}

				if (resultIsDouble) {
					return getDouble(value1).compareTo(getDouble(value2));
				}

		}

		throw new RuntimeException();
	}

}
