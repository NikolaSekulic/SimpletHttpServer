package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.exec.operations.Addition;
import hr.fer.zemris.java.custom.scripting.exec.operations.Comparison;
import hr.fer.zemris.java.custom.scripting.exec.operations.Division;
import hr.fer.zemris.java.custom.scripting.exec.operations.IOperation;
import hr.fer.zemris.java.custom.scripting.exec.operations.Multiplication;
import hr.fer.zemris.java.custom.scripting.exec.operations.Subtraction;

/**
 * ValueWrapper is value container. It contains additional method for value
 * manipulation such as increment, decrement, multiply, divide and numCompare.
 *
 * @author Nikola Sekulić
 *
 */
public class ValueWrapper {

	/** value stored in wrapper **/
	private Object value;

	/**
	 * Constructor.
	 *
	 * @param value
	 *            value to be stored
	 */
	public ValueWrapper(Object value) {

		super();
		this.value = value;
	}

	/**
	 * Value getter.
	 *
	 * @return the value
	 */
	public Object getValue() {

		return value;
	}

	/**
	 * Value setter.
	 *
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {

		this.value = value;
	}

	/**
	 * Increments stored value with provided value, if values can be presented
	 * as numbers. Value can be number if it is instance of {@link Integer},
	 * {@link Double} or {@link String} that can be parsed to integer or double.
	 * <code>null</code> values are presented as integer number 0. If any value
	 * can be presented as double, new value in wrapper is instance of
	 * {@link Double}. If both values can be presented as Integer, result in
	 * wrapper will be stored as {@link Integer}.
	 *
	 * @param incValue
	 *            value to be incremented with
	 * @throws RuntimeException
	 *             if stored value or provided value can not be presented as
	 *             number
	 */
	public void increment(Object incValue) {

		IOperation operation = new Addition();
		Object result = operation.getResult(getValue(), incValue);
		setValue(result);
	}

	/**
	 * Decrements stored value with provided value, if values can be presented
	 * as numbers. Value can be number if it is instance of {@link Integer},
	 * {@link Double} or {@link String} that can be parsed to integer or double.
	 * <code>null</code> values are presented as integer number 0. If any value
	 * can be presented as double, new value in wrapper is instance of
	 * {@link Double}. If both values can be presented as Integer, result in
	 * wrapper will be stored as {@link Integer}.
	 *
	 * @param decValue
	 *            value to be decremented with
	 * @throws RuntimeException
	 *             if stored value or provided value can not be presented as
	 *             number
	 */
	public void decrement(Object decValue) {

		IOperation operation = new Subtraction();
		Object result = operation.getResult(getValue(), decValue);
		setValue(result);
	}

	/**
	 * Multiplies stored value with provided value, if values can be presented
	 * as numbers. Value can be number if it is instance of {@link Integer},
	 * {@link Double} or {@link String} that can be parsed to integer or double.
	 * <code>null</code> values are presented as integer number 0. If any value
	 * can be presented as double, new value in wrapper is instance of
	 * {@link Double}. If both values can be presented as Integer, result in
	 * wrapper will be stored as {@link Integer}.
	 *
	 * @param mulValue
	 *            value to be multiplied with
	 * @throws RuntimeException
	 *             if stored value or provided value can not be presented as
	 *             number
	 */
	public void multiply(Object mulValue) {

		IOperation operation = new Multiplication();
		Object result = operation.getResult(getValue(), mulValue);
		setValue(result);
	}

	/**
	 * Divides stored value with provided value, if values can be presented as
	 * numbers. Value can be number if it is instance of {@link Integer},
	 * {@link Double} or {@link String} that can be parsed to integer or double.
	 * <code>null</code> values are presented as integer number 0. If any value
	 * can be presented as double, new value in wrapper is instance of
	 * {@link Double}. If both values can be presented as Integer, result in
	 * wrapper will be stored as {@link Integer}.
	 *
	 * @param divValue
	 *            value to be divided with
	 * @throws RuntimeException
	 *             if stored value or provided value can not be presented as
	 *             number
	 */
	public void divide(Object divValue) {

		IOperation operation = new Division();
		Object result = operation.getResult(getValue(), divValue);
		setValue(result);
	}

	/**
	 * Compares stored value with provided value, if values can be presented as
	 * numbers. Value can be number if it is instance of {@link Integer},
	 * {@link Double} or {@link String} that can be parsed to integer or double.
	 * <code>null</code> values are presented as integer number 0. If any value
	 * can be presented as double, new value in wrapper is instance of
	 * {@link Double}. If both values can be presented as Integer, result in
	 * wrapper will be stored as {@link Integer}.
	 *
	 * @param withValue
	 *            value to be compared with
	 * @return negative integer if stored number is lesser than provided, 0 if
	 *         stored number is equals to provided number, positive integer if
	 *         stored number is greater than provided number
	 * @throws RuntimeException
	 *             if stored value or provided value can not be presented as
	 *             number
	 */
	public int numCompare(Object withValue) {

		IOperation operation = new Comparison();
		Object result = operation.getResult(getValue(), withValue);
		return (Integer) result;
	}

}
