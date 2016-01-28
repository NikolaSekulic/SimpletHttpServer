package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.visitors.ExecutionVisitor;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Smart script program executor. Writes result of execution to RequestContext
 * output stream.
 * 
 * @author Nikola Sekulić
 * 
 */
public class SmartScriptEngine {

	/**
	 * Document (root) node of script
	 */
	private final DocumentNode documentNode;

	/**
	 * Visitor that executes document node
	 */
	private final INodeVisitor visitor;

	/**
	 * Constructor
	 * 
	 * @param documentNode
	 *            root of syntax tree of script
	 * @param requestContext
	 *            context
	 */
	public SmartScriptEngine(DocumentNode documentNode,
			RequestContext requestContext) {
		this.documentNode = documentNode;
		this.visitor = new ExecutionVisitor(requestContext);
	}

	/**
	 * Executes script
	 */
	public void execute() {
		this.documentNode.accept(this.visitor);
	}
}
