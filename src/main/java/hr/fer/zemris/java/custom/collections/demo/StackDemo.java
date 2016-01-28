package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Program StackDemo calculates postfix arithmetical expression. Expression is
 * provided through first program's argument, represented as string. Expression
 * can contains integer numbers, and operators: +, *, /, - and %. Between
 * operators and numbers has to be one or more spaces.
 * 
 * @author Nikola Sekulić
 *
 */
public class StackDemo {

	/**
	 * Class action provides method for stack operation.
	 * 
	 * @author Nikola Sekulić
	 *
	 */
	static abstract class Action {

		/**
		 * Performs action on stack.
		 * 
		 * @param stack
		 *            stack
		 * @param parameter
		 *            input parameter.
		 */
		public abstract void preformAction(ObjectStack stack, String parameter);
	}

	/**
	 * Provides method for handling number.
	 * 
	 * @author Nikola Sekulić
	 *
	 */
	static class NumberAction extends Action {

		@Override
		/**
		 * Parses integer from parameter and pushes it to stack.
		 */
		public void preformAction(ObjectStack stack, String parameter)
				throws NumberFormatException, EmptyStackException {
			int number = Integer.parseInt(parameter);
			stack.push(number);
		}

	}

	/**
	 * Provides method for handling operator.
	 * 
	 * @author Nikola Sekulić
	 *
	 */
	static abstract class Operator extends Action {
		public abstract int getResult(int firstOperand, int secondOperand);

		@Override
		/**
		 * Pops two number from stack, calculates result of operation, and pushes result to stack.
		 */
		public void preformAction(ObjectStack stack, String parameter) {

			int secondOperand = (Integer) stack.pop();
			int firstOperand = (Integer) stack.pop();
			int result = getResult(firstOperand, secondOperand);
			System.out.format("%d %s %d = %d%n", firstOperand, parameter,
					secondOperand, result);
			stack.push(result);
		}
	}

	/**
	 * Provides method for handling addition.
	 * 
	 * @author Nikola Sekulić
	 *
	 */
	static class OperatorPlus extends Operator {

		@Override
		/**
		 * {@inheritDoc}
		 */
		public int getResult(int firstOperand, int secondOperand) {
			return firstOperand + secondOperand;
		}
	}

	/**
	 * Provides method for handling subtraction.
	 * 
	 * @author Nikola Sekulić
	 *
	 */
	static class OperatorMinus extends Operator {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getResult(int firstOperand, int secondOperand) {
			return firstOperand - secondOperand;
		}
	}

	/**
	 * Provides method for handling division.
	 * 
	 * @author Nikola Sekulić
	 *
	 */
	static class OperatorDiv extends Operator {

		@Override
		public int getResult(int firstOperand, int secondOperand)
				throws IllegalArgumentException {

			if (secondOperand == 0) {
				throw new IllegalArgumentException("Cannot divide with zero!");
			}
			return firstOperand / secondOperand;
		}
	}

	/**
	 * Provides method for handling multiplication.
	 * 
	 * @author Nikola Sekulić
	 *
	 */
	static class OperatorMul extends Operator {

		@Override
		public int getResult(int firstOperand, int secondOperand) {
			return firstOperand * secondOperand;
		}
	}

	/**
	 * Provides method for handling modulo.
	 * 
	 * @author Nikola Sekulić
	 *
	 */
	static class OperatorMod extends Operator {

		@Override
		public int getResult(int firstOperand, int secondOperand)
				throws IllegalArgumentException {

			if (secondOperand == 0) {
				throw new IllegalArgumentException("Cannot divide with zero!");
			}
			return firstOperand % secondOperand;
		}
	}

	/**
	 * Main method of program.
	 * 
	 * @param args
	 *            program's arguments
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err
					.println("Program expects exacly one argument, postfix expresion");
			return;
		}

		System.out.println("Expersion '" + args[0] + "' is provided");

		String[] arguments = args[0].trim().split("\\s+");

		Action numberAction = new NumberAction();
		Action operatorPlus = new OperatorPlus();
		Action operatorMinus = new OperatorMinus();
		Action operatorMul = new OperatorMul();
		Action operatorDiv = new OperatorDiv();
		Action operatorMod = new OperatorMod();

		ObjectStack stack = new ObjectStack();

		for (String argument : arguments) {

			try {
				if (argument.equals("+")) {
					operatorPlus.preformAction(stack, argument);
				} else if (argument.equals("-")) {
					operatorMinus.preformAction(stack, argument);
				} else if (argument.equals("*")) {
					operatorMul.preformAction(stack, argument);
				} else if (argument.equals("/")) {
					operatorDiv.preformAction(stack, argument);
				} else if (argument.equals("%")) {
					operatorMod.preformAction(stack, argument);
				} else {
					numberAction.preformAction(stack, argument);
				}
			} catch (NumberFormatException nfe) {
				System.err.println("Invalid expression!");
				System.err
						.println("Expresion can contains integer, numbers or operators: +, -, /, * and %!");
				return;
			} catch (EmptyStackException ese) {
				System.err.println("Expression is not valid!");
				return;
			} catch (IllegalArgumentException iae) {
				System.err.println(iae.getMessage());
				return;
			} catch (Exception e) {
				System.err.println("Unknown exception! " + e.getClass()
						+ " Message: " + e.getMessage());
				return;
			}
		}

		if (stack.size() != 1) {
			System.err.println("Invalid expression!");
			return;
		}

		System.out.println("Result of expression: " + stack.peek());

	}
}
