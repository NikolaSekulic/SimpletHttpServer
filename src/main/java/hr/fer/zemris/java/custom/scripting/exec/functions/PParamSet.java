package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Function that creates persistent parameter in request context.
 * 
 * @author Nikola Sekulić
 *
 */
public class PParamSet implements IFunction {

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
	public PParamSet(Stack<Object> stack, RequestContext context) {
		this.stack = stack;
		this.context = context;
	}

	/**
	 * Pops name of new parameter from stack. Pops value for new parameter form
	 * stack. Create new persistent parameter in context.
	 */
	@Override
	public void execute() {

		final String name = this.stack.pop().toString();

		final String value = this.stack.pop().toString();

		this.context.setPersistentParameter(name, value);

	}
}
