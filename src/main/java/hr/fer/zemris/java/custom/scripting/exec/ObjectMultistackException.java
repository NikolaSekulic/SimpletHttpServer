package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Exception which is thrown when error in {@link ObjectMultistack} occurs.
 *
 * @author Nikola Sekulić
 *
 */
public class ObjectMultistackException extends RuntimeException {

	/** serial id **/
	private static final long serialVersionUID = 627016337297059572L;

	/**
	 * Constructor.
	 *
	 * @param message
	 *            error description
	 */
	public ObjectMultistackException(String message) {

		super(message);
	}

}
