package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Class Token represents one token from script.
 * 
 * @author Nikola Sekulić
 *
 */
public abstract class Token {

	/**
	 * Returns token as text in script.
	 * 
	 * @return token as text in script.
	 */
	public String asText() {
		return "";
	}

	/**
	 * Accepts visitor that processes token.
	 * 
	 * @param visitor
	 *            visitor to accepted
	 */
	public abstract void accept(ITokenVisitor visitor);
}
