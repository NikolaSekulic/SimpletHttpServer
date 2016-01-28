package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class RequestContext represents tool for generating response on HTTP request.
 * 
 * @author Nikola Sekulić
 * 
 */
public class RequestContext {

	/**
	 * Output stream
	 */
	private final OutputStream outputStream;

	/**
	 * Charset for output stream
	 */
	private Charset charset = StandardCharsets.UTF_8;

	/**
	 * Encoding of HTML body
	 */
	private String encoding = "UTF-8";

	/**
	 * Status code of HTTP response
	 */
	private int statusCode = 200;

	/**
	 * Status message of HTTP response, comes after status code
	 */
	private String statusText = "OK";

	/**
	 * Mime type
	 */
	private String mimeType = "text/html";

	/**
	 * Parameters of Context
	 */
	private Map<String, String> parameters;

	/**
	 * Content-Length header parameter
	 */
	private Long contentLength;

	/**
	 * Temporary parameters of context
	 */
	private Map<String, String> temporaryParameters;

	/**
	 * Persistent parameters of context
	 */
	private Map<String, String> persistentParameters;

	/**
	 * List of cookies
	 */
	private List<RCCookie> outputCookies;

	/**
	 * Flag that signals if header is generated
	 */
	private boolean headerGenerated = false;

	/**
	 * Constructor. Input collections are copied.
	 * 
	 * @param outputStream
	 *            output stream on which response will be written
	 * @param parameters
	 *            parameters of context
	 * @param persistentParameters
	 *            persistent parameters of context
	 * @param outputCookies
	 *            cookies for HTTP response.
	 * 
	 * @throws IllegalArgumentException
	 *             if outputStream is null
	 */
	public RequestContext(OutputStream outputStream, // must not be null!
			Map<String, String> parameters, // if null, treat as empty
			Map<String, String> persistentParameters, // if null, treat as empty
			List<RCCookie> outputCookies) { // if null, treat as empty

		if (outputStream == null) {
			throw new IllegalArgumentException("Output stream cannot be null!");
		}

		this.outputStream = outputStream;

		if (parameters == null) {
			this.parameters = new HashMap<>();
		} else {
			this.parameters = new HashMap<>(parameters);
		}

		this.temporaryParameters = new HashMap<>();

		if (persistentParameters == null) {
			this.persistentParameters = new HashMap<>();
		} else {
			this.persistentParameters = new HashMap<>(persistentParameters);
		}

		if (outputCookies == null) {
			this.outputCookies = new ArrayList<>();
		} else {
			this.outputCookies = new ArrayList<>(outputCookies);
		}

	}

	/**
	 * Sets content length in header.
	 * 
	 * @param contentLength
	 *            content length
	 * @return this RequestContext
	 */
	public RequestContext setContentLength(long contentLength) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Cannot change content length after header is generated");
		}
		this.contentLength = contentLength;
		return this;
	}

	/**
	 * Sets encoding of request.
	 * 
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Cannot change encoding after header is generated");
		}
		this.encoding = encoding;
		this.charset = Charset.forName(encoding);
	}

	/**
	 * Sets status code of request.
	 * 
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Cannot change status code after header is generated");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Sets status text of response.
	 * 
	 * @param statusText
	 *            the statusText to set
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Cannot change status text after header is generated");
		}
		this.statusText = statusText;
	}

	/**
	 * Sets content type of request.
	 * 
	 * @param mimeType
	 *            the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException(
					"Cannot change mime type after header is generated");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Sets flag parameter generated
	 * 
	 * @param headerGenerated
	 *            the headerGenerated to set
	 */
	public void setHeaderGenerated(boolean headerGenerated) {
		this.headerGenerated = headerGenerated;
	}

	/**
	 * Returns parameter.
	 * 
	 * @param name
	 *            parameter's name
	 * @return parameter for specific name, or null if parameter woth provided
	 *         name doesn't exist
	 */
	public String getParameter(String name) {

		return parameters.get(name);

	}

	/**
	 * Returns names of all parameters.
	 * 
	 * @return names of all parameters as unmodifiable {@link Set}
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Returns persistent parameter for provided name.
	 * 
	 * @param name
	 *            of parameter
	 * @return parameter for provided name.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Returns names of all persistent parameters.
	 * 
	 * @return names of all persistent parameters as unmodifiable {@link Set}
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Adds or replaces persistent parameter.
	 * 
	 * @param name
	 *            name of parameter
	 * @param value
	 *            parameter
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Removes persistent parameter.
	 * 
	 * @param name
	 *            name of parameter to be removed
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Returns temporary parameter for provided name.
	 * 
	 * @param name
	 *            of parameter
	 * @return parameter for provided name.
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Returns names of all temporary parameters.
	 * 
	 * @return names of all temporary parameters as unmodifiable {@link Set}
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Adds or replaces temporary parameter.
	 * 
	 * @param name
	 *            name of parameter
	 * @param value
	 *            parameter
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Removes temporary parameter.
	 * 
	 * @param name
	 *            name of parameter to be removed
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Creates header. Sets version of HTTP to 1.1
	 * 
	 * @return byte representation of header.
	 */
	private byte[] createHeader() {

		StringBuilder sb = new StringBuilder();

		sb.append("HTTP/1.1 ").append(statusCode).append(' ')
				.append(statusText).append("\r\n");

		sb.append("Content-Type: ").append(this.mimeType);

		if (mimeType.startsWith("text/")) {
			sb.append("; ").append("charset=").append(encoding);
		}

		sb.append("\r\n");

		if (contentLength != null) {
			sb.append("Content-Length: ").append(this.contentLength)
					.append("\r\n");
		}

		for (RCCookie cookie : outputCookies) {
			sb.append("Set-Cookie: ").append(cookie.toString()).append("\r\n");
		}

		sb.append("\r\n");

		String header = sb.toString();

		return header.getBytes(StandardCharsets.US_ASCII);

	}

	/**
	 * Number of bytes written to output stream.
	 */
	private int writenSize = 0;

	/**
	 * Writes provided data to output stream. If flag headerGenerated is not
	 * set, sets that flag and writes header before provided data.
	 * 
	 * @param data
	 *            bytes to write
	 * @return this {@link RequestContext}
	 * @throws IOException
	 *             if IO error occurs
	 */
	public RequestContext write(byte[] data) throws IOException {

		if (contentLength != null) {
			if (writenSize + data.length > contentLength) {
				throw new RuntimeException("Cannot wite to output stream!\r\n"
						+ "Content length is limited to " + contentLength
						+ ".\r\n" + writenSize
						+ " bytes is already written, and size of new data is "
						+ data.length + " bytes.");
			}
		}

		if (!headerGenerated) {
			this.setHeaderGenerated(true);
			// System.out.println(new String(createHeader(),
			// StandardCharsets.US_ASCII));
			this.outputStream.write(createHeader());
		}
		this.outputStream.write(data);
		return this;
	}

	/**
	 * Converts provided string to bytes using encoding parameter from this
	 * RequestContext and writes that bytes to output stream. If flag
	 * headerGenerated is not set, sets that flag and writes header encoded with
	 * charset US_ASCII before provided string.
	 * 
	 * @param text
	 *            text o write
	 * @return this RequestContext
	 * @throws IOException
	 *             if IO error occurs
	 */
	public RequestContext write(String text) throws IOException {
		byte[] data = text.getBytes(this.charset);
		return write(data);
	}

	/**
	 * Adds cookie to context.
	 * 
	 * @param cookie
	 *            cookie to add
	 * @return this RequestContext
	 */
	public RequestContext addRCCookie(RCCookie cookie) {
		outputCookies.add(cookie);
		return this;
	}

	/**
	 * Class represent cookie in HTTP response.
	 * 
	 * @author Nikola Sekulić
	 * 
	 */
	public static class RCCookie {

		/**
		 * Name of cookie
		 */
		private final String name;
		/**
		 * Value of cookie
		 */
		private final String value;

		/**
		 * Domain of cookie
		 */
		private final String domain;

		/**
		 * Path of cookie
		 */
		private final String path;

		/**
		 * Lifetime of cookie in seconds.
		 */
		private final Integer maxAge;

		/**
		 * Constructor
		 * 
		 * @param name
		 *            name of cookie
		 * @param value
		 *            value of cookie
		 * @param domain
		 *            domain of cookie
		 * @param path
		 *            path of cookie
		 * @param maxAge
		 *            lifetime of cookie inn seconds
		 */
		public RCCookie(String name, String value, Integer maxAge,
				String domain, String path) {
			super();

			if (name == null) {
				throw new IllegalArgumentException(
						"Name if cookie cannot be null!");
			}

			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Name getter.
		 * 
		 * @return name of cookie
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Value getter.
		 * 
		 * @return value of cookie
		 */
		public String getValue() {
			return this.value;
		}

		/**
		 * Domain getter
		 * 
		 * @return domain of cookie
		 */
		public String getDomain() {
			return this.domain;
		}

		/**
		 * Path getter
		 * 
		 * @return path of cookie
		 */
		public String getPath() {
			return this.path;
		}

		/**
		 * Lifetime getter.
		 * 
		 * @return lifetime of cookie
		 */
		public Integer getMaxAge() {
			return this.maxAge;
		}

		/**
		 * Returns string representation of cookie, as it should be in HTTP
		 * response.
		 */
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();

			sb.append(this.name).append("=").append('"').append(this.value)
					.append('"');

			if (this.domain != null) {
				sb.append("; ").append("Domain=").append(this.domain);
			}

			if (this.path != null) {
				sb.append("; ").append("Path=").append(this.path);
			}

			if (this.maxAge != null) {
				sb.append("; ").append("Max-Age=").append(this.maxAge);
			}

			sb.append("; ").append("HttpOnly");

			return sb.toString();
		}
	}

}
