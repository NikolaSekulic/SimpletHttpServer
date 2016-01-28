package hr.fer.zemris.java.custom.collections;

/**
 * Subclass of {@link RuntimeException}. Thrown by methods in the ObjectStack
 * class to indicate that the stack is empty.
 * 
 * @author Nikola Sekulić
 * 
 */
public class EmptyStackException extends RuntimeException {

	/** Serialization ID **/
	private static final long serialVersionUID = 6307697858263274779L;

	/**
	 * Creates new EmptyStackException
	 * 
	 * @param arg0
	 *            error message
	 */
	public EmptyStackException(String arg0) {
		super(arg0);

	}

}
