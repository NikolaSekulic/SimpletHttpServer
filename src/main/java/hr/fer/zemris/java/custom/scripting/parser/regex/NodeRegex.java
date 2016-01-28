package hr.fer.zemris.java.custom.scripting.parser.regex;

/**
 * Class NodeRegex is used for generating regular expressions for matching+
 * 
 * @author Nikola Sekulić
 * 
 */
public class NodeRegex {

	/**
	 * Regular expression that present start tag in script
	 */
	private static final String tagOpener = "(?<!\\Q\\\\E)(\\{)(\\s*)(\\$)(\\s*)";

	/**
	 * Regular expression for end tag ins script
	 */
	private static final String tagCloser = "(\\s*)(\\$)(\\s*)(\\})";

	/**
	 * Generate regular expression for echo expression. Echo expression starts
	 * with "{$=" tag and ends with "$}" tag. Between characters in start tag
	 * end end tag, empty characters are ignorable. Between start tag and end
	 * can be anything except '{' character. If '{' character is between tags,
	 * it has to be inside string token.
	 * 
	 * @return regular expression for echo expression
	 */
	public static String getEchoNodeRegex() {

		String regex = String.format(
				"(%s)(\\s*)(\\=)(\\s*)(((%s)|([^\\}]*))*)(%s)", tagOpener,
				TokenRegex.getStringRegex(), tagCloser);

		return regex;
	}

	/**
	 * Generate regular expression for end expression. End expression starts
	 * with "{$" tag and ends with "$}" tag. Between characters in start tag end
	 * end tag, empty characters are ignorable. Between start tag and end tag
	 * "END" substring is placed. "END" substring is case-insensitive.
	 * 
	 * @return regular expression for end expression
	 */
	public static String getEndNodeRegex() {
		String regex = String.format("(%s)(\\s*)[Ee][Nn][Dd](\\s*)(%s)",
				tagOpener, tagCloser);
		return regex;
	}

	/**
	 * Generate regular expression for for loop expression. For loop expression
	 * starts with "{$FOR" tag and ends with "$}" tag. Between characters in
	 * start tag end end tag, empty characters are ignorable, except substring
	 * "FOR". "FOR" substring in start tag is case-insensitive. Between start
	 * tag and end can be anything except '{' character. If '{' character is
	 * between tags, it has to be in side string token.
	 * 
	 * @return regular expression for for loop expression
	 */
	public static String getForLoopNodeRegex() {

		String regex = String.format(
				"(%s)(\\s*)[Ff][Oo][Rr](\\s*)(((%s)|([^\\}]*))*)(%s)",
				tagOpener, TokenRegex.getStringRegex(), tagCloser);

		return regex;
	}

	/**
	 * Generate regular expression for text in document that is not part of
	 * expression with tag. If text contains '{' character, that character has
	 * to be escaped with '\' character.
	 * 
	 * @return regular expression for text
	 */
	public static String getTextNodeRegex() {

		String regex = "((([^\\{])|((?<=\\Q\\\\E)(\\{)))+)";

		return regex;
	}

	/**
	 * Get regular expression that matches any node in document.
	 * 
	 * @return regular expression
	 */
	public static String getNodeRegex() {

		String regex = String.format("((%s)|(%s)|(%s)|(%s))",
				getEchoNodeRegex(), getEndNodeRegex(), getForLoopNodeRegex(),
				getTextNodeRegex());

		return regex;
	}
}
