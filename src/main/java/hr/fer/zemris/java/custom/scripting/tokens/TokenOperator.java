package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Class TokenOperator represents operator from script.
 * 
 * @author Nikola Sekulić
 *
 */
public class TokenOperator extends Token {

	/**
	 * Operator's symbol.
	 */
	private String symbol;

	/**
	 * Constructs TokenOperator
	 * 
	 * @param symbol
	 *            symbol of operator as String.
	 */
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Constructs TokenOperator
	 * 
	 * @param symbol
	 *            symbol of operator as char.
	 */
	public TokenOperator(char symbol) {
		this.symbol = "" + symbol;
	}

	/**
	 * Gets operator representation.
	 * 
	 * @return operator representation.
	 */
	public String getSymbol() {
		return symbol;
	}

	@Override
	public String asText() {
		return symbol;
	}

	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visitOperator(this);
	}
}
