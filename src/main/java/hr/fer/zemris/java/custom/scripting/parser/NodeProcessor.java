package hr.fer.zemris.java.custom.scripting.parser;

import java.util.regex.Pattern;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.EndNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.regex.NodeRegex;
import hr.fer.zemris.java.custom.scripting.parser.tokens.TokenStream;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 * Class NodeProcessor provide the service that constructs Node from string
 * representation of Node in document.
 * 
 * @author Nikola Sekulić
 * 
 */
public class NodeProcessor {

	/**
	 * Regular expression for end of tag.
	 */
	private static String closeTagRegex = "((\\s*)\\$(\\s*)\\}(\\s*)\\z)";

	/**
	 * Regular expression for start tag.
	 */
	private static String openTagRegex = "(\\A(\\s*)\\{(\\s*)\\$(\\s*))";

	/**
	 * Construct Node from string representation of node.
	 * 
	 * @param nodeRepresentation
	 *            String representation of node.
	 * @return EchoNode if input presents echo node. EndNode if input presents
	 *         end node. ForLoopNode if input presents for loop node. TextNode
	 *         if input presents text node. null if input does not present any
	 *         node.
	 */
	public static Node processNode(String nodeRepresentation) {

		if (nodeRepresentation == null) {
			return null;
		}

		Node node = null;
		node = processEchoNode(nodeRepresentation);

		if (node == null) {
			node = processEndNode(nodeRepresentation);
		}
		if (node == null) {
			node = processForLoopNode(nodeRepresentation);
		}
		if (node == null) {
			node = processTextNode(nodeRepresentation);
		}

		return node;
	}

	/**
	 * Constructs EchoNode from string representation of node.
	 * 
	 * @param nodeRepresentation
	 *            String that contains node in document.
	 * @return EchoNode if string is representation of echo node, null otherwise
	 */
	public static EchoNode processEchoNode(String nodeRepresentation) {

		if (nodeRepresentation == null) {
			return null;
		}

		if (Pattern.matches(NodeRegex.getEchoNodeRegex(), nodeRepresentation)) {

			String tokensString = nodeRepresentation.replaceFirst(openTagRegex
					+ "(\\=\\s*)", "");
			tokensString = tokensString.replaceFirst(closeTagRegex, "");

			TokenStream tokenStream = new TokenStream(tokensString);

			String tokenRepresentation = null;
			ArrayBackedIndexedCollection tokens = new ArrayBackedIndexedCollection();

			while ((tokenRepresentation = tokenStream.nextToken()) != null) {
				tokens.add(TokenProcessor.processToken(tokenRepresentation));
			}

			EchoNode node = null;

			if (tokens.size() == 0) {
				node = new EchoNode();
			} else {
				Token[] tokensArray = new Token[tokens.size()];
				for (int i = 0; i < tokens.size(); i++) {
					tokensArray[i] = (Token) tokens.get(i);
				}
				node = new EchoNode(tokensArray);
			}

			return node;
		}

		return null;
	}

	/**
	 * Constructs EndNode from string representation of node.
	 * 
	 * @param nodeRepersentation
	 *            String that contains node in document.
	 * @return EndNode if string is representation of end node, null otherwise
	 */
	public static EndNode processEndNode(String nodeRepersentation) {

		if (nodeRepersentation == null) {
			return null;
		}

		if (Pattern.matches(NodeRegex.getEndNodeRegex(), nodeRepersentation)) {
			return new EndNode();
		}

		return null;
	}

	/**
	 * Constructs ForLoopNode from string representation of node.
	 * 
	 * @param nodeRepresentation
	 *            String that contains node in document.
	 * @return ForLoopNode if string is representation of for loop, null
	 *         otherwise.
	 * @throws SmartScriptParserException
	 *             if there is lexical or syntax error
	 */
	public static ForLoopNode processForLoopNode(String nodeRepresentation)
			throws SmartScriptParserException {

		if (nodeRepresentation == null) {
			return null;
		}

		if (Pattern
				.matches(NodeRegex.getForLoopNodeRegex(), nodeRepresentation)) {
			String tokensString = nodeRepresentation.replaceFirst(openTagRegex
					+ "([Ff][Oo][Rr](\\s*))", "");
			tokensString = tokensString.replaceFirst(closeTagRegex, "");

			TokenStream tokenStream = new TokenStream(tokensString);

			TokenVariable variable = TokenProcessor
					.processVariableToken(tokenStream.nextToken());

			if (variable == null) {
				String errorMessage = "Syntax error: ";
				errorMessage += "For loops must have variable: ";
				errorMessage += nodeRepresentation;
				throw new SmartScriptParserException(errorMessage);
			}

			Token start = TokenProcessor.processToken(tokenStream.nextToken());

			if (start == null) {
				String errorMessage = "Syntax error: ";
				errorMessage += "For loops must have start expression: ";
				errorMessage += nodeRepresentation;
				throw new SmartScriptParserException(errorMessage);
			}

			if (!(start instanceof TokenVariable
					|| start instanceof TokenConstantDouble
					|| start instanceof TokenConstantInteger || start instanceof TokenString)) {

				String errorMessage = "Start expression of for loop has to be constant or variable!";
				throw new SmartScriptParserException(errorMessage);

			}

			Token end = TokenProcessor.processToken(tokenStream.nextToken());

			if (end == null) {
				String errorMessage = "Syntax error: ";
				errorMessage += "For loops must have end expression: ";
				errorMessage += nodeRepresentation;
				throw new SmartScriptParserException(errorMessage);
			}

			if (!(end instanceof TokenVariable
					|| end instanceof TokenConstantDouble
					|| end instanceof TokenConstantInteger || end instanceof TokenString)) {

				String errorMessage = "end expression of for loop has to be constant or variable!";
				throw new SmartScriptParserException(errorMessage);

			}

			Token step = TokenProcessor.processToken(tokenStream.nextToken());

			if (step != null) {
				if (!(step instanceof TokenVariable
						|| step instanceof TokenConstantDouble
						|| step instanceof TokenConstantInteger || step instanceof TokenString)) {

					String errorMessage = "Step expression of for loop has to be constant or variable!";
					throw new SmartScriptParserException(errorMessage);

				}
			}

			if (tokenStream.nextToken() != null) {
				String errorMessage = "Syntax error: ";
				errorMessage += "For loops must have 2 or 3 expressions: ";
				errorMessage += nodeRepresentation;
				throw new SmartScriptParserException(errorMessage);
			}

			ForLoopNode node = new ForLoopNode(variable, start, end, step);
			return node;
		}
		return null;
	}

	/**
	 * Constructs TextNode from string representation of node.
	 * 
	 * @param nodeRepresentation
	 *            String that contains node in document.
	 * @return TextNode if string is representation of text, null otherwise.
	 */
	public static TextNode processTextNode(String nodeRepresentation) {

		if (nodeRepresentation == null) {
			return null;
		}

		if (Pattern.matches(NodeRegex.getTextNodeRegex(), nodeRepresentation)) {

			TextNode node = new TextNode(nodeRepresentation);
			return node;

		}

		return null;
	}
}
