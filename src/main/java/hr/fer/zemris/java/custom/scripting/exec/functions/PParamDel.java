package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * 
 * Function that removes persistent parameter form HTTP request context.
 * 
 * @author Nikola Sekulić
 *
 */
public class PParamDel implements IFunction {

	/**
	 * Stack for function execution.
	 */
	private final Stack<Object> stack;

	/**
	 * HTTP request context for function execution.
	 */
	private final RequestContext context;

	/**
	 * Constructor
	 * 
	 * @param stack
	 *            stack with function argument
	 * @param context
	 *            context with parameters
	 */
	public PParamDel(Stack<Object> stack, RequestContext context) {
		this.stack = stack;
		this.context = context;
	}

	/**
	 * Pops name of parameter form stack. removes parameter form persistent
	 * parameters of context.
	 */
	@Override
	public void execute() {

		final String name = this.stack.pop().toString();

		this.context.removePersistentParameter(name);

	}
}
