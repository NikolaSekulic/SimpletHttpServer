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
 * brojPoziva.smscr in folder './webroot/scripts'. Arguments of program are
 * ignored. Variable for script is brojPoziva, and it's value is 3.
 * 
 * @author Nikola Sekulić
 *
 */
public class SmartScriptEngineDemo3 {

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
					.get("./webroot/scripts/brojPoziva.smscr")),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err
					.println("Cannot read script from file ./webroot/scripts/brojPoziva.smscr");
			return;
		}
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters,
				persistentParameters, cookies);
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(), rc)
				.execute();
		System.out.println("Vrijednost u mapi: "
				+ rc.getPersistentParameter("brojPoziva"));
	}
}