package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

/**
 * Function that swaps last two elements on stack
 * 
 * @author Nikola Sekulić
 *
 */
public class Swap implements IFunction {

	/**
	 * Stack for function execution.
	 */
	private final Stack<Object> stack;

	/**
	 * Constructor
	 * 
	 * @param stack
	 *            stack with function arguments
	 */
	public Swap(Stack<Object> stack) {
		this.stack = stack;
	}

	/**
	 * Replace position of last to added values on stack.
	 */
	@Override
	public void execute() {
		final Object obj1 = this.stack.pop();
		final Object obj2 = this.stack.pop();
		this.stack.push(obj1);
		this.stack.push(obj2);
	}

}
