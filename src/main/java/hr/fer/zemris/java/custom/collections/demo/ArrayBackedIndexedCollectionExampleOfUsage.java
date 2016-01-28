package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;

/**
 * Demo program for {@link ArrayIndexOutOfBoundsException}. Program ignores all
 * arguments.
 * 
 * @author Nikola Sekulić
 *
 */
public class ArrayBackedIndexedCollectionExampleOfUsage {

	/**
	 * Main method of program.
	 * 
	 * @param args
	 *            program's arguments
	 */
	public static void main(String[] args) {

		ArrayBackedIndexedCollection col = new ArrayBackedIndexedCollection(2);
		col.add(Integer.valueOf(20));
		col.add("New York");
		col.add("San Francisco"); // here the internal array is reallocated to 4
		System.out.println(col.contains("New York")); // writes: true
		col.remove(1); // removes "New York"; shifts "San Francisco" to position
						// 1
		System.out.println(col.get(1)); // writes: "San Francisco"
		System.out.println(col.size()); // writes: 2

		System.out.println();

		col = new ArrayBackedIndexedCollection();
		System.out.println(col.capacity()); // writes 16

		System.out.println();

		col.insert(1, 0);
		col.add(3);
		col.add(5);
		col.insert(2, 1);
		col.insert(4, 3);
		col.insert(7, 5);
		col.insert(6, 5);

		// writes: 1 2 3 4 5 6 7
		for (int i = 0; i < col.size(); i++) {
			System.out.print(col.get(i) + " ");
		}

		System.out.println();
		System.out.println();

		// writes: 0 2 6 -1 -1
		System.out.println(col.indexOf(1) + " " + col.indexOf(3) + " "
				+ col.indexOf(7) + " " + col.indexOf(-1) + col.indexOf("2"));
		System.out.println();

		col = new ArrayBackedIndexedCollection(3);
		col.add(1);
		col.add(2);
		col.add(3);
		col.add(4);

		// writes 6
		System.out.println(col.capacity());

		// writes IllegalArgumentException 1
		try {
			col.add(null);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException 1");
		}

		// writes IllegalArgumentException 2
		try {
			col.insert(null, 0);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException 2");
		}

		System.out.println();

		col = new ArrayBackedIndexedCollection();

		col.add(0);
		col.add(1);

		// writes IndexOutOfBoundsException 1
		try {
			col.insert(3, 3);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException 1");
		}

		// writes IndexOutOfBoundsException 2
		try {
			col.remove(2);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException 2");
		}
	}
}
