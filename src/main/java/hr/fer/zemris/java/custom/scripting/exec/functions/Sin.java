package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;

import java.util.Stack;

/**
 * Function that calculates sine.
 * 
 * @author Nikola Sekulić
 *
 */
public class Sin implements IFunction {

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
	public Sin(Stack<Object> stack) {
		super();
		this.stack = stack;
	}

	/**
	 * Pops value from stack. Calculates it's sin in degrees and pushes value os
	 * in to stack.
	 */
	@Override
	public void execute() {
		final ValueWrapper argument = new ValueWrapper(this.stack.pop());
		argument.multiply(1.0);
		this.stack.push(Math.sin((Double) argument.getValue()
				* (Math.PI / 180.0)));
	}

}
