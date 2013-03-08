package com.hectorlopezfernandez.utils;

import java.net.URL;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;

public final class OWASPUtils {

	// ficheros de reglas y parseadores
	private static String POST_COMMENTS_POLICY_FILE = "/antisamy-comments-rules.xml";
	private static String NO_TAGS_POLICY_FILE = "/antisamy-no-tags-rules.xml";
	private static Policy postCommentsPolicy;
	private static Policy noTagsPolicy;
	private static AntiSamy parser;
	
	static {
		try {
			URL pcpu = (new OWASPUtils()).getClass().getResource(POST_COMMENTS_POLICY_FILE);
			postCommentsPolicy = Policy.getInstance(pcpu);
			URL ntpu = (new OWASPUtils()).getClass().getResource(NO_TAGS_POLICY_FILE);
			noTagsPolicy = Policy.getInstance(ntpu);
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

	/**
	 * Parsea un texto para eliminar todos los tags de html
	 */
	public static CleanResults parseTextNoTag(String text) {
		CleanResults cr = null;
		try {
			cr = parser.scan(text, noTagsPolicy);
		} catch(Exception e) {
			throw new RuntimeException("Error eliminando todo el html de un texto?? " + e.getClass().getName() + " - " + e.getMessage());
		}
		return cr;
	}

}