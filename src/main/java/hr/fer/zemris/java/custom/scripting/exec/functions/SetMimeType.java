package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Function that sets Content-Type parameter in HTTP request.
 * 
 * @author Nikola Sekulić
 *
 */
public class SetMimeType implements IFunction {

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
	public SetMimeType(Stack<Object> stack, RequestContext context) {
		this.stack = stack;
		this.context = context;
	}

	/**
	 * Puts mime-type from stack and sets it to context.
	 */
	@Override
	public void execute() {
		final String mime = (String) this.stack.pop();
		this.context.setMimeType(mime);
	}

}
