package hr.fer.zemris.java.custom.scripting.exec.operations;

@FunctionalInterface
/**
 * Interface that defines operation between two objects
 * @author Nikola Sekulić
 *
 */
public interface IOperation {

	/**
	 * Returns result of operation between two objects
	 *
	 * @param operand1
	 *            first operand
	 * @param operand2
	 *            second operand
	 * @return result of operation
	 */
	Object getResult(Object operand1, Object operand2);

}
