package hr.fer.zemris.java.custom.scripting.exec.operations;

import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that converts Object to number representation.
 *
 * @author Nikola Sekulić
 *
 */
public class ValueConverters {

	/**
	 * Converters stored in map. Key of converter is {@link Class} of object
	 * that can be converted to number.
	 */
	private static Map<Class<?>, IValueConverter> converters;

	/**
	 * Converters initialization.
	 */
	static {
		ValueConverters.converters = new HashMap<Class<?>, IValueConverter>();

		ValueConverters.converters.put(Integer.class, (value) -> value);

		ValueConverters.converters.put(Double.class, (value) -> value);

		ValueConverters.converters.put(String.class,
				(value) -> {
					String input = (String) value;
					Object output = null;

					try {
						output = Integer.valueOf((String) value);
						return output;
					} catch (NumberFormatException ignorable) {
					}

					try {
						output = Double.valueOf((String) value);
					} catch (NumberFormatException ignorable) {
						System.out.println(input.getClass());
						System.out.println(value);
						throw new RuntimeException("Cannot parse number from: "
								+ input);
					}

					return output;
				});

	}

	/**
	 * Converts object to number. If provided object is instance of Integer or
	 * Integer 0 is returned if provided number is <code>null</code>. Double,
	 * same object is returned. If provided object is instance of {@link String}
	 * , Integer object is returned if string can be parsed to integer number,
	 * Double object is returned if string can be parsed to Double. In all other
	 * cases {@link RuntimeException} is thrown.
	 *
	 * @param value
	 *            value to be converted
	 * @return Double or Integer number
	 *
	 */
	public static Object converteValue(Object value) {

		if (value == null) {
			return Integer.valueOf(0);
		}

		if (value instanceof ValueWrapper) {
			value = ((ValueWrapper) value).getValue();
		}

		IValueConverter converter = null;
		converter = ValueConverters.converters.get(value.getClass());

		if (converter == null) {
			throw new RuntimeException("Class " + value.getClass()
					+ " cannot be connverted to number");
		}
		return converter.convertValue(value);
	}

}
