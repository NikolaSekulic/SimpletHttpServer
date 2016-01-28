package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Class EndNode represents end expression in document.
 * 
 * @author Nikola Sekulić
 *
 */
public class EndNode extends Node {

	@Override
	public String toString() {
		return "{$END$}";
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEndNode(this);
	}
}
