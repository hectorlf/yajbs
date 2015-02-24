package com.hectorlopezfernandez.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.postingshighlight.PostingsHighlighter;
import org.apache.lucene.store.Directory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.PageDao;
import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SearchResult;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.utils.HTMLUtils;

public class SearchServiceImpl implements SearchService {

	private final static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
	
	private final static int MAX_RESULTADOS_BUSQUEDA = 20;
	private final static int MAX_PARRAFOS_RESALTADOS_POR_DOCUMENTO = 4;

	private final static Analyzer analyzer = new SpanishAnalyzer();
	private final static PostingsHighlighter highlighter = new PostingsHighlighter();

	private final static String ID_FIELD_NAME = "id";
	private final static String TYPE_FIELD_NAME = "type";
	private final static String TITLE_FIELD_NAME = "title";
	private final static String TITLE_URL_FIELD_NAME = "title_url";
	private final static String INDEXED_FIELD_NAME = "contents";
	private final static String PUBLICATION_DATE_AS_LONG_FIELD_NAME = "publication_date";

	private final Directory directory;
	private final PostDao postDao;
	private final PageDao pageDao;
	
	/* Constructores */
	
	@Inject
	public SearchServiceImpl(Directory directory, PostDao postDao, PageDao pageDao) {
		if (directory == null) throw new IllegalArgumentException("El parametro directory no puede ser nulo.");
		if (postDao == null) throw new IllegalArgumentException("El parametro postDao no puede ser nulo.");
		if (pageDao == null) throw new IllegalArgumentException("El parametro pageDao no puede ser nulo.");
		this.directory = directory;
		this.postDao = postDao;
		this.pageDao = pageDao;
	}
	
	/* Metodos */

	@Override
	public PaginationInfo computePagination(Integer page) {
		return PaginationInfo.DISABLED;
	}

	@Override
	public List<SearchResult> search(String queryString) {
		logger.debug("Buscando en el �ndice con el texto de consulta: {}", queryString);
		// no buscamos con una cadena en blanco
		if (queryString == null || queryString.trim().length() == 0) return Collections.emptyList();
		// se abre el ramdirectory
		IndexReader reader = null;
		List<SearchResult> results = Collections.emptyList();
		try {
			// se construye el reader
			reader = DirectoryReader.open(directory);
			// se construyen los objetos de lucene
	        IndexSearcher searcher = new IndexSearcher(reader);
	        QueryParser parser = new QueryParser(INDEXED_FIELD_NAME, analyzer);
	        Query query = parser.parse(queryString);
	        // se buscan los mejores MAX_RESULTADOS_BUSQUEDA resultados
	        TopDocs searchResults = searcher.search(query, MAX_RESULTADOS_BUSQUEDA);
	        logger.debug("La b�squeda de lucene ha encontrado {} documentos coincidentes.", searchResults.totalHits);
	        ScoreDoc[] hits = searchResults.scoreDocs;
	        if (hits == null || hits.length == 0) return Collections.emptyList();
	        // se generan los pasajes de texto formateados
	        String[] passages = highlighter.highlight(INDEXED_FIELD_NAME, query, searcher, searchResults, MAX_PARRAFOS_RESALTADOS_POR_DOCUMENTO);
	        // se construyen los resultados de busqueda
	        results = new ArrayList<SearchResult>(hits.length);
	        for (int i = 0; i < hits.length; i++) {
	        	Document doc = searcher.doc(hits[i].doc);
	        	logger.debug("Encontrado documento de tipo {} con titulo '{}'", doc.get(TYPE_FIELD_NAME), doc.get(TITLE_FIELD_NAME));
	        	// se usa como contenido el resultado del resaltador de lucene
	        	SearchResult sr = createSearchResultFromDocumentAndFormattedPassage(doc, passages[i]);
	        	results.add(sr);
	        }
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException accediendo al Directory de Lucene. RARO, RARO. -> {}", ioe.getMessage());
		} catch(ParseException pe) {
			logger.warn("Ha ocurrido una ParseException al procesar el texto de b�squeda. Esto no deber�a pasar. -> {}", pe.getMessage());
		} finally {
			// se cierra el reader
			try { reader.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
		return results;
	}
	private SearchResult createSearchResultFromDocumentAndFormattedPassage(Document document, String passage) {
		assert(document != null);
		DateTime pubDate = new DateTime(Long.valueOf(document.get(PUBLICATION_DATE_AS_LONG_FIELD_NAME)));
		SearchResult sr = new SearchResult(document.get(TYPE_FIELD_NAME), document.get(TITLE_FIELD_NAME), document.get(TITLE_URL_FIELD_NAME), passage, pubDate);
		return sr;
	}

	@Override
	public void addPostToIndex(Post post) {
		if (post == null) throw new IllegalArgumentException("El parametro post no puede ser nulo.");
		if (post.getId() == null) throw new IllegalArgumentException("El id del post a indexar no puede ser nulo.");
		logger.debug("A�adiendo post con id {} y titulo '{}' al �ndice de lucene.", post.getId(), post.getTitle());
		IndexWriter writer = null;
		try {
			// se crea el writer y el documento con la informaci�n del post
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			writer = new IndexWriter(directory, iwc);
			Document doc = createDocument();
	    	transferPostToDocument(post, doc);
        	// se indexa el documento
        	addDocumentToIndex(doc, writer);
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException insertando un documento en el �ndice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} catch(RuntimeException re) {
			logger.error("Ha ocurrido una RuntimeExcepcion inesperada insertando un documento en el �ndice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", re.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
			throw re;
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}

	@Override
	public void removePostFromIndex(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Borrando post con id {} del �ndice de lucene.", id);
		IndexWriter writer = null;
		try {
			// se crea el writer y se borra el documento
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			writer = new IndexWriter(directory, iwc);
        	removeDocumentFromIndex(id, writer);
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException borrando un documento del �ndice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}

	@Override
	public void addPageToIndex(Page page) {
		if (page == null) throw new IllegalArgumentException("El parametro page no puede ser nulo.");
		if (page.getId() == null) throw new IllegalArgumentException("El id de la p�gina a indexar no puede ser nulo.");
		logger.debug("A�adiendo page con id {} y titulo '{}' al �ndice de lucene.", page.getId(), page.getTitle());
		IndexWriter writer = null;
		try {
			// se crea el writer y el documento con la informaci�n de la p�gina
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			writer = new IndexWriter(directory, iwc);
	    	Document doc = createDocument();
	    	transferPageToDocument(page, doc);
        	// se indexa el documento
        	addDocumentToIndex(doc, writer);
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException insertando un documento en el �ndice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} catch(RuntimeException re) {
			logger.error("Ha ocurrido una RuntimeExcepcion inesperada insertando un documento en el �ndice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", re.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
			throw re;
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}

	@Override
	public void removePageFromIndex(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Borrando page con id {} del �ndice de lucene.", id);
		IndexWriter writer = null;
		try {
			// se crea el writer y se borra el documento
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			writer = new IndexWriter(directory, iwc);
        	removeDocumentFromIndex(id, writer);
        	writer.close();
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException borrando un documento del �ndice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}


	@Override
	public void reindex() {
		logger.debug("Recreando por completo el �ndice de lucene.");
		IndexWriter writer = null;
		try {
			// se crean los objetos de lucene
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			writer = new IndexWriter(directory, iwc);
			writer.deleteAll();
	    	Document doc = createDocument();
	    	// se reindexan todos los post
			List<Post> allPosts = postDao.listPublishedPosts(PaginationInfo.DISABLED);
			for (Post p : allPosts) {
				transferPostToDocument(p, doc);
	        	addDocumentToIndex(doc, writer);
			}
			// se reindexan todas las p�ginas
			List<Page> allPages = pageDao.getAllPages();
			for (Page p : allPages) {
				transferPageToDocument(p, doc);
	        	addDocumentToIndex(doc, writer);
			}
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException reindexando todos los objetos. Hay que verificar la configuraci�n. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} catch(RuntimeException re) {
			logger.error("Ha ocurrido una RuntimeExcepcion inesperada reindexando todos los objetos. Hay que verificar la configuraci�n. RARO RARO. -> {}", re.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
			throw re;
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}


	/** M�todos privados generales */
	
	private void transferPostToDocument(Post post, Document document) {
		assert(post != null);
		assert(post.getId() != null);
		assert(document != null);
		((StringField)document.getField(ID_FIELD_NAME)).setStringValue(post.getId().toString());
		((StringField)document.getField(TYPE_FIELD_NAME)).setStringValue(SearchResult.POST_TYPE);
		((StringField)document.getField(TITLE_FIELD_NAME)).setStringValue(post.getTitle());
		((StringField)document.getField(TITLE_URL_FIELD_NAME)).setStringValue(post.getTitleUrl());
		String title = StringEscapeUtils.unescapeHtml4(post.getTitle());
		String excerpt = StringEscapeUtils.unescapeHtml4(HTMLUtils.parseTextForLucene(post.getExcerpt()));
		String content = StringEscapeUtils.unescapeHtml4(HTMLUtils.parseTextForLucene(post.getContent()));
		StringBuilder completeText = new StringBuilder(2 + title.length() + excerpt.length() + content.length());
    	completeText.append(title).append(" ").append(excerpt).append(" ").append(content);
    	((Field)document.getField(INDEXED_FIELD_NAME)).setStringValue(completeText.toString());
    	((StringField)document.getField(PUBLICATION_DATE_AS_LONG_FIELD_NAME)).setStringValue(String.valueOf(post.getPublicationDateAsLong()));
	}

	private void transferPageToDocument(Page page, Document document) {
		assert(page != null);
		assert(page.getId() != null);
		assert(document != null);
		((StringField)document.getField(ID_FIELD_NAME)).setStringValue(page.getId().toString());
		((StringField)document.getField(TYPE_FIELD_NAME)).setStringValue(SearchResult.PAGE_TYPE);
		((StringField)document.getField(TITLE_FIELD_NAME)).setStringValue(page.getTitle());
		((StringField)document.getField(TITLE_URL_FIELD_NAME)).setStringValue(page.getTitleUrl());
		String title = StringEscapeUtils.unescapeHtml4(page.getTitle());
		String content = StringEscapeUtils.unescapeHtml4(HTMLUtils.parseTextForLucene(page.getContent()));
		StringBuilder completeText = new StringBuilder(1 + title.length() + content.length());
    	completeText.append(title).append(" ").append(content);
    	((Field)document.getField(INDEXED_FIELD_NAME)).setStringValue(completeText.toString());
    	((StringField)document.getField(PUBLICATION_DATE_AS_LONG_FIELD_NAME)).setStringValue(String.valueOf(page.getPublicationDateAsLong()));
	}

	private void addDocumentToIndex(Document document, IndexWriter indexWriter) throws IOException {
		assert(document != null);
		assert(indexWriter != null);
		logger.debug("Escribiendo documento en el �ndice de lucene: {}", document);
		// se inserta o actualiza el documento en el �ndice
		Term id = new Term(ID_FIELD_NAME, document.get(ID_FIELD_NAME));
		indexWriter.updateDocument(id, document);
	}

	private void removeDocumentFromIndex(Long id, IndexWriter indexWriter) throws IOException {
		assert(id != null);
		assert(indexWriter != null);
		logger.debug("Borrando documento con id {} en el �ndice de lucene.", id);
		// se inserta o actualiza el documento en el �ndice
		Term docId = new Term(ID_FIELD_NAME, id.toString());
		indexWriter.deleteDocuments(docId);
	}

	
	// metodo comun para generar un documento, ya que todos son iguales
	private Document createDocument() {
    	Document doc = new Document();
    	Field idField = new StringField(ID_FIELD_NAME, "", Field.Store.NO);
    	doc.add(idField);
    	Field typeField = new StringField(TYPE_FIELD_NAME, "", Field.Store.YES);
    	doc.add(typeField);
    	Field titleField = new StringField(TITLE_FIELD_NAME, "", Field.Store.YES);
    	doc.add(titleField);
    	Field titleUrlField = new StringField(TITLE_URL_FIELD_NAME, "", Field.Store.YES);
    	doc.add(titleUrlField);
    	FieldType offsetsType = new FieldType(TextField.TYPE_STORED);
    	offsetsType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
    	Field contentField = new Field(INDEXED_FIELD_NAME, "", offsetsType);
    	doc.add(contentField);
    	Field publicationDateField = new StringField(PUBLICATION_DATE_AS_LONG_FIELD_NAME, "0", Field.Store.YES);
    	doc.add(publicationDateField);
    	return doc;
	}
}