package hr.fer.zemris.java.custom.scripting.parser.regex;

import java.util.regex.Pattern;

/**
 * Class TokenRegex is used for generating regular expressions for matching
 * tokens. Regular expressions are generated in {@link Pattern} format.
 * 
 * @author Nikola Sekulić
 * 
 */
public class TokenRegex {

	/**
	 * Regular expression for integer constant
	 */
	private static final String simpleIntDecimalRegex = "[0-9]+";

	/**
	 * Regular expression for integer constant in hexadecimal radix.
	 */
	private static final String simpleIntHexRegex = "[0-9A-Fa-f]+";

	/**
	 * Regular expression for integer constant in octal radix
	 */
	private static final String simpleIntOctalRegex = "[0-7]+";

	/**
	 * Regulat expression for integer constant in binary radix.
	 */
	private static final String simpleIntBinaryRegex = "[01]+";

	/**
	 * Generates regular expression for integer constant. Regular expression is
	 * generated in {@link Pattern} format. Regular expression matches if input
	 * string represents integer constant like java source code, including
	 * constants in binary, octal, decimal and hexadecimal format.
	 * 
	 * @return regular expression
	 */
	public static String getIntegerConstantRegex() {

		String intDecimalRegex = String.format("(%s)((_(0*%s))*)",
				simpleIntDecimalRegex, simpleIntDecimalRegex);
		String intHexRegex = String.format("(0[xX])(%s)((_%s)*)",
				simpleIntHexRegex, simpleIntHexRegex);
		String intOctalRegex = String.format("(0)(%s)((_%s)*)",
				simpleIntOctalRegex, simpleIntOctalRegex);
		String intBinaryRegex = String.format("(0[bB])(%s)((_%s)*)",
				simpleIntBinaryRegex, simpleIntBinaryRegex);

		String regex = String.format(
				"([\\-\\+]?)((%s)|(%s)|(%s)|(%s)|((0+)(?!\\d)))",
				intBinaryRegex, intHexRegex, intOctalRegex, intDecimalRegex);

		return regex;
	}

	/**
	 * Generates regular expression for double constant. Regular expression is
	 * generated in {@link Pattern} format. Regular expression matches if input
	 * string represents double constant like java source code.
	 * 
	 * @return regular expression
	 */
	public static String getDoubleConstantRegex() {

		String doublePart = "\\d+(_\\d+)*";
		String exampleOne = String.format("(%s)*\\.(%s)+", doublePart,
				doublePart);
		String exampleTwo = String.format("(%s)+\\.(%s)*", doublePart,
				doublePart);
		String doubleWithoutExponent = String.format("(%s)|(%s)", exampleOne,
				exampleTwo);
		String doubleWithExponent = String.format(
				"((%s)|(%s))([eE]([\\+\\-]?)(%s))", doubleWithoutExponent,
				doublePart, doublePart);
		String regex = String.format("[\\-\\+]?((%s)|(%s))",
				doubleWithExponent, doubleWithoutExponent);
		return regex;
	}

	/**
	 * Generates regular expression for function. First character of function is
	 * '@', second character has to be letter. All others character has to be
	 * letter, digit or underscore.
	 * 
	 * @return regular expression
	 */
	public static String getFunctionRegex() {
		return "@\\p{L}[\\p{L}_\\d]*";
	}

	/**
	 * Generates regular expression for operator. Operators are: +, -, *, /, %,
	 * =. If operator is + or -, next sequence in string must not be integer or
	 * decimal constant.
	 * 
	 * @return regular expression
	 */
	public static String getOperatorRegex() {

		String regex = String
				.format("(((\\*)|(\\=)|(/)|(%%))|(((\\+)|(\\-))(((?=[\\+\\-])|(?!((%s)|(%s)))))))",
						getIntegerConstantRegex(), getDoubleConstantRegex());

		return regex;
	}

	/**
	 * Generate regular expression for string. String begins and ends with '"'
	 * character. If between begin and end of string is more '"' characters,
	 * they have to be escaped with '\' character, like in this example:
	 * "string\"string". Only 'n', 'r', 't', '"' and '\' characters can be
	 * escaped with, '\' character. Characters '"' and '\' have to be escaped
	 * with '\' character.
	 * 
	 * @return regular expression
	 */
	public static String getStringRegex() {

		String notEsapingChars = "[^\\Q\\\"\\E]";
		String escapingChars = "[\\Qnrt\\\"\\E]";
		String regex = String.format("\"((%s)|(\\Q\\\\E(%s)))*\"",
				notEsapingChars, escapingChars);

		return regex;
	}

	/**
	 * Generate regular expression for variable. First character of variable has
	 * to be letter. Other characters have to be letter, digit or underscore.
	 * 
	 * @return regular expression
	 */
	public static String getVariableRegex() {
		return "\\p{L}[\\p{L}_\\d]*";
	}

	/**
	 * Generate regular expression for that matches any token.
	 * 
	 * @return regular expression
	 */
	public static String getTokenRegex() {
		String regex = String.format("(%s)|(%s)|(%s)|(%s)|(%s)|(%s)",
				getDoubleConstantRegex(), getIntegerConstantRegex(),
				getFunctionRegex(), getOperatorRegex(), getStringRegex(),
				getVariableRegex());
		return regex;
	}

}
