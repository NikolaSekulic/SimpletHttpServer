package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;

/**
 * Class EchoNode represents EchoExpression in document. Contains zero ore more
 * token expressions.
 * 
 * @author Nikola Sekulić
 * 
 */
public class EchoNode extends Node {

	/**
	 * Nodes in echo node
	 */
	private Token[] tokens;

	/**
	 * Constructs EchoNode that contains zero ore more tokens.
	 * 
	 * @param tokens
	 *            zero or more of tokens that echo expression contains.
	 */
	public EchoNode(Token... tokens) {
		this.tokens = tokens;
	}

	/**
	 * Returns array of tokens in echo node expression.
	 * 
	 * @return array of nodes contained in this EchoNode
	 */
	public Token[] getTokens() {
		return tokens;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$= ");
		for (Token token : tokens) {
			sb.append(token.asText());
			sb.append(" ");
		}
		sb.append("$}");
		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
