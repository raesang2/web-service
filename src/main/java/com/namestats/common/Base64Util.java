package com.namestats.common;

import java.util.Base64;

public class Base64Util {


	// 문자열을 Base64로 인코딩
	public static String encodeBase64(String input) {
		return Base64.getEncoder().encodeToString(input.getBytes());
	}

	// Base64로 인코딩된 문자열을 디코딩
	public static String decodeBase64(String encodedString) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		return new String(decodedBytes);
	}
}