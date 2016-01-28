package hr.fer.zemris.java.custom.scripting.visitors;

import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.custom.scripting.exec.functions.FunctionFactory;
import hr.fer.zemris.java.custom.scripting.exec.functions.IFunction;
import hr.fer.zemris.java.custom.scripting.tokens.ITokenVisitor;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * EchoTokenVisitor executes echo node in Smart script engine.
 * 
 * @author Nikola Sekulić
 * 
 */
public class EchoTokenVisitor implements ITokenVisitor {

	/**
	 * Stack of node values to be echoed or used in calculation
	 */
	private final Stack<Object> stack;

	/**
	 * Variables
	 */
	private final ObjectMultistack variables;

	/**
	 * Request context used in functions
	 */
	private final RequestContext context;

	/**
	 * Constructor.
	 * 
	 * @param stack
	 *            stack of values
	 * @param variables
	 *            variables
	 * @param context
	 *            context for function
	 */
	public EchoTokenVisitor(Stack<Object> stack, ObjectMultistack variables,
			RequestContext context) {
		super();
		this.stack = stack;
		this.variables = variables;
		this.context = context;
	}

	/**
	 * Pushes value of constant on stack
	 */
	@Override
	public void visitConstantDouble(TokenConstantDouble token) {
		this.stack.push(token.getValue());
	}

	/**
	 * Pushes value if constant on stack.
	 */
	@Override
	public void visitConstatnInteger(TokenConstantInteger token) {
		this.stack.push(token.getValue());
	}

	/**
	 * Executes function. Function can pop and push more arguments from stack.
	 */
	@Override
	public void visitFunction(TokenFunction token) {
		final String name = token.getName();
		final IFunction f = FunctionFactory.createFunction(name, this.stack,
				this.context);
		f.execute();
	}

	/**
	 * Pops two arguments form stack. Executes operation with that arguments.
	 * First argument popped form stack is second operand. Pushes result of
	 * operation on stack.
	 */
	@Override
	public void visitOperator(TokenOperator token) {
		final char operator = token.getSymbol().charAt(0);
		final Object value2 = this.stack.pop();
		final ValueWrapper value1 = new ValueWrapper(this.stack.pop());

		switch (operator) {
			case '+' :
				value1.increment(value2);
				break;
			case '-' :
				value1.decrement(value2);
				break;
			case '*' :
				value1.multiply(value2);
				break;
			case '/' :
				value1.multiply(value2);
				break;
			default :
				throw new RuntimeException("Unknown operator");

		}

		this.stack.push(value1.getValue());
	}

	/**
	 * Pushes to stack value of string token
	 */
	@Override
	public void visitString(TokenString token) {
		final String str = token.getValue();
		// str = str.substring(1, str.length() - 1);
		this.stack.push(str);
	}

	/**
	 * Pushes to stack variable last value
	 */
	@Override
	public void visitVariable(TokenVariable token) {
		final String varName = token.getName();
		this.stack.push(this.variables.peek(varName).getValue());
	}

}
