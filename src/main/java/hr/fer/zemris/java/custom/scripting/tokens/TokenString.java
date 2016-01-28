package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Class TokenString represents string in script.
 * 
 * @author Nikola Sekulić
 *
 */
public class TokenString extends Token {

	/**
	 * Value of string.
	 */
	private String value;

	/**
	 * Constructs TokenString
	 * 
	 * @param value
	 *            string from script with "\"" at the begin and end of string
	 */
	public TokenString(String value) {
		this.value = value;
	}

	/**
	 * Gets string representation.
	 * 
	 * @return returns String representation of token
	 */
	public String getValue() {
		return value.substring(1, value.length() - 1)
				.replaceAll("\\Q\\n\\E", "\n").replaceAll("\\Q\\r\\E", "\r");
	}

	@Override
	public String asText() {
		return value;
	}

	@Override
	public void accept(ITokenVisitor visitor) {

		visitor.visitString(this);
	}

}
