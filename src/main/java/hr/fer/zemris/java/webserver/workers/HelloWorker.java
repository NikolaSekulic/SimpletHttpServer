package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * HelloWorker generates html with hello message, current time on server, and
 * length of parameter name form GET parameters of request. Sends generated html
 * to client. If parameter name doesn't exist, writes message 'You did not send
 * me your name!' instead of length.
 * 
 * @author Nikola Sekulić
 * 
 */
public class HelloWorker implements IWebWorker {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRequest(RequestContext context) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final Date now = new Date(System.currentTimeMillis());
		context.setMimeType("text/html");
		final String name = context.getParameter("name");
		try {
			context.write("<html><body>");
			context.write("<h1>Hello!!!</h1>");
			context.write("<p>Now is: " + sdf.format(now) + "</p>");
			if (name == null || name.trim().isEmpty()) {
				context.write("<p>You did not send me your name!</p>");
			} else {
				context.write("<p>Your name has " + name.trim().length()
						+ " letters.</p>");
			}
			context.write("</body></html>");
		} catch (final IOException ex) {
		}
	}
}
