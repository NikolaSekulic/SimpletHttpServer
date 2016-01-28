package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.EndNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Program for {@link SmartScriptParser} demonstration. Program expects one
 * argument, path to file with script. Program reads script, parses it to nodes,
 * and writes reconstructs script text from parsed nodes.
 * 
 * @author Nikola Sekulić
 *
 */
public class TreeWriter {

	/**
	 * Main method of program.
	 * 
	 * @param args
	 *            arguments of program
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("One argumment is expected: path to script");
			return;
		}

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(args[0]),
					StandardCharsets.UTF_8);
		} catch (final IOException e) {
			System.out.println("Cannot acces to file " + args[0] + " !");
			return;
		}

		final StringBuilder sb = new StringBuilder();
		for (final String line : lines) {
			sb.append(line).append("\r\n");
		}

		final String program = sb.toString();

		SmartScriptParser parser = null;

		try {

			parser = new SmartScriptParser(program);

		} catch (final SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.out.println(e.getMessage());
			System.err.println("ERROR");
			return;
		} catch (final Exception e) {
			System.out.println("Unknown exception :(");
			e.printStackTrace();
			return;
		}

		final DocumentNode document = parser.getDocumentNode();
		final WriterVisitor visitor = new WriterVisitor();
		document.accept(visitor);
		System.out.println(visitor.getResult());

	}

	/**
	 * Writer visitor is used for reconstructing original script from syntax
	 * tree of script.
	 * 
	 * @author Nikola Sekulić
	 * 
	 */
	public static class WriterVisitor implements INodeVisitor {

		/**
		 * String builder for content of reconstructed script.
		 */
		private final StringBuilder sb;

		/**
		 * Constructor
		 */
		public WriterVisitor() {
			this.sb = new StringBuilder();
		}

		/**
		 * Reconstructs TextNode.
		 */
		@Override
		public void visitTextNode(TextNode node) {
			this.sb.append(node.getText());
		}

		/**
		 * Reconstructs for loop node.
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			this.sb.append(node.toString());
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
			this.sb.append(new EndNode().toString());
		}

		/**
		 * Reconstructs Echo node.
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			this.sb.append(node.toString());
		}

		/**
		 * Reconstructs Document node.
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}

		/**
		 * Reconstructs EndNode.
		 */
		@Override
		public void visitEndNode(EndNode node) {
			this.sb.append(node.toString());
		}

		/**
		 * Returns reconstructed text of script.
		 * 
		 * @return text of script
		 */
		public String getResult() {
			return this.sb.toString();
		}

	}
}
