package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class SmartHttpServer is simple HTTP server. Except of sending documents, it
 * can also dynamically generate HTML documents form .smsrc scripts or from java
 * classes that implement {@link IWebWorker} interface.
 * 
 * @author Nikola Sekulić
 * 
 */
public class SmartHttpServer {

	/**
	 * IP address of server
	 */
	private final String address;

	/**
	 * Port of server
	 */
	private int port;

	/**
	 * Number of threads that processes HTTP requests
	 */
	private int workerThreads;

	/**
	 * Lifetime of session in seconds
	 */
	private int sessionTimeout;

	/**
	 * Mime types of request. Key of map is file extension, and value is mime
	 * type. Example key=png, value = "image/png"
	 */
	private final Map<String, String> mimeTypes = new HashMap<String, String>();

	/**
	 * Thread that accepts TCP connections, and forwards request to workers.
	 */
	private final ServerThread serverThread;

	/**
	 * Thread pool for workers
	 */
	private ExecutorService threadPool;

	/**
	 * Thread that removes expired sessions
	 */
	private final CleanerThread cleaner;

	/**
	 * Path to root folder of server
	 */
	private final Path documentRoot;

	/**
	 * Map that contains workers. Key of map is requested URL form HTTP request
	 * that worker already served.
	 */
	private final Map<String, IWebWorker> workersMap;

	/**
	 * Map of sessions. Key is cookie name, and value is session.
	 */
	private final Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<String, SmartHttpServer.SessionMapEntry>();

	/**
	 * Random object for generating session id.
	 */
	private final Random sessionRandom = new Random();

	/**
	 * Socket that accepts HTTP request in ServerThread.
	 */
	ServerSocket serverSocket = null;

	/**
	 * Constructor. Creates new server. Reads server's parameter from properties
	 * file. Mandatory parameters are: INET address, port, path to file with
	 * mime-type definitions, number of threads that process request, lifetime
	 * of cookies, and root directory of server.
	 * 
	 * @param configFileName
	 *            path of file with configuration parameters
	 */
	public SmartHttpServer(String configFileName) {

		final Properties serverProperties = new Properties();
		BufferedReader reader = null;

		try {
			reader = Files.newBufferedReader(Paths.get(configFileName),
					StandardCharsets.ISO_8859_1);
		} catch (final IOException e) {
			throw new RuntimeException("Cannot open properties file: "
					+ configFileName, e);
		}

		try {
			serverProperties.load(reader);
		} catch (final IOException e) {
			throw new RuntimeException("Cannot load properties from file: "
					+ configFileName, e);
		}

		if (!serverProperties.containsKey("server.address")) {
			throw new RuntimeException("Property server.address is missing!");
		}

		this.address = serverProperties.getProperty("server.address");

		if (!serverProperties.containsKey("server.port")) {
			throw new RuntimeException("Property server.port is missing!");
		}

		try {
			this.port = Integer.parseInt(serverProperties
					.getProperty("server.port"));
		} catch (final NumberFormatException e) {
			throw new RuntimeException(
					"server.port in properties is not integer number", e);
		}

		if (!serverProperties.containsKey("server.workerThreads")) {
			throw new RuntimeException(
					"Property server.workerThreads is missing!");
		}

		if (!serverProperties.containsKey("session.timeout")) {
			throw new RuntimeException("Property session.timeout is missing!");
		}

		try {
			this.sessionTimeout = Integer.parseInt(serverProperties
					.getProperty("session.timeout"));
		} catch (final NumberFormatException e) {
			throw new RuntimeException(
					"session.timeout in properties is not integer number", e);
		}

		try {
			this.workerThreads = Integer.parseInt(serverProperties
					.getProperty("server.workerThreads"));
		} catch (final NumberFormatException exception) {
			System.out.println("ERROR:");
			System.out
					.println("server.workerThreads in properties is not integer number");
			throw new RuntimeException("server.workerThreads in properties is not integer number");
		}

		if (!serverProperties.containsKey("server.documentRoot")) {
			throw new RuntimeException(
					"Property server.documentRoot is missing!");
		}

		this.documentRoot = Paths.get(serverProperties
				.getProperty("server.documentRoot"));

		if (!Files.exists(this.documentRoot, LinkOption.NOFOLLOW_LINKS)
				|| !Files.isDirectory(this.documentRoot,
						LinkOption.NOFOLLOW_LINKS)) {
			System.out.println("ERROR:");
			System.out.println("Document root doesn't exist");
			throw new RuntimeException("Document root doesn't exist");
		}

		if (!serverProperties.containsKey("server.mimeConfig")) {
			throw new RuntimeException("Property server.mimeConfig is missing!");
		}

		final Path mimeConfigPath = Paths.get(serverProperties
				.getProperty("server.mimeConfig"));

		if (!Files.exists(mimeConfigPath, LinkOption.NOFOLLOW_LINKS)) {
			throw new RuntimeException(
					"Mime type properties file doesn't exist! fileName: "
							+ mimeConfigPath.getFileName());
		}

		final Properties mimeProperties = new Properties();
		BufferedReader mimeReader = null;
		try {
			mimeReader = Files.newBufferedReader(mimeConfigPath,
					StandardCharsets.ISO_8859_1);
		} catch (final IOException e) {
			throw new RuntimeException(
					"Cannnot open mimeConfig file! fileName: "
							+ mimeConfigPath.getFileName(), e);
		}
		try {
			mimeProperties.load(mimeReader);
		} catch (final IOException e) {
			throw new RuntimeException(
					"Cannot load properties from mymeConfig file! filaName: "
							+ mimeConfigPath.getFileName(), e);
		}

		for (final Object mime : mimeProperties.keySet()) {

			this.mimeTypes.put(((String) mime).toLowerCase(), mimeProperties
					.getProperty((String) mime).toLowerCase());
		}

		this.workersMap = new HashMap<>();

		if (serverProperties.containsKey("server.workers")) {
			final String workersPath = serverProperties
					.getProperty("server.workers");

			try (BufferedReader wReader = Files.newBufferedReader(
					Paths.get(workersPath), StandardCharsets.UTF_8)) {
				while (true) {
					String line = wReader.readLine();
					if (line == null) {
						break;
					}

					final int comment = line.indexOf('#');
					if (comment != -1) {
						line = line.substring(0, comment).trim();
					}

					if (!line.isEmpty()) {

						final String[] parts = line.split("=");

						if (parts.length != 2) {
							throw new RuntimeException("Error in file "
									+ workersPath
									+ ". Two charcter '=' in same line");
						}

						final String path = parts[0].trim();
						final String fqcn = parts[1].trim();

						if (workersPath.contains(path)) {
							throw new RuntimeException(
									"Cannot laod workers from file: "
											+ workersPath
											+ "! Two worker have same path");
						}

						Class<?> referenceToClass;
						try {
							referenceToClass = this.getClass().getClassLoader()
									.loadClass(fqcn);
						} catch (final ClassNotFoundException e) {
							throw new RuntimeException(
									"Cannot laod workers from file: "
											+ workersPath
											+ "! Unknown worker class");
						}
						Object newObject;
						try {
							newObject = referenceToClass.newInstance();
						} catch (final Exception e) {
							throw new RuntimeException(
									"Unable to laod workers from file: "
											+ workersPath);
						}
						final IWebWorker iww = (IWebWorker) newObject;
						this.workersMap.put(path, iww);
					}
				}
			} catch (final IOException e) {
				throw new RuntimeException("Cannot open properties file: "
						+ workersPath, e);
			}
		}

		this.serverThread = new ServerThread();
		this.cleaner = new CleanerThread();

	}

	/**
	 * Starts server. Creates main thread which forwards requests to workers,
	 * and cleaner thread which removes outdated cookies.
	 */
	protected synchronized void start() {

		// … start server thread if not already running …
		if (!this.serverThread.isAlive()) {
			this.serverThread.start();
		}

		if (!this.cleaner.isAlive()) {
			this.cleaner.start();
		}

		// … init threadpool by Executors.newFixedThreadPool(...); …

		this.threadPool = Executors.newFixedThreadPool(this.workerThreads);

	}

	/**
	 * Stops server. Stops main and cleaner thread. Workers are stopped after
	 * they are done with job.
	 */
	protected synchronized void stop() {

		stop = true;
		// … signal server thread to stop running …
		while (!this.serverThread.isInterrupted()) {
			this.serverThread.interrupt();
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
		}

		while (!this.cleaner.isInterrupted()) {
			this.cleaner.interrupt();
		}

		// … shutdown threadpool …
		this.threadPool.shutdown();
	}

	/**
	 * Generate session id
	 * 
	 * @return String with 20 characters of upper-case letters, pseudo-randomly
	 *         generated.
	 */
	private synchronized String generateSessionID() {
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 20; i++) {
			final char c = (char) (this.sessionRandom.nextInt(26) + 65);
			sb.append(c);
		}

		return sb.toString();
	}

	/**
	 * Main thread of server.
	 * 
	 * @author Nikola Sekulić
	 * 
	 */
	protected class ServerThread extends Thread {

		/**
		 * Accepts request from users and forwards them to workers.
		 */
		@Override
		public void run() {

			try {
				serverSocket = new ServerSocket(SmartHttpServer.this.port, 50,
						InetAddress.getByName(SmartHttpServer.this.address));
			} catch (final IOException e) {
				throw new RuntimeException(
						"Cannot create server socket at specified port");
			}

			while (true) {

				Socket client = null;

				if (stop) {
					break;
				}

				try {
					client = serverSocket.accept();
				} catch (final IOException e) {
				}

				if (client != null) {
					final ClientWorker worker = new ClientWorker(client);
					SmartHttpServer.this.threadPool.submit(worker);
				}
			}

			try {
				serverSocket.close();
			} catch (final IOException e) {
			}
		}
	}

	/**
	 * Signal to stop server
	 */
	private volatile boolean stop = false;

	/**
	 * Cleaner thread of server.
	 * 
	 * @author Nikola Sekulić
	 * 
	 */
	protected class CleanerThread extends Thread {

		/**
		 * Periodically after each one minute cleans expired sessions.
		 */
		@Override
		public void run() {

			while (true) {

				if (stop) {
					break;
				}

				try {
					Thread.sleep(1000 * 60);
				} catch (final InterruptedException e) {
					if (stop) {
						break;
					}
				}

				for (final String session : SmartHttpServer.this.sessions
						.keySet()) {
					if (SmartHttpServer.this.sessions.get(session).validUntil < System
							.currentTimeMillis()) {
						SmartHttpServer.this.sessions.remove(session);
					}
				}
			}
		}
	}

	/**
	 * Worker thread. Accepts job from main thread and generate HTTP response.
	 * 
	 * @author Nikola Sekulić
	 * 
	 */
	private class ClientWorker implements Runnable {

		/**
		 * Socket for communication wuth client
		 */
		private final Socket csocket;

		/**
		 * Stream for input data
		 */
		private PushbackInputStream istream;

		/**
		 * Stream for output data
		 */
		private OutputStream ostream;

		/**
		 * Parameters from HTTP request.
		 */
		private Map<String, String> params = new HashMap<String, String>();

		/**
		 * Permanent parameters from HTTP request.
		 */
		private final Map<String, String> permParams = new HashMap<>();

		/**
		 * Persistent parameters for RequestContext
		 */
		private final List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();

		/**
		 * Session ID
		 */
		private String SID;

		/**
		 * Constructor
		 * 
		 * @param csocket
		 *            socket on which response is sent.
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		/**
		 * Reads all line from requests header, except last line which is empty
		 * line.
		 * 
		 * @return lines of header
		 */
		private List<String> readRequestHeaderLines() {
			final List<String> lines = new ArrayList<>();

			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(this.istream,
							StandardCharsets.US_ASCII));

			while (true) {
				try {
					final String line = reader.readLine();
					if (line == null || line.trim().isEmpty()) {
						break;
					} else {
						lines.add(line.trim());
					}
				} catch (final IOException e) {
				}
			}

			return lines;
		}

		/**
		 * Send information of invalid request. Generates HtML response with
		 * provided status code and provided status message, and sends it to
		 * client.
		 * 
		 * @param status
		 *            response status
		 * @param message
		 *            response message
		 */
		private void respondToInvalidRequest(int status, String message) {

			final RequestContext context = new RequestContext(this.ostream,
					this.params, this.permParams, this.outputCookies);
			context.setStatusCode(status);
			context.setStatusText("INVALID REQUEST");
			context.setEncoding("utf-8");
			context.setMimeType("text/plain");
			try {
				context.write(Integer.toString(status) + " " + message);
			} catch (final IOException e) {
				return;
			}

			try {
				this.csocket.close();
			} catch (final IOException e) {
			}
		}

		/**
		 * Parse url GET parameters and stores into params map.
		 * 
		 * @param parameters
		 *            GET parameters from url
		 */
		private void parseURLParameters(String parameters) {
			if (this.params == null) {
				this.params = new HashMap<>();
			}
			final String[] urlParams = parameters.split("\\Q&\\E");
			for (String p : urlParams) {
				p = p.trim();
				if (!p.isEmpty()) {
					final String[] paramParts = p.split("=");
					final String name = paramParts[0].trim();
					String value = "";
					if (paramParts.length > 1) {
						value = paramParts[1];
						this.params.put(name, value);
					}
				}
			}

		}

		/**
		 * Executes job. Check if client has valid cookie. If it has, retrieves
		 * data from previous session.
		 */
		@Override
		public void run() {

			try {
				this.csocket.setSoTimeout(60 * 1000);
			} catch (final SocketException e1) {
				return;
			}

			// obtain input stream from socket and wrap it to pushback input
			// stream
			try {
				this.istream = new PushbackInputStream(
						this.csocket.getInputStream());
			} catch (final IOException e) {
				return;
			}

			// obtain output stream from socket
			try {
				this.ostream = this.csocket.getOutputStream();
			} catch (final IOException e) {
				return;
			}

			// Then read complete request header from your client in separate
			// method...

			final List<String> request = this.readRequestHeaderLines();

			final List<SessionMapEntry> cookies = this.findSesssions(request);

			if (cookies == null || cookies.isEmpty()) {

				final String id = SmartHttpServer.this.generateSessionID();
				this.SID = id;

				String cookieDomain = this.getDomainFormRequest(request);

				if (cookieDomain.equalsIgnoreCase("localhost")) {
					cookieDomain = null;
				}

				this.outputCookies.add(new RCCookie(id, id, null, cookieDomain,
						"/"));

				SmartHttpServer.this.sessions.put(id, new SessionMapEntry(id,
						System.currentTimeMillis()
								+ SmartHttpServer.this.sessionTimeout * 1000,
						this.params));
			} else {
				this.getParametersFromSessions(cookies);
			}

			// If header is invalid (less then a line at least) return response
			// status 400

			if (request.size() == 0) {
				this.respondToInvalidRequest(400, "Invalid request");
				return;
			}

			// Extract (method, requestedPath, version) from firstLine
			final String firstLine = request.get(0);
			final String[] firstLineParts = firstLine.trim().split("\\s+");

			if (firstLineParts.length != 3) {
				this.respondToInvalidRequest(400, "Invalid request");
				return;
			}

			// if method not GET or version not HTTP/1.0 or HTTP/1.1 return
			// response status 400

			if (!firstLineParts[0].equalsIgnoreCase("GET")) {
				this.respondToInvalidRequest(400,
						"Only GET method is supported");
				return;
			}

			final String version = firstLineParts[2];
			if (!version.equalsIgnoreCase("HTTP/1.0")
					&& !version.equalsIgnoreCase("HTTP/1.1")) {
				this.respondToInvalidRequest(400,
						"Invalid version of HTTP protocol!");
				return;
			}

			// String path; String paramString;
			// (path, paramString) = split requestedPath to path and
			// parameterString
			final String[] urlParts = firstLineParts[1].trim().split("\\?");
			String path = urlParts[0];

			if (path.startsWith("/")) {
				path = path.substring(1);
			}

			if (path.endsWith("/")) {
				path = path.substring(0, path.length() - 1);
			}

			if (path.isEmpty()) {
				path = "index.html";
			}

			// parseParameters(paramString); ==> your method to fill map
			// parameters
			if (urlParts.length > 1) {
				this.parseURLParameters(urlParts[1]);
			}

			if (SmartHttpServer.this.workersMap.containsKey("/" + path)) {
				SmartHttpServer.this.workersMap.get("/" + path).processRequest(
						new RequestContext(this.ostream, this.params,
								this.permParams, this.outputCookies));
				try {
					this.csocket.close();
				} catch (final IOException e) {
				}
				return;

			} else {
				if (path.matches("ext/[\\w]*")) {
					final String workerName = path.split("/")[1];
					this.executeWorker(workerName);
					try {
						this.csocket.close();
					} catch (final IOException e) {
					}
					return;
				}
			}
			// requestedPath = resolve path with respect to documentRoot
			// if requestedPath is not below documentRoot, return response
			// status 403 forbidden

			String webRoot;
			try {
				webRoot = SmartHttpServer.this.documentRoot.toFile()
						.getCanonicalPath();
			} catch (final IOException e) {
				return;
			}

			final Path canonicalPath = SmartHttpServer.this.documentRoot
					.resolve(path);

			String requestPath;
			try {
				requestPath = canonicalPath.toFile().getCanonicalPath();
			} catch (final IOException e) {
				return;
			}

			if (!requestPath.startsWith(webRoot)) {
				this.respondToInvalidRequest(403, "FORBIDDEN");
				return;
			}

			// check if requestedPath exists, is file and is readable; if not,
			// return status 404
			final Path finalPath = Paths.get(requestPath);
			if (!Files.exists(finalPath, LinkOption.NOFOLLOW_LINKS)
					|| !Files.isReadable(finalPath)
					|| Files.isDirectory(finalPath, LinkOption.NOFOLLOW_LINKS)) {
				this.respondToInvalidRequest(404, "File not found!");
				return;
			}

			// else extract file extension
			final String fileName = finalPath.getFileName().toString();
			final int point = fileName.lastIndexOf('.');

			String extension = null;
			if (point != -1 && point != fileName.length() - 1) {
				extension = fileName.substring(point + 1).toLowerCase();
			}

			if (extension != null && extension.equalsIgnoreCase("smscr")) {
				this.executeScript(finalPath);
			} else {
				this.sendFile(finalPath, extension);
			}

			try {
				this.csocket.close();
			} catch (final IOException e) {
			}
		}

		/**
		 * Sends file to client. With extension defines mime-type in response.
		 * 
		 * @param path
		 *            path of file
		 * @param extension
		 *            extension of file.
		 */
		private void sendFile(Path path, String extension) {
			// find in mimeTypes map appropriate mimeType for current file
			// extension
			// (you filled that map during the construction of
			// SmartHttpServer
			// from mime.properties)
			// if no mime type found, assume application/octet-stream

			String mime = "application/octet-stream";
			if (extension != null) {
				if (SmartHttpServer.this.mimeTypes.containsKey(extension)) {
					mime = SmartHttpServer.this.mimeTypes.get(extension);
				}
			}

			// create a rc = new RequestContext(...); set mime-type; set
			// status
			// to 200
			// If you want, you can modify RequestContext to allow you to
			// add
			// additional headers
			// so that you can add “Content-Length: 12345” if you know that
			// file
			// has 12345 bytes

			final RequestContext context = new RequestContext(this.ostream,
					this.params, this.permParams, this.outputCookies);
			try {
				context.setContentLength(Files.size(path));
			} catch (final IOException e1) {
			}

			context.setStatusCode(200);
			context.setMimeType(mime);

			// open file, read its content and write it to rc (that will
			// generate header and send
			// file bytes to client)

			BufferedInputStream fileStream = null;
			final byte[] buffer = new byte[4096];
			try {
				fileStream = new BufferedInputStream(Files.newInputStream(path,
						StandardOpenOption.READ));

				while (true) {
					final int bytes = fileStream.read(buffer);
					if (bytes == -1) {
						break;
					}

					context.write(Arrays.copyOf(buffer, bytes));
				}
			} catch (final IOException e) {
			}

			try {
				if(fileStream != null) {
					fileStream.close();
				}
			} catch (final Exception e) {
			}

		}

		/**
		 * Executes worker {@link IWebWorker}.
		 * 
		 * @param workerName
		 *            name of worker.
		 */
		private void executeWorker(String workerName) {
			final String className = workerName;
			Class<?> referenceToClass = null;
			try {
				referenceToClass = this
						.getClass()
						.getClassLoader()
						.loadClass(
								"hr.fer.zemris.java.webserver.workers."
										+ className);
			} catch (final ClassNotFoundException e) {
				this.respondToInvalidRequest(404, "File not found!");
				try {
					this.csocket.close();
				} catch (final IOException e1) {
				}
				return;
			}
			Object newObject = null;
			try {
				newObject = referenceToClass.newInstance();
			} catch (final Exception e) {
				this.respondToInvalidRequest(404, "File not found!");
				try {
					this.csocket.close();
				} catch (final IOException e1) {
				}
				return;
			}

			final IWebWorker iww = (IWebWorker) newObject;
			iww.processRequest(new RequestContext(this.ostream, this.params,
					this.permParams, this.outputCookies));
			try {
				this.csocket.close();
			} catch (final IOException e) {
			}

		}

		/**
		 * Executes .smsrc script and write result to HTTP response.
		 * 
		 * @param finalPath
		 *            path of script
		 */
		private void executeScript(Path finalPath) {

			String errorMessage = null;

			final RequestContext context = new RequestContext(this.ostream,
					this.params, this.permParams, this.outputCookies);
			context.setMimeType("text/plain");

			List<String> lines = null;
			try {
				lines = Files.readAllLines(finalPath, StandardCharsets.UTF_8);
			} catch (final IOException e) {
				errorMessage = "Cannot acces to script!";
				try {
					context.write(errorMessage);
				} catch (final IOException e1) {
				}
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
				errorMessage = "SYNTAX ERROR IN SCRIPT!\r\n" + e.getMessage();
				try {
					context.write(errorMessage);
				} catch (final IOException e1) {
				}
				return;
			} catch (final Exception e) {
				errorMessage = "UNKNOWN ERROR IN SCRIPT!\r\n" + e.getMessage();
				try {
					context.write(errorMessage);
				} catch (final IOException e1) {
				}
				return;
			}

			final SmartScriptEngine engine = new SmartScriptEngine(
					parser.getDocumentNode(), context);

			try {
				engine.execute();
				if (this.SID != null
						&& SmartHttpServer.this.sessions
								.containsKey(this.SID)) {
					for (final String name : context
							.getPersistentParameterNames()) {
						SmartHttpServer.this.sessions.get(this.SID).map
								.put(name, context
										.getPersistentParameter(name));
					}
				}
			} catch (final Exception e) {
				errorMessage = "EXECUTION ERROR IN SCRIPT!\r\n"
						+ e.getMessage();
				try {
					context.write(errorMessage);
				} catch (final IOException e1) {
				}
				return;
			}

		}

		/**
		 * Finds if new client has valid stored cookie.
		 * 
		 * @param requestLines
		 *            lines from HTTP request.
		 * @return list of sessions in which client has got cookie that is still
		 *         valid. If stored session for client is outdated, removes it
		 *         from server, else, extend valid time for session.
		 */
		private List<SessionMapEntry> findSesssions(List<String> requestLines) {

			final List<SessionMapEntry> list = new ArrayList<>();

			final Set<String> checkedIDs = new HashSet<>();

			for (String line : requestLines) {
				if (line.trim().startsWith("Cookie:")
						|| line.trim().startsWith("cookie:")) {
					line = line.substring(7).trim();

					final String[] cookies = line.split(";");

					for (final String cookie : cookies) {
						final String[] cookieParts = cookie.trim().split(
								"=");
						final String id = cookieParts[0].trim();

						if (!checkedIDs.contains(id)) {
							if (SmartHttpServer.this.sessions
									.containsKey(id)) {
								final SessionMapEntry session = SmartHttpServer.this.sessions
										.get(id);
								if (session.validUntil < System
										.currentTimeMillis()) {
									SmartHttpServer.this.sessions
											.remove(id);
								} else {
									session.validUntil = System
											.currentTimeMillis()
											+ 1000
											* SmartHttpServer.this.sessionTimeout;
									list.add(session);
								}
							}
							checkedIDs.add(id);
						}
					}
				}
			}

			return list;
			
		}

		/**
		 * Gets parameter from stored sessions.
		 * 
		 * @param sessions
		 *            list of sessions.
		 */
		private synchronized void getParametersFromSessions(List<SessionMapEntry> sessions) {
			for (final SessionMapEntry session : sessions) {
				for (final String key : session.map.keySet()) {
					final String value = session.map.get(key);
					this.permParams.put(key, value);
				}
				this.SID = session.sid;
			}
		}

		/**
		 * Gets domain from HTTP request ("Host:" parameter)
		 * 
		 * @param requestLines
		 *            lines of request
		 * @return domain from request.
		 */
		private String getDomainFormRequest(List<String> requestLines) {
			String domain = null;
			for (final String line : requestLines) {
				if (line.trim().startsWith("Host:")
						|| line.trim().startsWith("host:")) {
					domain = line.split(":")[1].trim();

				}
			}

			if (domain == null) {
				domain = SmartHttpServer.this.address;
			}

			return domain;
		}
	}

	/**
	 * Session for server and client.
	 * 
	 * @author Nikola Sekulić
	 * 
	 */
	public static class SessionMapEntry {
		/**
		 * Identifier of session
		 */
		String sid;

		/**
		 * Time after which session is outdated..
		 */
		long validUntil;

		/**
		 * Stored parameters of session.
		 */
		Map<String, String> map;

		/**
		 * Constructor
		 * 
		 * @param sid
		 *            ID of session
		 * @param validUntil
		 *            vaid time
		 * @param parameters
		 *            parameters of session.
		 */
		public SessionMapEntry(String sid, long validUntil,
				Map<String, String> parameters) {
			this.sid = sid;
			this.validUntil = validUntil;
			this.map = new ConcurrentHashMap<>(parameters);
		}
	}

	/**
	 * Main method of program. Starts server.
	 * 
	 * @param args
	 *            one argument is expected, path to file with properties for
	 *            server
	 */
	public static void main(String[] args) {

		String propertiesPath = null;

		if (args.length != 1) {
			System.out
					.println("One parameter is required: path to server properties file!");

			System.out
					.println("Server will use default path './properties/server.properties'");
			propertiesPath = "./properties/server.properties";

		} else {
			propertiesPath = args[0];
		}

		SmartHttpServer server = null;

		try {
			server = new SmartHttpServer(propertiesPath);
		} catch (final Exception e) {
			System.out.println("ERROR:");
			System.out.println(e.getMessage());
			// e.printStackTrace();
			return;
		}

		server.start();

		System.out.println("Enter stop to stop server: ");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in, StandardCharsets.UTF_8));

		while (true) {

			System.out.print("> ");
			String command = null;
			try {
				command = reader.readLine();
			} catch (IOException e) {
				continue;
			}

			if (command !=  null && command.trim().equalsIgnoreCase("stop")) {
				server.stop();
				break;
			} else {
				System.err.println("Uknown command");
			}
		}

	}
}
