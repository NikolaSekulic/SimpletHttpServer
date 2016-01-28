package hr.fer.zemris.java.custom.scripting.parser;

import java.util.regex.Pattern;

import hr.fer.zemris.java.custom.scripting.parser.regex.TokenRegex;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 * Class TokenProcessor provides service for construction Token from string
 * representation of token.
 * 
 * @author Nikola Sekulić
 * 
 */
public class TokenProcessor {

	/**
	 * Constructs TokenConstantDouble from string representation. It construct
	 * token if input string represents double constant like Java source code.
	 * 
	 * @param tokenRepresentation
	 *            string representation of double constant
	 * @return TokenConstatnDouble if input string matches double constant, null
	 *         otherwise.
	 */
	public static TokenConstantDouble processConstantDoubleToken(
			String tokenRepresentation) {

		if (tokenRepresentation == null) {
			return null;
		}

		if (Pattern.matches(TokenRegex.getDoubleConstantRegex(),
				tokenRepresentation)) {
			tokenRepresentation = tokenRepresentation.replaceAll("_", "");
			double value = Double.parseDouble(tokenRepresentation);
			TokenConstantDouble token = new TokenConstantDouble(value);
			return token;
		}

		return null;
	}

	/**
	 * Constructs TokenConstantInteger from string representation. It constructs
	 * token if input string represents integer constant like Java source code,
	 * including integer representation in binary, octal, decimal and
	 * hexadecimal radix.
	 * 
	 * @param tokenRepresentation
	 *            string representation of integer constant
	 * @return TokenConstatnInteger if input string matches integer constant,
	 *         null otherwise.
	 */
	public static TokenConstantInteger processConstantIntegerToken(
			String tokenRepresentation) {

		if (tokenRepresentation == null) {
			return null;
		}

		if (Pattern.matches(TokenRegex.getIntegerConstantRegex(),
				tokenRepresentation)) {

			int radix = 10;
			boolean negative = false;

			tokenRepresentation = tokenRepresentation.replaceAll("_", "");

			char firstChar = tokenRepresentation.charAt(0);
			if (firstChar == '-') {
				negative = true;
				tokenRepresentation = tokenRepresentation.substring(1,
						tokenRepresentation.length());
			} else if (firstChar == '+') {
				tokenRepresentation = tokenRepresentation.substring(1,
						tokenRepresentation.length());
			}

			if (Pattern.matches("\\A0[Xx].*", tokenRepresentation)) {
				tokenRepresentation = tokenRepresentation.replaceFirst(
						"\\A0[Xx]", "");
				radix = 16;
			} else if (Pattern.matches("\\A0[Bb].*", tokenRepresentation)) {
				tokenRepresentation = tokenRepresentation.replaceFirst(
						"\\A0[Bb]", "");
				radix = 2;
			} else if (Pattern.matches("\\A0[0-7]+", tokenRepresentation)) {
				tokenRepresentation = tokenRepresentation.replaceFirst("\\A0",
						"");
				radix = 8;
			}

			int value = Integer.parseInt(tokenRepresentation, radix);

			if (negative) {
				value *= -1;
			}

			TokenConstantInteger token = new TokenConstantInteger(value);
			return token;
		}

		return null;
	}

	/**
	 * Constructs TokenFunction from string representation. It constructs token
	 * if input string represents function. First character of function is '@',
	 * second character has to be letter. All others character has to be letter,
	 * digit or underscore.
	 * 
	 * @param tokenRepresentation
	 *            string representation of function.
	 * @return TokenFunction if input string matches function, null otherwise.
	 */
	public static TokenFunction processFunctionToken(String tokenRepresentation) {

		if (tokenRepresentation == null) {
			return null;
		}

		if (Pattern.matches(TokenRegex.getFunctionRegex(), tokenRepresentation)) {
			TokenFunction token = new TokenFunction(
					tokenRepresentation.substring(1,
							tokenRepresentation.length()));
			return token;
		}

		return null;
	}

	/**
	 * Constructs TokenOperator from string representation. It constructs token
	 * if input string represents operator in document. Operators are: +, -, *,
	 * /, %, =.
	 * 
	 * @param tokenRepresentation
	 *            string representation of operator.
	 * @return TokenOperator if input string matches operator, null otherwise.
	 */
	public static TokenOperator processOperatorToken(String tokenRepresentation) {

		if (tokenRepresentation == null) {
			return null;
		}

		if (Pattern.matches(TokenRegex.getOperatorRegex(), tokenRepresentation)) {
			TokenOperator token = new TokenOperator(tokenRepresentation);
			return token;
		}

		return null;
	}

	/**
	 * Constructs TokenString from string representation. It constructs token if
	 * input string represents string in document. String begins and ends with
	 * '"' character. If between begin and end of string is more '"' characters,
	 * they have to be escaped with '\' character, like in this example:
	 * "string\"string". Only 'n', 'r', 't', '"' and '\' characters can be
	 * escaped with, '\' character. Characters '"' and '\' have to be escaped
	 * with '\'.
	 * 
	 * @param tokenRepresentation
	 *            string representation of string.
	 * @return TokenString if input text matches string, null otherwise.
	 */
	public static TokenString processStringToken(String tokenRepresentation) {

		if (tokenRepresentation == null) {
			return null;
		}

		if (Pattern.matches(TokenRegex.getStringRegex(), tokenRepresentation)) {
			TokenString token = new TokenString(tokenRepresentation);
			return token;
		}

		return null;
	}

	/**
	 * Constructs TokenVariable from string representation. It construct token
	 * if input string represents variable. First character of variable has to
	 * be letter. Other characters have to be letter, digit or underscore.
	 * 
	 * @param tokenRepresentation
	 *            string representation of variable.
	 * @return TokenVariable if input string matches variable, null otherwise.
	 */
	public static TokenVariable processVariableToken(String tokenRepresentation) {

		if (tokenRepresentation == null) {
			return null;
		}

		if (Pattern.matches(TokenRegex.getVariableRegex(), tokenRepresentation)) {
			TokenVariable token = new TokenVariable(tokenRepresentation);
			return token;
		}

		return null;
	}

	/**
	 * Constructs subclass of Token from string representation of token.
	 * 
	 * @param tokenRepresentation
	 *            String representation of token.
	 * @return TokenConstatnDouble if input string matches double constant.
	 *         TokenConstatnInteger if input string matches integer constant.
	 *         TokenFunction if input string matches function. TokenOperator if
	 *         input string matches operator. TokenString if input text matches
	 *         string. TokenVariable if input string matches variable. null if
	 *         input string does not represents any token.
	 */
	public static Token processToken(String tokenRepresentation) {

		Token token = null;

		if (tokenRepresentation == null) {
			return null;
		}

		token = processConstantDoubleToken(tokenRepresentation);

		if (token == null) {
			token = processConstantIntegerToken(tokenRepresentation);
		}

		if (token == null) {
			token = processFunctionToken(tokenRepresentation);
		}

		if (token == null) {
			token = processOperatorToken(tokenRepresentation);
		}

		if (token == null) {
			token = processStringToken(tokenRepresentation);
		}

		if (token == null) {
			token = processVariableToken(tokenRepresentation);
		}

		return token;
	}

}
