package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.Set;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * IWebWorker EchoParams generates table with GET parameters form client
 * request, and sends it to client in HTML format. Table has two columns. In
 * first column are names of parameters, in second are values of parameters.
 * 
 * @author Nikola Sekulić
 * 
 */
public class EchoParams implements IWebWorker {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRequest(RequestContext context) {

		final Set<String> names = context.getParameterNames();
		final StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head><body>");
		sb.append("<h1>Parameters: </h1>");

		if (names == null || names.isEmpty()) {
			sb.append("Parameters are not provided!");
		} else {
			sb.append("<table border=\"1\">");
			for (final String name : names) {

				sb.append("<tr>");
				sb.append("<td>").append(name).append("</td>");
				sb.append("<td>").append(context.getParameter(name))
						.append("</td>");
				sb.append("</tr>");

			}
			sb.append("</table>");
		}

		sb.append("</body>").append("</html>");

		try {
			context.write(sb.toString());
		} catch (final IOException e) {
		}

	}

}
