package hr.fer.zemris.java.custom.scripting.parser.nodes;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.custom.scripting.parser.regex.NodeRegex;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class NodeStream is used for getting String representation of node from
 * String that contains more nodes.
 * 
 * @author Nikola Sekulić
 * 
 */
public class NodeStream {

	/**
	 * Pattern that represents node in text
	 */
	Pattern pattern;

	/**
	 * Buffer that contains nodes
	 */
	String nodeBuffer;

	/**
	 * Constructs NodeStream from String that contains more nodes.
	 * 
	 * @param nodeBuffer
	 *            String with nodes.
	 * @throws NullPointerException
	 *             if nodeBuffer is null
	 */
	public NodeStream(String nodeBuffer) throws NullPointerException {
		if (nodeBuffer == null) {
			throw new NullPointerException(
					"NodeStream cannot be constructed from null!");
		}
		this.nodeBuffer = nodeBuffer;
		pattern = Pattern.compile(NodeRegex.getNodeRegex());

	}

	/**
	 * Gets next String representation of one node from buffer. Content between
	 * "{$[Node Name]" and "$}" is not checked. There can be anything, except
	 * for the End node. But node has to start with "{$[Node Name]" and end with
	 * "$}",
	 * 
	 * @return string that represents one node. null if buffer is empty.
	 * @throws SmartScriptParserException
	 *             if buffer contains substring that is not node representation.
	 */
	public String nextNode() throws SmartScriptParserException {

		if (nodeBuffer.equals("")) {
			return null;
		}

		Matcher matcher = pattern.matcher(nodeBuffer);

		if (matcher.lookingAt()) {
			String node = nodeBuffer.substring(matcher.start(), matcher.end());
			nodeBuffer = nodeBuffer.substring(matcher.end(),
					nodeBuffer.length());
			return node;
		} else {
			throw new SmartScriptParserException("Cannot parse new node from:"
					+ nodeBuffer);
		}
	}

}
