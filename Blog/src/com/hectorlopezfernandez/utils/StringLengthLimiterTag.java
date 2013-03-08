package com.hectorlopezfernandez.utils;

public class StringLengthLimiterTag {

	public static String limitLength(String input, int limit) {
		if (input == null || input.length() == 0) return input;
		if (limit < 4) return "...";
		if (input.length() <= limit) return input;
		StringBuilder sb = new StringBuilder(input);
		sb.setLength(limit - 3);
		sb.append("...");
		return sb.toString();
	}

}