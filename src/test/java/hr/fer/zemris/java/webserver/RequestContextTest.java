package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestContextTest {

	/**
	 * Converts hexadecimal string to byte array. Each two characters in string
	 * presents byte in hexadecimal radix.
	 *
	 * @param hexString
	 *            input string
	 * @return byte array
	 */
	public static byte[] hexStringToByteArray(String hexString) {

		byte[] array = new byte[hexString.length() / 2];

		for (int i = 0; i < (hexString.length() - 1); i += 2) {
			int number = Integer.parseInt(hexString.substring(i, i + 2), 16);
			array[i / 2] = (byte) (number & 0xff);
		}

		return array;
	}

	@Test
	public void testExmaple1() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		RequestContext rc = new RequestContext(baos, null, null, null);

		rc.setEncoding("ISO-8859-2");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");

		try {
			rc.write("Čevapčići").write(" i Šiščevapčići.");
		} catch (IOException e) {
			assertTrue(false);
		}

		byte[] expected = hexStringToByteArray("485454502f312e31"
				+ "2032303520496465" + "6d6f2064616c6a65" + "0d0a436f6e74656e"
				+ "742d547970653a20" + "746578742f706c61" + "696e3b2063686172"
				+ "7365743d49534f2d" + "383835392d320d0a" + "0d0ac865766170e8"
				+ "69e669206920a969" + "b9e865766170e869" + "e6692e");

		assertArrayEquals(expected, baos.toByteArray());
	}

	@Test
	public void testExample2() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RequestContext rc = new RequestContext(baos, null, null, null);

		rc.setEncoding("UTF-8");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");

		try {
			rc.write("Čevapčići").write(" i Šiščevapčići.");
		} catch (IOException e) {
			assertTrue(false);
		}

		byte[] expected = hexStringToByteArray("485454502f312e31"
				+ "2032303520496465" + "6d6f2064616c6a65" + "0d0a436f6e74656e"
				+ "742d547970653a20" + "746578742f706c61" + "696e3b2063686172"
				+ "7365743d5554462d" + "380d0a0d0ac48c65" + "766170c48d69c487"
				+ "69206920c5a069c5" + "a1c48d65766170c4" + "8d69c487692e");

		assertArrayEquals(expected, baos.toByteArray());
	}

	@Test
	public void testExample3() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RequestContext rc = new RequestContext(baos, null, null, null);

		rc.setEncoding("UTF-8");
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1",
				"/"));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, "/"));
		// Only at this point will header be created and written...

		try {
			rc.write("Čevapčići").write(" i ").write("Šiščevapčići.");
		} catch (IOException e) {
			assertTrue(false);
		}

		String expected = "HTTP/1.1 205 Idemo dalje\r\n"
				+ "Content-Type: text/plain; charset=UTF-8\r\n"
				+ "Set-Cookie: korisnik=\"perica\"; Domain=127.0.0.1; Path=/; Max-Age=3600; HttpOnly\r\n"
				+ "Set-Cookie: zgrada=\"B4\"; Path=/; HttpOnly\r\n" + "\r\n"
				+ "Čevapčići i Šiščevapčići.";
		String actual = new String(baos.toByteArray(), StandardCharsets.UTF_8);

		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullStream() {
		new RequestContext(null, new HashMap<String, String>(),
				new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorRCCookieNullName() {
		new RCCookie(null, "kjkj", 3000, "www.9gag.com", "/");
	}

	@Test
	public void testCookie() {
		final RCCookie cookie = new RCCookie("Kolačić sa borovnicama", "K+",
				222, null, null);

		assertEquals("Name is: Kolačić sa borovnicama",
				"Kolačić sa borovnicama", cookie.getName());
		assertEquals("Valid time is 222", Integer.valueOf(222),
				cookie.getMaxAge());
		assertEquals("Value is K+", "K+", cookie.getValue());
		assertNull("Path is not setted", cookie.getPath());
		assertNull("Domain is not setted", cookie.getDomain());
	}

	@Test(expected = RuntimeException.class)
	public void testExceptionIfMimeChangesAfterWriting() {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		try {
			rc.write(new byte[]{1, 2, 3});
		} catch (final IOException e) {
		}

		rc.setMimeType("image/jpg");
	}

	public void testTempParameters() {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final Map<String, String> params = new HashMap<>();
		params.put("a", "a");
		params.put("b", "b");

		final RequestContext context = new RequestContext(os, null, null, null);

		context.setTemporaryParameter("c", "c");

		context.setTemporaryParameter("e", "e");
		context.removeTemporaryParameter("b");

		final Set<String> names = context.getTemporaryParameterNames();

		assertTrue("Parameters are {b, e}",
				names.size() == 2 && names.contains("b") && names.contains("e"));
	}

	@Test
	public void testSetAndSetPersistentParameters() {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final Map<String, String> params = new HashMap<>();
		params.put("a", "a");
		params.put("b", "b");

		final RequestContext context = new RequestContext(os, null, params,
				null);

		params.put("c", "c");
		context.setPersistentParameter("d", "d");
		context.removePersistentParameter("b");

		final Set<String> names = context.getPersistentParameterNames();

		assertTrue("Paramteres are {a, d}",
				names.size() == 2 && names.contains("a") && names.contains("d"));
	}

	@Test(expected = RuntimeException.class)
	public void testExceptionIfStatusChangesAfterWriting() {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		try {
			rc.write(new byte[]{1, 2, 3});
		} catch (final IOException e) {
		}

		rc.setStatusCode(303);
	}

	@Test(expected = RuntimeException.class)
	public void testExceptionIfEncodingChangesAfterWriting() {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		try {
			rc.write(new byte[]{1, 2, 3});
		} catch (final IOException e) {
		}

		rc.setEncoding("UTF-16");
	}

	@Test(expected = RuntimeException.class)
	public void testExceptionIfStatusTextChangesAfterWriting() {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		try {
			rc.write(new byte[]{1, 2, 3});
		} catch (final IOException e) {
		}

		rc.setStatusText("File not found");
	}

	@Test(expected = RuntimeException.class)
	public void testExceptionIfContentLengthChangesAfterWriting() {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final RequestContext rc = new RequestContext(os,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());

		rc.setContentLength(123l);

		try {
			rc.write(new byte[]{1, 2, 3});
		} catch (final IOException e) {
		}

		rc.setContentLength(45l);
	}

	@Test
	public void testPersistentParameters() {
		RequestContext rc = new RequestContext(new ByteArrayOutputStream(),
				null, null, null);

		assertNull(rc.getParameter("test"));

		rc.setPersistentParameter("p1", "v1");
		rc.setPersistentParameter("p2", "v2");
		rc.removePersistentParameter("p1");

		assertNull(rc.getPersistentParameter("v1"));
		assertEquals("v2", rc.getPersistentParameter("p2"));

		rc.setPersistentParameter("p3", "v3");

		Set<String> pNames = rc.getPersistentParameterNames();

		assertEquals(pNames.size(), 2);
		assertTrue(pNames.contains("p2"));
		assertTrue(pNames.contains("p3"));

	}

	@Test
	public void testTemporaryParameters() {
		RequestContext rc = new RequestContext(new ByteArrayOutputStream(),
				null, null, null);

		rc.setMimeType("image");

		rc.setTemporaryParameter("p1", "v1");
		rc.setTemporaryParameter("p2", "v2");
		rc.removeTemporaryParameter("p1");

		assertNull(rc.getTemporaryParameter("v1"));
		assertEquals("v2", rc.getTemporaryParameter("p2"));

		rc.setTemporaryParameter("p3", "v3");

		Set<String> pNames = rc.getTemporaryParameterNames();

		assertEquals(pNames.size(), 2);
		assertTrue(pNames.contains("p2"));
		assertTrue(pNames.contains("p3"));

	}

	@Test
	public void testParameters() {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("name1", "value1");
		parameters.put("name2", "value2");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RequestContext rc = new RequestContext(baos, parameters, null, null);

		String p1 = rc.getParameter("name1");
		String p2 = rc.getParameter("name2");
		String p3 = rc.getParameter("name3");

		Set<String> names = rc.getParameterNames();

		assertEquals("value1", p1);
		assertEquals("value2", p2);
		assertNull(p3);

		assertEquals(names.size(), 2);
		assertTrue(names.contains("name1"));
		assertTrue(names.contains("name2"));
	}

	@Test
	public void testHeaderWithtContentLength() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RequestContext rc = new RequestContext(baos, null, null, null);
		rc.setMimeType("image/png");
		rc.setContentLength(10l);
		
		rc.addRCCookie(new RCCookie("name", "name1", null, null, null));

		String content = new String("0123456789");

		try {
			rc.write(content.getBytes(StandardCharsets.US_ASCII));
		} catch (IOException e) {
		}

		String expected = "HTTP/1.1 200 OK\r\n" + "Content-Type: image/png\r\n"
				+ "Content-Length: 10\r\n" + "Set-Cookie: name=\"name1\"; HttpOnly\r\n" + "\r\n" + "0123456789";

		String actual = new String(baos.toByteArray(),
				StandardCharsets.US_ASCII);
		assertEquals(expected, actual);
	}

	@Test(expected = RuntimeException.class)
	public void testHeaderWithContentLengthException() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RequestContext rc = new RequestContext(baos, null, null, null);
		rc.setMimeType("image/png");
		rc.setContentLength(10l);

		// 11 bytes
		String content = new String("01234567890");

		try {
			rc.write(content.getBytes(StandardCharsets.US_ASCII));
		} catch (IOException e) {
		}
	}

}
