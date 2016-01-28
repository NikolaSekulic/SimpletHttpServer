package hr.fer.zemris.java.custom.scripting.exec.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Demo program for {@link SmartScriptEngine}. Engine executes script
 * fibonacci.smscr in folder './webroot/scripts'. Arguments of program are
 * ignored.
 * 
 * @author Nikola Sekulić
 *
 */
public class SmartScriptEngineDemo4 {

	/**
	 * Main method of program.
	 * 
	 * @param args
	 *            program arguments.
	 */
	public static void main(String[] args) {
		String documentBody = null;

		try {
			documentBody = new String(Files.readAllBytes(Paths
					.get("./webroot/scripts/fibonacci.smscr")),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err
					.println("Cannot read script from file ./webroot/scripts/fibonacci.smscr");
			return;
		}
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters,
						persistentParameters, cookies)).execute();
	}
}