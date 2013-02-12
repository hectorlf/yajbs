package com.hectorlopezfernandez.utils;

public class StringTitleCaserTag {

	public static String toTitleCase(String input) {
		if (input == null || input.length() == 0) return input;
		StringBuilder sb = new StringBuilder(input);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

}