package hr.fer.zemris.java.custom.scripting.nodes;

import java.security.InvalidParameterException;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;

/**
 * Class Node represents a node in syntax tree. Each node may contain zero or
 * more nodes as children. Child of the node is subnode of node in tree.
 * 
 * @author Nikola Sekulić
 * 
 */
public abstract class Node {

	/**
	 * Children nodes
	 */
	private ArrayBackedIndexedCollection children;

	/**
	 * Adds specified node as child.
	 * 
	 * @param child
	 *            node to be added
	 * @throws InvalidParameterException
	 *             if specified node is null
	 */
	public void addChildNode(Node child) throws InvalidParameterException {
		if (children == null) {
			children = new ArrayBackedIndexedCollection();
		}

		children.add(child);
	}

	/**
	 * Gets number of children.
	 * 
	 * @return number of children
	 */
	public int numberOfChildren() {
		if (children == null) {
			return 0;
		}
		return children.size();
	}

	/**
	 * Returns child at specified position. Child on the left side has index 0.
	 * Index of child increases, as it position moves to the right.
	 * 
	 * @param index
	 *            Index of the child.
	 * @return Child at specified index
	 * @throws IndexOutOfBoundsException
	 *             if index is less then 0 or greater or equal to number of
	 *             children.
	 */
	public Node getChild(int index) throws IndexOutOfBoundsException {
		if (children == null) {
			throw new IndexOutOfBoundsException("Index " + index
					+ " id specified. Node has 0 children.");
		}

		Node child = (Node) children.get(index); // this method throws
													// IndexOutOfBoundsException

		return child;
	}

	/**
	 * Accepts visitor that get some information about this node.
	 * 
	 * @param visitor
	 *            visitor to accept
	 */
	public abstract void accept(INodeVisitor visitor);
}
