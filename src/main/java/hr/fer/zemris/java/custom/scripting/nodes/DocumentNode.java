package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Class Document represents node in syntax tree that contains entire document.
 * It is used for root node in tree.
 * 
 * @author Nikola
 * 
 */
public class DocumentNode extends Node {

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}

}
