package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Signals that an error has been reached while parsing document.
 * 
 * @author Nikola Sekulić
 * 
 */
public class SmartScriptParserException extends RuntimeException {

	/** serialization id **/
	private static final long serialVersionUID = -7873703860495022568L;

	/**
	 * Creates new SmartScriptParserException
	 * 
	 * @param message
	 *            error message
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

	/**
	 * Creates new SmartScriptParserException
	 * 
	 * @param message
	 *            error message
	 * @param cause
	 *            cause of exception
	 */
	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);

	}

}
