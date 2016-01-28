package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Class TokenConstantInteger represents integer constant in script.
 * 
 * @author Nikola Sekulić
 *
 */
public class TokenConstantInteger extends Token {

	/**
	 * Constant value.
	 */
	private int value;

	/**
	 * Constructs TokenConstantInteger token.
	 * 
	 * @param value
	 *            value of integer constant
	 */
	public TokenConstantInteger(int value) {
		this.value = value;
	}

	/**
	 * Gets value of integer constant.
	 * 
	 * @return value of integer constant.
	 */
	public int getValue() {
		return value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}

	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visitConstatnInteger(this);
	}
}
