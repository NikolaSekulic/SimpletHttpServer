package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Class TokenFunction represents function from script.
 * 
 * @author Nikola Sekulić
 * 
 */
public class TokenFunction extends Token {

	/**
	 * Name of function
	 */
	private String name;

	/**
	 * Constructs TokenFunction token.
	 * 
	 * @param name
	 *            name of function in script (without prefix @)
	 */
	public TokenFunction(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of function in script (without prefix @).
	 * 
	 * @return name of function.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String asText() {
		return "@" + name;
	}

	@Override
	public void accept(ITokenVisitor visitor) {
		visitor.visitFunction(this);
	}

}
