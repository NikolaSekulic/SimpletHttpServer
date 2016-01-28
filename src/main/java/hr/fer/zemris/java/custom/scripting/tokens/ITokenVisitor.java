package hr.fer.zemris.java.custom.scripting.tokens;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;

/**
 * Visitor that processes {@link Token} in {@link SmartScriptEngine}
 * 
 * @author Nikola Sekulić
 *
 */
public interface ITokenVisitor {

	/**
	 * Processes {@link TokenConstantDouble}
	 * 
	 * @param token
	 *            token to process
	 */
	public void visitConstantDouble(TokenConstantDouble token);

	/**
	 * Processes {@link TokenConstantInteger}
	 * 
	 * @param token
	 *            token to process
	 */
	public void visitConstatnInteger(TokenConstantInteger token);

	/**
	 * Processes {@link TokenFunction}
	 * 
	 * @param token
	 *            token to process
	 */
	public void visitFunction(TokenFunction token);

	/**
	 * Processes {@link TokenOperator}
	 * 
	 * @param token
	 *            token to process
	 */
	public void visitOperator(TokenOperator token);

	/**
	 * Processes {@link TokenString}
	 * 
	 * @param token
	 *            token to process
	 */
	public void visitString(TokenString token);

	/**
	 * Processes {@link TokenVariable}
	 * 
	 * @param token
	 *            token to process
	 */
	public void visitVariable(TokenVariable token);

}
