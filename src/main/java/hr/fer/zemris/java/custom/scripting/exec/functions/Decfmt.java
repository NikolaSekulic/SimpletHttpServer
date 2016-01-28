package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Stack;

/**
 * Function for parsing decimal number from stack.
 * 
 * @author Nikola Sekulić
 *
 */
public class Decfmt implements IFunction {

	/**
	 * Stack for execution of function.
	 */
	private final Stack<Object> stack;

	/**
	 * Constructor
	 * 
	 * @param stack
	 *            stack with function arguments
	 */
	public Decfmt(Stack<Object> stack) {
		this.stack = stack;
	}

	/**
	 * Pops decimal format form stack. Pops number from stack. Formats number
	 * and pushes it back to stack.
	 */
	@Override
	public void execute() {
		final String format = (String) this.stack.pop();
		final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		final DecimalFormat df = new DecimalFormat(format, symbols);

		final ValueWrapper value = new ValueWrapper(this.stack.pop());
		value.multiply(1.0);
		final String str = df.format(value.getValue());
		this.stack.push(str);

	}

}
