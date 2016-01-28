package hr.fer.zemris.java.custom.scripting.nodes;

import java.security.InvalidParameterException;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 * Class ForLoopNode represents for loop in document expression. For loop node
 * contains one variable, token for start expression of loop and token for end
 * expression of loop. It can also contains token for step expression of loop,
 * but it is not required.
 * 
 * @author Nikola Sekulić
 * 
 */
public class ForLoopNode extends Node {

	/**
	 * Variable of for loop
	 */
	TokenVariable variable;

	/**
	 * Initial value of variable of for loop
	 */
	Token startExpression;

	/**
	 * Last value of variable in for loop
	 */
	Token endExpression;

	/**
	 * Step of for loop
	 */
	Token stepExpression;

	/**
	 * Constructs ForLoopNode.
	 * 
	 * @param variable
	 *            Token that represents variable in token
	 * @param startExpression
	 *            Token that represents start expression of loop.
	 * @param endExpression
	 *            Token that represents end expression of loop.
	 * @param stepExpression
	 *            Token that represent step expression of loop. It can be null.
	 * 
	 * @throws InvalidParameterException
	 *             if variable, startExpression or endExpression is null.
	 */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression, Token stepExpression) {

		if ((variable == null) || (startExpression == null)
				|| (endExpression == null)) {
			throw new InvalidParameterException(
					"ForLoopNode: null is not allowed");
		}

		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Gets VariableToken that represents variable expression in for loop.
	 * 
	 * @return VariableToken that represents variable expression in for loop.
	 */
	public TokenVariable getVariable() {
		return variable;
	}

	/**
	 * Gets start expression of for loop.
	 * 
	 * @return Token that represents start expression of for loop.
	 */
	public Token getStartExpression() {
		return startExpression;
	}

	/**
	 * Gets end expression of for loop.
	 * 
	 * @return Token that represents end expression of for loop.
	 */
	public Token getEndExpression() {
		return endExpression;
	}

	/**
	 * Gets step expression of for loop.
	 * 
	 * @return Token that represents step expression of for loop if exists, null
	 *         otherwise.
	 */
	public Token getStepExpression() {
		return stepExpression;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$FOR ");
		sb.append(variable.asText()).append(" ");
		sb.append(startExpression.asText()).append(" ");
		sb.append(endExpression.asText()).append(" ");

		if (stepExpression != null) {
			sb.append(stepExpression.asText()).append(" ");
		}

		sb.append("$}");

		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
}
