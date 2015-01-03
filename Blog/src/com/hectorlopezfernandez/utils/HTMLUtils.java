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

	private static final Logger logger = LoggerFactory.getLogger(HTMLUtils.class);

	private static final String[] HREF_ATTRIBUTE = new String[] { "href" };
	private static final String[] SRC_ATTRIBUTE = new String[] { "src" };

	// no instanciable
	private HTMLUtils() {};

	/**
	 * Elimina todos los tags de html excepto <p>, <a> con su href e <img> con su src.
	 * Espec�fico para los feeds atom.
	 */
	public static String parseTextForFeeds(String text) {
		logger.debug("Procesando html para presentar en un feed: {}", text);
		if (text == null || text.length() == 0) return "";
		// se crean los elementos del parseador de nekohtml
		ElementRemover remover = new ElementRemover();
		remover.acceptElement("p", null);
		remover.acceptElement("a", HREF_ATTRIBUTE);
		remover.acceptElement("img", SRC_ATTRIBUTE);
		DOMFragmentParser parser = new DOMFragmentParser();
		HTMLDocument document = new HTMLDocumentImpl();
		DocumentFragment fragment = document.createDocumentFragment();
		try {
			InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
			InputSource inputSource = new InputSource(is);
			parser.setProperty("http://cyberneko.org/html/properties/filters", new XMLDocumentFilter[] { remover });
			parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
			parser.parse(inputSource, fragment);
		} catch(Exception e) {
			// en el remoto caso de que ocurra una excepci�n parseando el texto, se devuelve la cadena vac�a y a correr
			// no tiene mucho sentido levantar excepci�n por esto
			logger.error("Ha ocurrido un error parseando un texto html. RARO, RARO... {} - {}", e.getClass().getName(), e.getMessage());
			return "";
		}
		// una vez parseado con �xito, se construye la salida
		StringBuilder sb = new StringBuilder(text.length());
		composeTextForFeeds(fragment, sb);
		logger.debug("Resultado del parseo de html: {}", sb);
		return sb.toString();
	}
	private static void composeTextForFeeds(Node node, StringBuilder output) {
		//TODO si un tag a no tiene texto, no se debe imprimir en la salida
		// se recorre el �rbol DOM, hay que comprobar si se trata de alg�n tag de los aceptados o un texto y escribirlo
		// si un nodo es de texto, contiene todo el texto que hay entre dos elementos (bien sea el comienzo o fin del tag o un tag nuevo)
		if (node instanceof HTMLParagraphElementImpl) output.append("<p>");
		else if (node instanceof HTMLAnchorElementImpl) {
			logger.debug("Nodo de enlace: {}", ((HTMLAnchorElementImpl)node).getHref());
			output.append("<a href=\"").append(((HTMLAnchorElementImpl)node).getHref()).append("\">");
		} else if (node instanceof HTMLImageElementImpl) {
			logger.debug("Nodo de imagen: {}", ((HTMLImageElementImpl) node).getSrc());
			output.append("<img src=\"").append(((HTMLImageElementImpl) node).getSrc()).append("\"/>");
		}
		//IMPORTANTE como esto va a un feed que muestra html y nekohtml deshace la codificaci�n, se recodifican las entidades html del texto
		else if (node instanceof TextImpl) {
			logger.debug("Nodo de texto: {}", node.getNodeValue());
			output.append(Reform.HtmlEncode(node.getNodeValue()));
		}
		Node child = node.getFirstChild();
		while (child != null) {
			composeTextForFeeds(child, output);
			child = child.getNextSibling();
		}
		// una vez recorrido el �rbol en profundidad, se escriben los elementos finales de los tags que lo llevan
		if (node instanceof HTMLParagraphElementImpl) output.append("</p>");
		else if (node instanceof HTMLAnchorElementImpl) output.append("</a>");
	}

	
	/**
	 * Elimina todos los tags de html. Espec�fico para la indexaci�n en lucene.
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
		logger.debug("Resultado de la inserci�n de blancos: {}", sb);
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
		// una vez parseado con �xito, se construye la salida
		sb.setLength(0);
		composeTextForLucene(fragment, sb);
		logger.debug("Resultado del parseo de html: {}", sb);
		return sb.toString();
	}
	private static void composeTextForLucene(Node node, StringBuilder output) {
		// se recorre el �rbol DOM y se recoge �nicamente el texto sin ning�n tag
		if (node instanceof TextImpl) output.append(node.getNodeValue());
		Node child = node.getFirstChild();
		while (child != null) {
			composeTextForFeeds(child, output);
			child = child.getNextSibling();
		}
	}

	/**
	 * Codifica las entidades html pero no las etiquetas. Espec�fico para el retorno de pasajes resaltados por la busqueda de lucene.
	 */
	public static String parseTextFromHighlightedLucenePassage(String text) {
		return ESCAPE_HTML4_CHARACTERS.translate(text);
	}
		
	
	
	// Helpers
	
	/**
	 * Translator copiado de Commons Lang para codificar s�lo los caract�res, no las etiquetas
	 */
	public static final CharSequenceTranslator ESCAPE_HTML4_CHARACTERS = 
			new AggregateTranslator(
					new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()),
					new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE())
			);

	/**
	 * Translator copiado de Commons Lang para decodificar s�lo los caract�res, no las etiquetas
	 */
//	public static final CharSequenceTranslator UNESCAPE_HTML4_CHARACTERS = 
//			new AggregateTranslator(
//					new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()),
//					new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()),
//					new NumericEntityUnescaper()
//			);

}