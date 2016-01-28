package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Function that sets temporary parameter to {@link RequestContext}.
 * 
 * @author Nikola Sekulić
 *
 */
public class TParamSet implements IFunction {

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
	public TParamSet(Stack<Object> stack, RequestContext context) {
		this.stack = stack;
		this.context = context;
	}

	/**
	 * Pops name of new parameter from stack. Pops value for new parameter form
	 * stack. Create new temporary parameter in context.
	 */
	@Override
	public void execute() {

		final String name = this.stack.pop().toString();

		final String value = this.stack.pop().toString();

		this.context.setTemporaryParameter(name, value);

	}

}
