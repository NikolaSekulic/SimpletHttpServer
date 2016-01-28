package hr.fer.zemris.java.custom.scripting.parser;

import java.security.InvalidParameterException;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.EndNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.nodes.NodeStream;

/**
 * Class SmartScriptParser represents syntax tree of document.
 * 
 * @author Nikola Sekulić
 * 
 */
public class SmartScriptParser {

	/**
	 * Document (root) node of script
	 */
	private DocumentNode documentNode;

	/**
	 * Stack for script parsing
	 */
	private ObjectStack stack;

	/**
	 * Returns root node of syntax tree.
	 * 
	 * @return DocumentNode that represents root node of syntax tree.
	 */
	public DocumentNode getDocumentNode() {
		return this.documentNode;
	}

	/**
	 * Construct SmartScripParser object that presents syntax tree of document.
	 * 
	 * @param docBody
	 *            string that contains entire document as text.
	 * @throws SmartScriptParserException
	 *             if there is syntax or lexical error in document.
	 * @throws NullPointerException
	 *             if input is null.
	 */
	public SmartScriptParser(String docBody) throws SmartScriptParserException,
			NullPointerException {

		if (docBody == null) {
			throw new NullPointerException("DocBody cannot be null!");
		}

		documentNode = new DocumentNode();
		stack = new ObjectStack();
		stack.push(documentNode);

		NodeStream nodeStream = new NodeStream(docBody);

		String nodeRepresentation = null;

		while ((nodeRepresentation = nodeStream.nextNode()) != null) {

			Node node = NodeProcessor.processNode(nodeRepresentation);
			if (node == null) {
				throw new SmartScriptParserException(
						"SmartScriptParser: Unknown node!");
			}

			try {
				evaluateEchoNode(node);
				evaluateEndNode(node);
				evaluateForLoopNode(node);
				evaluateTextNode(node);
			} catch (EmptyStackException e) {
				throw new SmartScriptParserException("Syntax error!", e);
			} catch (InvalidParameterException e) {
				throw new SmartScriptParserException(
						"Unable to parse document!", e);
			} catch (SmartScriptParserException e) {
				throw e;
			} catch (Exception e) {
				throw new SmartScriptParserException("Unknown error!");
			}
		}

		if (stack.size() != 1) {
			throw new SmartScriptParserException(
					"Syntax error! Missing {$END$} tag.");
		}
	}

	/**
	 * If node is EchoNode, put it as child to the node on top of stack.
	 * 
	 * @param node
	 *            node
	 */
	private void evaluateEchoNode(Node node) {
		if (node.getClass() == EchoNode.class) {
			((Node) stack.peek()).addChildNode(node);
		}
	}

	/**
	 * If node is EndNode, pops node from stack and to previous node as child.
	 * 
	 * @param node
	 *            node
	 */
	private void evaluateEndNode(Node node) {
		if (node.getClass() == EndNode.class) {
			Node forLoopNode = (Node) stack.pop();
			if (forLoopNode.getClass() != ForLoopNode.class) {
				throw new SmartScriptParserException(
						"End tag has not corespodent for loop.");
			}

			((Node) stack.peek()).addChildNode(forLoopNode);
		}
	}

	/**
	 * If node is ForLoopNode, it is pushed on stack.
	 * 
	 * @param node
	 *            node
	 */
	private void evaluateForLoopNode(Node node) {
		if (node.getClass() == ForLoopNode.class) {
			stack.push(node);
		}
	}

	/**
	 * If node is TextNode, put it as child to node on top of stack.
	 * 
	 * 
	 * @param node
	 *            node
	 */
	private void evaluateTextNode(Node node) {
		if (node.getClass() == TextNode.class) {
			((Node) stack.peek()).addChildNode(node);
		}
	}

}
