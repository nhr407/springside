package org.springside.modules.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springside.modules.utils.Encodes;

public class EncodesTest {

	@Test
	public void hexEncode() {
		String input = "haha,i am a very long message";
		String result = Encodes.encodeHex(input.getBytes());
		assertEquals(input, new String(Encodes.decodeHex(result)));
	}

	@Test
	public void base64Encode() {
		String input = "haha,i am a very long message";
		String result = Encodes.encodeBase64(input.getBytes());
		assertEquals(input, new String(Encodes.decodeBase64(result)));
	}

	@Test
	public void base64UrlSafeEncode() {
		String input = "haha,i am a very long message";
		String result = Encodes.encodeUrlSafeBase64(input.getBytes());
		assertEquals(input, new String(Encodes.decodeBase64(result)));
	}

	@Test
	public void base62Encode() {
		long num = 63;

		String result = Encodes.encodeBase62(num);
		assertEquals("11", result);
		assertEquals(num, Encodes.decodeBase62(result));
	}

	@Test
	public void urlEncode() {
		String input = "http://locahost/?q=中文&t=1";
		String result = Encodes.urlEncode(input);
		System.out.println(result);

		assertEquals(input, Encodes.urlDecode(result));
	}

	@Test
	public void xmlEncode() {
		String input = "1>2";
		String result = Encodes.escapeXml(input);
		assertEquals("1&gt;2", result);
		assertEquals(input, Encodes.unescapeXml(result));
	}

	@Test
	public void html() {
		String input = "1>2";
		String result = Encodes.escapeHtml(input);
		assertEquals("1&gt;2", result);
		assertEquals(input, Encodes.unescapeHtml(result));
	}

	@Test
	public void csv() {
		String input = "haha,kaka";
		String result = Encodes.escapeCsv(input);
		System.out.println(result);
		assertEquals(input, Encodes.unescapeCsv(result));
		//
		input = "\"ha\"ka";
		result = Encodes.escapeCsv(input);
		System.out.println(result);
		assertEquals(input, Encodes.unescapeCsv(result));
	}
}
