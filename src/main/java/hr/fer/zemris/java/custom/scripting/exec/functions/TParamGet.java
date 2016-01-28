package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Function that gets temporary parameter from {@link RequestContext}.
 * 
 * @author Nikola Sekulić
 *
 */
public class TParamGet implements IFunction {

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
	public TParamGet(Stack<Object> stack, RequestContext context) {
		this.stack = stack;
		this.context = context;
	}

	/**
	 * Pops default value of temporary parameter. Pops name of parameter. Gets
	 * temporary parameter from context. If parameter doesn't exist, pushes
	 * default value on stack, otherwise pushes parameter to stack.
	 */
	@Override
	public void execute() {

		final String defValue = this.stack.pop().toString();
		final String name = this.stack.pop().toString();

		final String value = this.context.getTemporaryParameter(name);

		if (value == null) {
			this.stack.push(defValue);
		} else {
			this.stack.push(value);
		}

	}
}
