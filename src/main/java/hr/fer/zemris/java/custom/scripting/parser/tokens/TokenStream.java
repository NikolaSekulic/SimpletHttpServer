package hr.fer.zemris.java.custom.scripting.parser.tokens;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.custom.scripting.parser.regex.TokenRegex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TokenStream class is used for get String representation of token from String
 * that contains more tokens.
 * 
 * @author Nikola Sekulić
 * 
 */
public class TokenStream {

	/**
	 * Buffer with text that represents tokens
	 */
	String tokenBuffer;

	/**
	 * Pattern for token recognition.
	 */
	Pattern pattern;

	/**
	 * Constructs token buffer form String that contains tokens
	 * 
	 * @param tokenBuffer
	 *            String with tokens
	 * @throws NullPointerException
	 *             if tokenBuffer is null
	 */
	public TokenStream(String tokenBuffer) throws NullPointerException {

		if (tokenBuffer == null) {
			throw new NullPointerException(
					"TokenStream cannot be constucted from null!");
		}

		this.tokenBuffer = tokenBuffer.trim();
		pattern = Pattern.compile(TokenRegex.getTokenRegex());
	}

	/**
	 * Gets next String representation of one token from buffer.
	 * 
	 * @return string that represents one token. null if buffer is empty.
	 * @throws SmartScriptParserException
	 *             if buffer contains substring that is not token.
	 */
	public String nextToken() throws SmartScriptParserException {

		tokenBuffer = tokenBuffer.trim();
		if (tokenBuffer.isEmpty()) {
			return null;
		}

		Matcher matcher = pattern.matcher(tokenBuffer);

		if (matcher.lookingAt()) {
			String token = tokenBuffer
					.substring(matcher.start(), matcher.end());
			tokenBuffer = tokenBuffer.substring(matcher.end(),
					tokenBuffer.length());

			return token;
		} else {
			throw new SmartScriptParserException(
					"Cannot parse next token from: \r\n" + tokenBuffer);
		}
	}

}
