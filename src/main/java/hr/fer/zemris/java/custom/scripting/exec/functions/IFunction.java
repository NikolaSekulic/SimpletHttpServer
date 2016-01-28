package hr.fer.zemris.java.custom.scripting.exec.functions;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;

/**
 * Interface defines function that is executed on {@link SmartScriptEngine}
 * 
 * @author Nikola Sekulić
 * 
 */
public interface IFunction {

	/**
	 * Executes function.
	 */
	public void execute();
}
