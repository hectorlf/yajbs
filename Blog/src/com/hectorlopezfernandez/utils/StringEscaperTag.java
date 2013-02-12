package com.hectorlopezfernandez.utils;

import org.owasp.reform.Reform;

public class StringEscaperTag {

	public static String escape(String input) {
		return Reform.HtmlEncode(input);
	}

}