package com.hectorlopezfernandez.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.html.dom.HTMLAnchorElementImpl;
import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.html.dom.HTMLImageElementImpl;
import org.apache.html.dom.HTMLLIElementImpl;
import org.apache.html.dom.HTMLOListElementImpl;
import org.apache.html.dom.HTMLParagraphElementImpl;
import org.apache.html.dom.HTMLPreElementImpl;
import org.apache.html.dom.HTMLQuoteElementImpl;
import org.apache.html.dom.HTMLUListElementImpl;
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
		//TODO implementar un algoritmo correcto para eliminar retornos de carro manteniendo los <pre> ¿?
		// se parsea el html
		DocumentFragment fragment = parseHtml(text, getRemoverForFeeds());
		if (fragment == null) return "";
		// una vez parseado, se construye la salida
		StringBuilder sb = new StringBuilder(text.length());
		composeTextForFeeds(fragment, sb);
		logger.debug("Resultado del parseo de html: {}", sb);
		return sb.toString();
	}
	private static void composeTextForFeeds(Node node, StringBuilder output) {
		// se recorre el �rbol DOM, hay que comprobar si se trata de alg�n tag de los aceptados o un texto y escribirlo
		// si un nodo es de texto, contiene todo el texto que hay entre dos elementos (bien sea el comienzo o fin del tag o un tag nuevo)
		int currentSize = output.length();
		if (node instanceof HTMLParagraphElementImpl) {
			logger.debug("Nodo de parrafo");
			output.append("<p>");
		} else if (node instanceof HTMLAnchorElementImpl) {
			logger.debug("Nodo de enlace: {}", ((HTMLAnchorElementImpl)node).getHref());
			output.append("<a href=\"").append(((HTMLAnchorElementImpl)node).getHref()).append("\">");
			currentSize = output.length();
		} else if (node instanceof HTMLImageElementImpl) {
			logger.debug("Nodo de imagen: {}", ((HTMLImageElementImpl) node).getSrc());
			output.append("<img src=\"").append(((HTMLImageElementImpl) node).getSrc()).append("\"/>");
		} else if (node instanceof HTMLUListElementImpl) {
			logger.debug("Nodo de lista no numerada");
			output.append("<ul>");
		} else if (node instanceof HTMLOListElementImpl) {
			logger.debug("Nodo de lista numerada");
			output.append("<ol>");
		} else if (node instanceof HTMLLIElementImpl) {
			logger.debug("Nodo de elemento de lista");
			output.append("<li>");
		} else if (node instanceof TextImpl) {
			//IMPORTANTE como esto va a un feed que muestra html y nekohtml deshace la codificaci�n, se recodifican las entidades html del texto
			logger.debug("Nodo de texto: {}", node.getNodeValue());
			output.append(Reform.HtmlEncode(node.getNodeValue()));
		} else if (node instanceof HTMLQuoteElementImpl) {
			logger.debug("Nodo de bloque de cita");
			output.append("<blockquote>");
		} else if (node instanceof HTMLPreElementImpl) {
			logger.debug("Nodo de bloque pre");
			output.append("<pre>");
		} else {
			logger.debug("Encontrado tag no reconocido: {} - {} - {}", node.getClass(), node.getNodeName(), node.getNodeValue());
		}
		Node child = node.getFirstChild();
		while (child != null) {
			composeTextForFeeds(child, output);
			child = child.getNextSibling();
		}
		// una vez recorrido el �rbol en profundidad, se escriben los elementos finales de los tags que lo llevan
		if (node instanceof HTMLParagraphElementImpl) output.append("</p>");
		else if (node instanceof HTMLAnchorElementImpl) {
			// caso especial, si un tag a no tiene texto despues de parsear, no se imprime
			if (output.length() == currentSize) {
				output.setLength(output.lastIndexOf("<a href=\""));
			} else output.append("</a>");
		}
		else if (node instanceof HTMLUListElementImpl) output.append("</ul>");
		else if (node instanceof HTMLOListElementImpl) output.append("</ol>");
		else if (node instanceof HTMLLIElementImpl) output.append("</li>");
		else if (node instanceof HTMLQuoteElementImpl) output.append("</blockquote>");
		else if (node instanceof HTMLPreElementImpl) output.append("</pre>");
	}

	
	/**
	 * Elimina todos los tags de html. Espec�fico para la indexaci�n en lucene.
	 */
	public static String parseTextForLucene(String text) {
		logger.debug("Procesando html para indexar en lucene: {}", text);
		if (text == null || text.length() == 0) return "";
		//TODO eliminar los retornos de carro ¿?
		// se parsea el html
		DocumentFragment fragment = parseHtml(text, getRemoverForLucene());
		if (fragment == null) return "";
		// una vez parseado, se construye la salida
		StringBuilder sb = new StringBuilder(text.length());
		composeTextForLucene(fragment, sb);
		logger.debug("Resultado del parseo de html: {}", sb);
		return sb.toString();
	}
	private static void composeTextForLucene(Node node, StringBuilder output) {
		// se recorre el �rbol DOM y se recoge �nicamente el texto sin ning�n tag
		if (node instanceof TextImpl) output.append(node.getNodeValue());
		Node child = node.getFirstChild();
		while (child != null) {
			composeTextForLucene(child, output);
			child = child.getNextSibling();
		}
		if (node instanceof HTMLParagraphElementImpl || node instanceof HTMLLIElementImpl) {
			if (output.length() > 0 && output.charAt(output.length() - 1) != ' ') output.append(" ");
		}
	}




	// Helpers

	private static ElementRemover getRemoverForFeeds() {
		ElementRemover remover = new ElementRemover();
		remover.acceptElement("p", null);
//		remover.acceptElement("em", null);
//		remover.acceptElement("strong", null);
//		remover.acceptElement("del", null);
		remover.acceptElement("blockquote", null);
		remover.acceptElement("pre", null);
		remover.acceptElement("ul", null);
		remover.acceptElement("ol", null);
		remover.acceptElement("li", null);
		remover.acceptElement("a", HREF_ATTRIBUTE);
		remover.acceptElement("img", SRC_ATTRIBUTE);
		remover.removeElement("script");
		return remover;
	}
	private static ElementRemover getRemoverForLucene() {
		ElementRemover remover = new ElementRemover();
		remover.acceptElement("p", null);
		remover.acceptElement("li", null);
		remover.removeElement("img");
		remover.removeElement("script");
		return remover;
	}
	private static DocumentFragment parseHtml(String html, final ElementRemover remover) {
		assert html != null && html.length() > 0 && remover != null;
		DOMFragmentParser parser = new DOMFragmentParser();
		HTMLDocument document = new HTMLDocumentImpl();
		DocumentFragment fragment = document.createDocumentFragment();
		try {
			InputStream is = new ByteArrayInputStream(html.getBytes("UTF-8"));
			InputSource inputSource = new InputSource(is);
			parser.setProperty("http://cyberneko.org/html/properties/filters", new XMLDocumentFilter[] { remover });
			parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
			parser.parse(inputSource, fragment);
		} catch(Exception e) {
			// en el remoto caso de que ocurra una excepci�n parseando el texto, se devuelve la cadena vac�a y a correr
			// no tiene mucho sentido levantar excepci�n por esto
			logger.error("Ha ocurrido un error parseando un texto html. RARO, RARO... {} - {}", e.getClass().getName(), e.getMessage());
			return null;
		}
		return fragment;
	}

}