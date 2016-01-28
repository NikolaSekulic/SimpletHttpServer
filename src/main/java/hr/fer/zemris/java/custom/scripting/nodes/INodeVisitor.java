package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node visitor for {@link Node}.
 * 
 * @author Nikola Sekulić
 *
 */
public interface INodeVisitor {

	/**
	 * Processes {@link TextNode}
	 * 
	 * @param node
	 *            text node
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Processes {@link ForLoopNode}
	 * 
	 * @param node
	 *            node to visit
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Processes {@link EchoNode}
	 * 
	 * @param node
	 *            node to visit
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Processes {@link DocumentNode}
	 * 
	 * @param node
	 *            node to visit
	 */
	public void visitDocumentNode(DocumentNode node);

	/**
	 * Processes {@link EndNode}
	 * 
	 * @param node
	 *            node to visit
	 */
	public void visitEndNode(EndNode node);
}