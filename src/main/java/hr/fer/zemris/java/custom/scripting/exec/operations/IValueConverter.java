package hr.fer.zemris.java.custom.scripting.exec.operations;

@FunctionalInterface
/**
 * Interface for object conversion.
 * @author Nikola Sekulić
 *
 */
public interface IValueConverter {

	/**
	 * Converts provided object to another object
	 *
	 * @param value
	 *            value to be converted
	 * @return converted value
	 */
	Object convertValue(Object value);

}
