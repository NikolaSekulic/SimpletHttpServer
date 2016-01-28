package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Function that pushes persistent parameters form request context to stack.
 * 
 * @author Nikola Sekulić
 *
 */
public class PParamGet implements IFunction {

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
	public PParamGet(Stack<Object> stack, RequestContext context) {
		this.stack = stack;
		this.context = context;
	}

	/**
	 * Pops default value of persistent parameter. Pops name of parameter. Gets
	 * persistent parameter from context. If parameter doesn't exist, pushes
	 * default value on stack, otherwise pushes parameter to stack.
	 */
	@Override
	public void execute() {

		final String defValue = this.stack.pop().toString();
		final String name = this.stack.pop().toString();

		final String value = this.context.getPersistentParameter(name);

		if (value == null) {
			this.stack.push(defValue);
		} else {
			this.stack.push(value);
		}

	}
}
