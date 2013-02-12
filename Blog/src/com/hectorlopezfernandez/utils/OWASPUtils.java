package com.hectorlopezfernandez.utils;

import java.net.URL;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;

public final class OWASPUtils {

	// ficheros de reglas y parseadores
	private static String POST_COMMENTS_POLICY_FILE = "/antisamy-comments-rules.xml";
	private static Policy postCommentsPolicy;
	private static AntiSamy parser;
	
	static {
		try {
			URL u = (new OWASPUtils()).getClass().getResource(POST_COMMENTS_POLICY_FILE);
			postCommentsPolicy = Policy.getInstance(u);
			parser = new AntiSamy();
		} catch(Exception e) {
			throw new RuntimeException("Error al crear el parseador de texto de comentarios: " + e.getClass().getName() + " - " + e.getMessage());
		}
	}
	
	// no instanciable
	private OWASPUtils() {};

	/**
	 * Parsea un texto para eliminar todo el html nocivo en un comentario
	 */
	public static CleanResults parsePostComment(String text) {
		CleanResults cr = null;
		try {
			cr = parser.scan(text, postCommentsPolicy);
		} catch(Exception e) {
			throw new RuntimeException("Error parseando texto de comentario?? " + e.getClass().getName() + " - " + e.getMessage());
		}
		return cr;
	}

}