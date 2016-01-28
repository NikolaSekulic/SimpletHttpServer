package hr.fer.zemris.java.custom.scripting.visitors;

import java.io.IOException;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.EndNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.ITokenVisitor;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Execution visitor is used for execution of smart scripts. Result of execution
 * is written to RequestContext's output stream.
 * 
 * @author Nikola Sekulić
 * 
 */
public class ExecutionVisitor implements INodeVisitor {

	/**
	 * Context for functions
	 */
	private RequestContext context;

	/**
	 * Variables
	 */
	private final ObjectMultistack variables;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            context of HTTP request
	 */
	public ExecutionVisitor(RequestContext context) {
		super();
		this.context = context;
		this.variables = new ObjectMultistack();
	}

	/**
	 * Context getter
	 * 
	 * @return Request Context
	 */
	public RequestContext getContext() {
		return this.context;
	}

	/**
	 * Context setter
	 * 
	 * @param context
	 *            Request Context
	 */
	public void setContext(RequestContext context) {
		this.context = context;
	}

	/**
	 * Writes to context value of text node
	 */
	@Override
	public void visitTextNode(TextNode node) {
		try {
			this.context.write(node.getText());
		} catch (final IOException e) {
			throw new RuntimeException("Cannot write to output stream!");
		}
	}

	/**
	 * Returns value from Token if token is instance of {@link TokenVariable},
	 * {@link TokenConstantDouble}, {@link TokenConstantInteger} or
	 * {@link TokenString},
	 * 
	 * @param token
	 *            token with value
	 * @return value of token
	 */
	private Object getValueForToken(Token token) {
		Object value = null;

		if (token instanceof TokenVariable) {
			value = this.variables.peek(((TokenVariable) token).getName());

		} else if (token instanceof TokenConstantInteger) {
			value = ((TokenConstantInteger) token).getValue();
		} else if (token instanceof TokenConstantDouble) {
			value = ((TokenConstantDouble) token).getValue();
		} else if (token instanceof TokenString) {
			final String str = ((TokenString) token).getValue();
			value = str;
		} else {
			throw new RuntimeException("Unsupported token (" + token.asText()
					+ ") for value representation!");
		}

		return value;
	}

	/**
	 * Executes for loop node
	 */
	@Override
	public void visitForLoopNode(ForLoopNode node) {
		final TokenVariable var = node.getVariable();
		final String varName = var.getName();

		final Object startValue = this.getValueForToken(node
				.getStartExpression());
		final Object endValue = this.getValueForToken(node.getEndExpression());
		Object stepValue = node.getStepExpression();
		if (stepValue == null) {
			stepValue = 1;
		} else {
			stepValue = this.getValueForToken((Token) stepValue);
		}

		// testiraj beskonačnu petlju
		final ValueWrapper range = new ValueWrapper(endValue);
		range.decrement(startValue);
		range.multiply(1.0);
		final Double r = (Double) range.getValue();
		final ValueWrapper stepValueWrapper = new ValueWrapper(stepValue);
		stepValueWrapper.multiply(1.0);
		final Double s = (Double) stepValueWrapper.getValue();
		if (s == 0) {
			throw new RuntimeException("Infinitive loop");
		}
		if (r * s < 0.0) {
			throw new RuntimeException("Infinitive loop");
		}

		final ValueWrapper start = new ValueWrapper(startValue);
		final ValueWrapper end = new ValueWrapper(endValue);
		final ValueWrapper step = new ValueWrapper(stepValue);

		for (this.variables.push(varName, start); this.variables.peek(varName)
				.numCompare(end) <= 0; this.variables.peek(varName).increment(
				step)) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}

		this.variables.pop(varName);
	}

	/**
	 * Executes echo node
	 */
	@Override
	public void visitEchoNode(EchoNode node) {
		final Stack<Object> tempStack = new Stack<>();
		final ITokenVisitor visitor = new EchoTokenVisitor(tempStack,
				this.variables, this.context);

		for (final Token token : node.getTokens()) {
			token.accept(visitor);
		}

		final Stack<Object> reverseStack = new Stack<>();

		while (!tempStack.isEmpty()) {
			reverseStack.push(tempStack.pop());
		}

		while (!reverseStack.isEmpty()) {
			try {
				this.context.write(reverseStack.pop().toString());
			} catch (final IOException e) {
				throw new RuntimeException("Cannot write to output stream");
			}
		}
	}

	/**
	 * Executes document node
	 */
	@Override
	public void visitDocumentNode(DocumentNode node) {
		for (int i = 0; i < node.numberOfChildren(); i++) {
			node.getChild(i).accept(this);
		}
	}

	/**
	 * Do nothing
	 */
	@Override
	public void visitEndNode(EndNode node) {
	}

}
