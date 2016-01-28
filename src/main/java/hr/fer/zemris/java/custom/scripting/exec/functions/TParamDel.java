package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Function that deletes temporary parameter form {@link RequestContext}
 * 
 * @author Nikola Sekulić
 *
 */
public class TParamDel implements IFunction {

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
	public TParamDel(Stack<Object> stack, RequestContext context) {
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

		this.context.removeTemporaryParameter(name);

	}

}
