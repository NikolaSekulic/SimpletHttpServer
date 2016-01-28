package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Class TokenConstantDouble represents double constant in script.
 * 
 * @author Nikola Sekulić
 *
 */
public class TokenConstantDouble extends Token {

	/**
	 * constant value
	 */
	private double value;

	/**
	 * Constructs TokenConstantDouble token.
	 * 
	 * @param value
	 *            value of decimal constant
	 */
	public TokenConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * Gets value of decimal constant.
	 * 
	 * @return value of decimal constant.
	 */
	public double getValue() {
		return value;
	}

	@Override
	public String asText() {
		return Double.toString(value);
	}

	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visitConstantDouble(this);
	}

}
