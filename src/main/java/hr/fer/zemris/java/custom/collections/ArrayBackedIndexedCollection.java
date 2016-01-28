package hr.fer.zemris.java.custom.collections;

/**
 * The ArrayBackedIndexedCollection implements a resizable array of Objects.
 * Elements of object can be accessed using an integer index. Each object of the
 * class tries to optimize storage management by maintaining a capacity. The
 * capacity is always at least as large as number of elements. Default capacity
 * of collection is 16.
 * 
 * @author Nikola Sekulić
 * @version 1.0
 * 
 */
public class ArrayBackedIndexedCollection {

	/**
	 * Constant default capacity of collection.
	 */
	public static final int DEFAULT_CAPACITY = 16;

	/**
	 * Number of elements in collection
	 */
	private int size;

	/**
	 * Capacity of collection
	 */
	private int capacity;

	/**
	 * Elements in collection.
	 */
	Object[] elements;

	/**
	 * Constructs empty collection object with default capacity.
	 */
	public ArrayBackedIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Construct empty collection object with the specified initial capacity.
	 * Initial capacity has to be greater then zero.
	 * 
	 * @param capacity
	 *            initial capacity of collection
	 * @throws IllegalArgumentException
	 *             if initial capacity if less then one.
	 */
	public ArrayBackedIndexedCollection(int capacity)
			throws IllegalArgumentException {

		if (capacity < 1) {
			String errorMessage = "Initial capacity has to be greater than 1!";
			throw new IllegalArgumentException(errorMessage);
		}

		size = 0;
		this.capacity = capacity;
		elements = new Object[capacity];
	}

	/**
	 * Doubles capacity of collection.
	 */
	private void doubleCapacity() {

		Object[] newElements = new Object[2 * elements.length];

		for (int index = 0; index < elements.length; index++) {
			newElements[index] = elements[index];
		}

		elements = newElements;
		capacity *= 2;
	}

	/**
	 * Tests if collection has no elements.
	 * 
	 * @return true if and only if collection has no elements; false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the number of elements in collection.
	 * 
	 * @return the number of elements in collection.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns capacity of collection.
	 * 
	 * @return capacity of collection
	 */
	public int capacity() {
		return this.capacity;
	}

	/**
	 * Appends specified object at the end of collection.
	 * 
	 * @param value
	 *            element to be appended to collection.
	 * @throws IllegalArgumentException
	 *             if specified element is null.
	 */
	public void add(Object value) throws IllegalArgumentException {

		if (size == capacity) {
			doubleCapacity();
		}

		if (value == null) {
			String errorMessage = "Adding null referenece is not allowed!";
			throw new IllegalArgumentException(errorMessage);
		}

		elements[size] = value;
		size += 1;
	}

	/**
	 * Returns the element at specified position in collection.
	 * 
	 * @param index
	 *            index of the element to return
	 * @return object at the specified index
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range
	 */
	public Object get(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= size) {
			String errorMessage = "Size of collection=" + size + ". Index="
					+ index + ".";
			throw new IndexOutOfBoundsException(errorMessage);
		}

		return elements[index];
	}

	/**
	 * Removes element at specified index. Shifts any subsequent elements to the
	 * left.
	 * 
	 * @param index
	 *            the index of element to be removed
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range
	 */
	public void remove(int index) throws IndexOutOfBoundsException {

		if (index < 0 || index >= size) {
			String errorMessage = "Size of collection=" + size + ". Index="
					+ index + ".";
			throw new IndexOutOfBoundsException(errorMessage);
		}

		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}

		elements[size - 1] = null;

		size -= 1;
	}

	/**
	 * Inserts specified element at specified position. Each component in
	 * collection with an index greater or equal to specified position is
	 * shifted upward to have an index one greater then the value it had
	 * previously.
	 * 
	 * @param value
	 *            element to be insert
	 * @param position
	 *            where to insert new element
	 * @throws IllegalArgumentException
	 *             if specified element is null
	 * @throws IndexOutOfBoundsException
	 *             if position is less then 0 or greater the number of elements
	 *             in collection
	 */
	public void insert(Object value, int position)
			throws IllegalArgumentException, IndexOutOfBoundsException {

		if (position < 0 || position > size) {
			String errorMessage = "Size of collection=" + size + ". Position="
					+ position + ".";
			throw new IndexOutOfBoundsException(errorMessage);
		}

		if (value == null) {
			String errorMessage = "Adding null referenece is not allowed!";
			throw new IllegalArgumentException(errorMessage);
		}

		if (size == capacity) {
			doubleCapacity();
		}

		for (int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}

		elements[position] = value;

		size += 1;
	}

	/**
	 * Returns the index of the first occurrence of the specified element in
	 * collection. Elements in collection and specified element are compared
	 * with method equal, inherited from class {@link Object}. If collection
	 * does not contain specified element, method returns -1.
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in
	 *         collection, or -1 if this collection does not contain the element
	 */
	public int indexOf(Object value) {

		for (int index = 0; index < size; index++) {
			if (elements[index].equals(value)) {
				return index;
			}
		}

		return -1;
	}

	/**
	 * Tests if collection contains specified element. Elements in collection
	 * and specified element are compared with method equal, inherited from
	 * class {@link Object}.
	 * 
	 * @param value
	 *            element whose presence in collection is to be tested.
	 * @return true if and only if collection contains specified element.
	 */
	public boolean contains(Object value) {
		int index = this.indexOf(value);

		if (index >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * Removes all elements from collection. Capacity is set to default
	 * capacity.
	 */
	public void clear() {
		elements = new Object[capacity];
		size = 0;
	}

	/**
	 * Converts collection to String.
	 * 
	 * @return String representation of collection.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Size: ").append(size).append(" Capacity: ").append(capacity);

		for (Object element : elements) {
			if (element != null) {
				sb.append(" ").append(element);
			}
		}

		return sb.toString();
	}

}
