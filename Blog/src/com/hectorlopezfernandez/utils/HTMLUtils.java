package com.hectorlopezfernandez.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.html.dom.HTMLAnchorElementImpl;
import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.html.dom.HTMLImageElementImpl;
import org.apache.html.dom.HTMLParagraphElementImpl;
import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.filters.ElementRemover;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.owasp.reform.Reform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

public final class HTMLUtils {

	private final static Logger logger = LoggerFactory.getLogger(HTMLUtils.class);

	// no instanciable
	private HTMLUtils() {};

	/**
	 * Elimina todos los tags de html excepto <p>, <a> con su href e <img> con su src.
	 * Específico para los feeds atom.
	 */
	public static String parseTextForFeeds(String text) {
		logger.debug("Procesando html para presentar en un feed: {}", text);
		if (text == null || text.length() == 0) return "";
		// se crean los elementos del parseador de nekohtml
		ElementRemover remover = new ElementRemover();
		remover.acceptElement("p", null);
		remover.acceptElement("a", new String[] { "href" });
		remover.acceptElement("img", new String[] { "src" });
		DOMFragmentParser parser = new DOMFragmentParser();
		HTMLDocument document = new HTMLDocumentImpl();
		DocumentFragment fragment = document.createDocumentFragment();
		try {
			InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
			InputSource inputSource = new InputSource(is);
			parser.setProperty("http://cyberneko.org/html/properties/filters", new XMLDocumentFilter[] { remover });
			parser.parse(inputSource, fragment);
		} catch(Exception e) {
			// en el remoto caso de que ocurra una excepción parseando el texto, se devuelve la cadena vacía y a correr
			// no tiene mucho sentido levantar excepción por esto
			logger.error("Ha ocurrido un error parseando un texto html. RARO, RARO... {} - {}", e.getClass().getName(), e.getMessage());
			return "";
		}
		// una vez parseado con éxito, se construye la salida
		StringBuilder sb = new StringBuilder(text.length());
		composeTextForFeeds(fragment, sb);
		logger.debug("Resultado del parseo de html: {}", sb);
		return sb.toString();
	}
	private static void composeTextForFeeds(Node node, StringBuilder output) {
		//TODO si un tag a no tiene texto, no se debe imprimir en la salida
		// se recorre el árbol DOM, hay que comprobar si se trata de algún tag de los aceptados o un texto y escribirlo
		// si un nodo es de texto, contiene todo el texto que hay entre dos elementos (bien sea el comienzo o fin del tag o un tag nuevo)
		if (node instanceof HTMLParagraphElementImpl) output.append("<p>");
		else if (node instanceof HTMLAnchorElementImpl) output.append("<a href=\"").append(((HTMLAnchorElementImpl)node).getHref()).append("\">");
		else if (node instanceof HTMLImageElementImpl) output.append("<img src=\"").append(((HTMLImageElementImpl) node).getSrc()).append("\"/>");
		//IMPORTANTE como esto va a un feed que muestra html y nekohtml deshace la codificación, se recodifican las entidades html del texto
		else if (node instanceof TextImpl) output.append(Reform.HtmlEncode(node.getNodeValue()));
		Node child = node.getFirstChild();
		while (child != null) {
			composeTextForFeeds(child, output);
			child = child.getNextSibling();
		}
		// una vez recorrido el árbol en profundidad, se escriben los elementos finales de los tags que lo llevan
		if (node instanceof HTMLParagraphElementImpl) output.append("</p>");
		else if (node instanceof HTMLAnchorElementImpl) output.append("</a>");
	}

	
	/**
	 * Elimina todos los tags de html. Específico para la indexación en lucene.
	 */
	public static String parseTextForLucene(String text) {
		logger.debug("Procesando html para indexar en lucene: {}", text);
		if (text == null || text.length() == 0) return "";
		//HACK! para evitar que se peguen los textos al coincidir algunos tags, se insertan espacios de forma paranoica
		StringBuilder sb = new StringBuilder(text);
		int i = 0;
		while (i > -1) {
			i = sb.indexOf("</", i);
			if (i > 0) {
				sb.insert(i, " ");
				i += 3;
			}
		}
		logger.debug("Resultado de la inserción de blancos: {}", sb);
		// se crean los elementos del parseador de nekohtml
		ElementRemover remover = new ElementRemover();
		DOMFragmentParser parser = new DOMFragmentParser();
		HTMLDocument document = new HTMLDocumentImpl();
		DocumentFragment fragment = document.createDocumentFragment();
		try {
			InputStream is = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
			InputSource inputSource = new InputSource(is);
			parser.setProperty("http://cyberneko.org/html/properties/filters", new XMLDocumentFilter[] { remover });
			parser.parse(inputSource, fragment);
		} catch(Exception e) {
			throw new RuntimeException("Ha ocurrido un error parseando un texto html. RARO, RARO... " + e.getClass().getName() + " - " + e.getMessage());
		}
		// una vez parseado con éxito, se construye la salida
		sb.setLength(0);
		composeTextForLucene(fragment, sb);
		logger.debug("Resultado del parseo de html: {}", sb);
		return sb.toString();
	}
	private static void composeTextForLucene(Node node, StringBuilder output) {
		// se recorre el árbol DOM y se recoge únicamente el texto sin ningún tag
		if (node instanceof TextImpl) output.append(node.getNodeValue());
		Node child = node.getFirstChild();
		while (child != null) {
			composeTextForFeeds(child, output);
			child = child.getNextSibling();
		}
	}

	/**
	 * Codifica las entidades html pero no las etiquetas. Específico para el retorno de pasajes resaltados por la busqueda de lucene.
	 */
	public static String parseTextFromHighlightedLucenePassage(String text) {
		return ESCAPE_HTML4_CHARACTERS.translate(text);
	}
		
	
	
	// Helpers
	
	/**
	 * Translator copiado de Commons Lang para codificar sólo los caractéres, no las etiquetas
	 */
	public static final CharSequenceTranslator ESCAPE_HTML4_CHARACTERS = 
			new AggregateTranslator(
					new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()),
					new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE())
			);

	/**
	 * Translator copiado de Commons Lang para decodificar sólo los caractéres, no las etiquetas
	 */
//	public static final CharSequenceTranslator UNESCAPE_HTML4_CHARACTERS = 
//			new AggregateTranslator(
//					new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()),
//					new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()),
//					new NumericEntityUnescaper()
//			);

}