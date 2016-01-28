package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Class TokenVariable represents variable from script.
 * 
 * @author Nikola Sekulić
 *
 */
public class TokenVariable extends Token {

	/**
	 * Name of variable
	 */
	private String name;

	/**
	 * Constructs TokenVariable.
	 * 
	 * @param name
	 *            name of the variable.
	 */
	public TokenVariable(String name) {
		this.name = name;
	}

	/**
	 * Gets name of the variable.
	 * 
	 * @return name of variable
	 */
	public String getName() {
		return name;
	}

	@Override
	public String asText() {
		return name;
	}

	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visitVariable(this);
	}
}
