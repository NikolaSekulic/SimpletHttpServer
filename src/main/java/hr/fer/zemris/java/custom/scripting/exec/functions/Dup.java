package hr.fer.zemris.java.custom.scripting.exec.functions;

import java.util.Stack;

/**
 * Function for duplication of value on top of stack.
 * 
 * @author Nikola Sekulić
 *
 */
public class Dup implements IFunction {

	/**
	 * Stack for execution of function.
	 */
	private final Stack<Object> stack;

	/**
	 * Constructor
	 * 
	 * @param stack
	 *            stack with function argument
	 */
	public Dup(Stack<Object> stack) {
		this.stack = stack;
	}

	/**
	 * Duplicates value from top of stack. Pushes new value on stack.
	 */
	@Override
	public void execute() {
		final Object obj = this.stack.peek();
		this.stack.push(obj);
	}
}
