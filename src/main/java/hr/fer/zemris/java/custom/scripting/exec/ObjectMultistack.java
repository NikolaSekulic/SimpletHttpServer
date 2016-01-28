package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectMultistack is collection of stacks. Each stack can be accessed wit key.
 * Key of Stack is String. Values on stack are {@link ValueWrapper} instances.
 *
 * @author Nikola Sekulić
 *
 */
public class ObjectMultistack {

	/** stacks **/
	private Map<String, MultistackEntry> stacks;

	/**
	 * Constructor. Initializes stacks.
	 */
	public ObjectMultistack() {

		stacks = new HashMap<String, ObjectMultistack.MultistackEntry>();
	}

	/**
	 * Pushes value on stack.
	 *
	 * @param name
	 *            key of stack
	 * @param valueWrapper
	 *            value to be pushed
	 */
	public void push(String name, ValueWrapper valueWrapper) {

		MultistackEntry entry = stacks.get(name);

		if (entry == null) {
			stacks.put(name, new MultistackEntry(valueWrapper, null));
		} else {
			stacks.put(name, new MultistackEntry(valueWrapper, entry));
		}

	}

	/**
	 * Gets value from top of stack and deletes that value from stack.
	 *
	 * @param name
	 *            stack's key
	 * @return value on top of stack with provided key
	 * @throws ObjectMultistackException
	 *             if stack with provided key is empty
	 */
	public ValueWrapper pop(String name) {

		MultistackEntry entry = stacks.get(name);

		if (entry == null) {
			throw new ObjectMultistackException("Stack with key " + name
					+ " is empty.");
		}

		stacks.put(name, entry.next);
		return entry.value;
	}

	/**
	 * Gets value from top of stack.
	 *
	 * @param name
	 *            stack's key
	 * @return value on top of stack with provided key
	 * @throws ObjectMultistackException
	 *             if stack with provided key is empty
	 */
	public ValueWrapper peek(String name) {

		MultistackEntry entry = stacks.get(name);

		if (entry == null) {
			throw new ObjectMultistackException("Stack with key " + name
					+ " is empty.");
		}
		return entry.value;
	}

	/**
	 * Checks if stack with provided key is empty.
	 *
	 * @param name
	 *            key of stack
	 * @return <code>true</code> if and only if stack with provided key is empty
	 */
	public boolean isEmpty(String name) {

		return stacks.get(name) == null;
	}

	/**
	 * Entry of the stack. Stack is implemented as linked list of stack entries.
	 * Entry contains {@link ValueWrapper} object as value on stack and
	 * reference to next entry on stack.
	 *
	 * @author Nikola Sekulić
	 *
	 */
	private static class MultistackEntry {

		/** value **/
		private ValueWrapper value;

		/** next entry in list **/
		private MultistackEntry next;

		/**
		 * Constructor
		 *
		 * @param value
		 *            value
		 * @param next
		 *            next entry in list
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {

			super();
			this.value = value;
			this.next = next;
		}

	}
}
