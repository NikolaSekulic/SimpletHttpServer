package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Stack;

/**
 * Function factory. Creates {@link IFunction} instances for provided function
 * name.
 * 
 * 
 * @author Nikola Sekulić
 *
 */
public class FunctionFactory {

	/**
	 * Creates function by name of function.
	 * 
	 * @param name
	 *            name of function
	 * @param stack
	 *            stack with arguments for function.
	 * @param params
	 *            other uncommon parameters for function.
	 * @return IFunction object that implements function defined with name
	 */
	public static IFunction createFunction(String name, Stack<Object> stack,
			Object... params) {

		if (name.equalsIgnoreCase("sin")) {
			return new Sin(stack);
		}

		if (name.equalsIgnoreCase("decfmt")) {
			return new Decfmt(stack);
		}

		if (name.equalsIgnoreCase("dup")) {
			return new Dup(stack);
		}

		if (name.equalsIgnoreCase("swap")) {
			return new Swap(stack);
		}

		if (name.equalsIgnoreCase("setmimetype")) {
			return new SetMimeType(stack, (RequestContext) params[0]);
		}

		if (name.equalsIgnoreCase("paramget")) {
			return new ParamGet(stack, (RequestContext) params[0]);
		}

		if (name.equalsIgnoreCase("tparamget")) {
			return new TParamGet(stack, (RequestContext) params[0]);
		}

		if (name.equalsIgnoreCase("pparamget")) {
			return new PParamGet(stack, (RequestContext) params[0]);
		}

		if (name.equalsIgnoreCase("tparamset")) {
			return new TParamSet(stack, (RequestContext) params[0]);
		}

		if (name.equalsIgnoreCase("pparamset")) {
			return new PParamSet(stack, (RequestContext) params[0]);
		}

		if (name.equalsIgnoreCase("tparamdel")) {
			return new TParamDel(stack, (RequestContext) params[0]);
		}

		if (name.equalsIgnoreCase("pparamdel")) {
			return new PParamDel(stack, (RequestContext) params[0]);
		}

		throw new IllegalArgumentException("Unknown function!");
	}
}
