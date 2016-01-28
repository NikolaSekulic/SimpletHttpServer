package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node that represents text in document that does not belong to another nodes.
 * 
 * @author Nikola Sekulić
 * 
 */
public class TextNode extends Node {

	/**
	 * Text in script that belongs to this node
	 */
	private String text;

	/**
	 * Constructs TextNode from text in document.
	 * 
	 * @param text
	 *            text from document, with line delimiters and all empty spaces
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Return text form TextNode.
	 * 
	 * @return text from that represents text node in document
	 */
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
