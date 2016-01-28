package hr.fer.zemris.java.custom.collections;

import java.security.InvalidParameterException;

/**
 * The ObjectStack class represents LIFO collection (Last In First Out), usually
 * called stack.
 * 
 */
public class ObjectStack {

	/**
	 * Wrapped collection that contains element of stack.
	 */
	private final ArrayBackedIndexedCollection adapteeCollection;

	/**
	 * Constructs an empty stack.
	 */
	public ObjectStack() {
		adapteeCollection = new ArrayBackedIndexedCollection();
	}

	/**
	 * Tests if stack is empty.
	 * 
	 * @return true if and only if the stack has no elements, false otherwise
	 */
	public boolean isEmpty() {
		return adapteeCollection.isEmpty();
	}

	/**
	 * Returns number of elements in the stack.
	 * 
	 * @return number of elements in the stack
	 */
	public int size() {
		return adapteeCollection.size();
	}

	/**
	 * Pushes specified element onto the top of the stack.
	 * 
	 * @param value
	 *            element to be pushed
	 * @throws InvalidParameterException
	 *             if specified element is null
	 */
	public void push(Object value) throws InvalidParameterException {
		adapteeCollection.add(value);
	}

	/**
	 * Removes the object from the top of the stack.
	 * 
	 * @return object that is removed from the top of the stack
	 * @throws EmptyStackException
	 *             if the stack is empty
	 */
	public Object pop() throws EmptyStackException {

		if (adapteeCollection.size() == 0) {
			String errorMessage = "pop(): stack is empty";
			throw new EmptyStackException(errorMessage);
		}

		Object topOfStack = adapteeCollection.get(adapteeCollection.size() - 1);
		adapteeCollection.remove(adapteeCollection.size() - 1);

		return topOfStack;
	}

	/**
	 * Returns the object form the top of stack. Returnde object is not removed
	 * from stack.
	 * 
	 * @return object from the top of the stack
	 * @throws EmptyStackException
	 *             if the stack is empty
	 */
	public Object peek() throws EmptyStackException {

		if (adapteeCollection.size() == 0) {
			String errorMessage = "peek(): stack is empty";
			throw new EmptyStackException(errorMessage);
		}

		Object topOfStack = adapteeCollection.get(adapteeCollection.size() - 1);

		return topOfStack;
	}

	/**
	 * Removes all element from the stack.
	 */
	public void clear() {
		adapteeCollection.clear();
	}

	/**
	 * Converts stack to String.
	 * 
	 * @return String representation of stack.
	 */
	@Override
	public String toString() {
		return adapteeCollection.toString();
	}

}
