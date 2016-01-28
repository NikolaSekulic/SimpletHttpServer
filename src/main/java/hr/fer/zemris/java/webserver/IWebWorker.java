package hr.fer.zemris.java.webserver;

/**
 * Interface defines request generator for HTTP response.
 * 
 * @author Nikola Sekulić
 * 
 */
public interface IWebWorker {

	/**
	 * Writes worker's result to provided context's output stream.
	 * 
	 * @param context
	 *            context for HTTP request
	 */
	public void processRequest(RequestContext context);
}
