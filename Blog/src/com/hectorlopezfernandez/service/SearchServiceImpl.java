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
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.PageDao;
import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.dto.SearchResult;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.utils.OWASPUtils;

public class SearchServiceImpl implements SearchService {

	private final static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

	private final static Analyzer analyzer = new SpanishAnalyzer(Version.LUCENE_42);
	private final static IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_42, analyzer);

	private final static String ID_FIELD_NAME = "id";
	private final static String TYPE_FIELD_NAME = "type";
	private final static String INDEXED_FIELD_NAME = "contents";

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
	public List<SearchResult> search(String queryString) {
		logger.debug("Buscando en el índice con el texto de consulta: {}", queryString);
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
	        QueryParser parser = new QueryParser(Version.LUCENE_42, INDEXED_FIELD_NAME, analyzer);
	        Query query = parser.parse(queryString);
	        // se buscan los mejores 50 resultados
	        TopDocs searchResults = searcher.search(query, 50);
	        logger.debug("La búsqueda de lucene ha encontrado {} documentos coincidentes.", searchResults.totalHits);
	        ScoreDoc[] hits = searchResults.scoreDocs;
	        if (hits == null || hits.length == 0) return Collections.emptyList();
	        // de los resultados, obtenemos los ids de los objetos y su tipo para cargar los datos
	        results = new ArrayList<SearchResult>(hits.length);
	        for (int i = 0; i < hits.length; i++) {
	        	Document doc = searcher.doc(hits[i].doc);
	        	logger.debug("Encontrado documento de tipo {} con id {}", doc.get(TYPE_FIELD_NAME), doc.get(ID_FIELD_NAME));
	        	SearchResult sr = createSearchResultFromDocument(doc);
	        	results.add(sr);
	        }
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException accediendo al RAMDirectory de Lucene. RARO, RARO. -> {}", ioe.getMessage());
		} catch(ParseException pe) {
			logger.warn("Ha ocurrido una ParseException al procesar el texto de búsqueda. Esto no debería pasar. -> {}", pe.getMessage());
		} finally {
			// se cierra el reader
			try { reader.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
		return results;
	}
	private SearchResult createSearchResultFromDocument(Document document) {
		assert(document != null);
		// se recupera el id del objeto encontrado en el índice
		String stringId = document.get(ID_FIELD_NAME);
		Long id = Long.valueOf(stringId);
		// se discrimina por tipo de objeto
		String type = document.get(TYPE_FIELD_NAME);
		if (SearchResult.PAGE_TYPE.equals(type)) {
			// el documento es de un objeto Page, se recupera y se construye el SearchResult
			Page p = pageDao.getPage(id);
			String content = OWASPUtils.parseTextNoTag(p.getContent()).getCleanHTML(); /* al contenido se le quita el html para presentarlo */
			SearchResult sr = new SearchResult(SearchResult.PAGE_TYPE, p.getTitle(), p.getTitleUrl(), content, p.getPublicationDate());
			return sr;
		} else if (SearchResult.POST_TYPE.equals(type)) {
			// el documento es de un objeto Post, se recupera y se construye el SearchResult
			Post p = postDao.getPost(id);
			String excerpt = OWASPUtils.parseTextNoTag(p.getExcerpt()).getCleanHTML(); /* al resumen se le quita el html para presentarlo */
			String content = OWASPUtils.parseTextNoTag(p.getContent()).getCleanHTML(); /* al contenido se le quita el html para presentarlo */
			StringBuilder sb = new StringBuilder(1 + excerpt.length() + content.length()); sb.append(excerpt).append(" ").append(content);
			SearchResult sr = new SearchResult(SearchResult.POST_TYPE, p.getTitle(), p.getTitleUrl(), sb.toString(), p.getPublicationDate());
			return sr;
		} else {
			throw new RuntimeException("Se ha recuperado un documento del índice de lucene con un tipo que no se sabe transformar. ¿Qué coño estás haciendo?");
		}
	}

	@Override
	public void addPostToIndex(Post post) {
		if (post == null) throw new IllegalArgumentException("El parametro post no puede ser nulo.");
		if (post.getId() == null) throw new IllegalArgumentException("El id del post a indexar no puede ser nulo.");
		logger.debug("Añadiendo post con id {} al índice de lucene.", post.getId());
		IndexWriter writer = null;
		try {
			// se crea el writer y el documento con la información del post
			writer = new IndexWriter(directory, iwc);
	    	Document doc = new Document();
	    	Field idField = new StringField(ID_FIELD_NAME, "", Field.Store.YES); doc.add(idField);
	    	Field typeField = new StringField(TYPE_FIELD_NAME, "", Field.Store.YES); doc.add(typeField);
	    	Field contentField = new TextField(INDEXED_FIELD_NAME, "", Field.Store.NO); doc.add(contentField);
	    	transferPostToDocument(post, doc);
        	// se indexa el documento
        	addDocumentToIndex(doc, writer);
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException insertando un documento en el índice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} catch(RuntimeException re) {
			logger.error("Ha ocurrido una RuntimeExcepcion inesperada insertando un documento en el índice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", re.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
			throw re;
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}

	@Override
	public void removePostFromIndex(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Borrando post con id {} del índice de lucene.", id);
		IndexWriter writer = null;
		try {
			// se crea el writer y se borra el documento
			writer = new IndexWriter(directory, iwc);
        	removeDocumentFromIndex(id, writer);
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException borrando un documento del índice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}

	@Override
	public void addPageToIndex(Page page) {
		if (page == null) throw new IllegalArgumentException("El parametro page no puede ser nulo.");
		if (page.getId() == null) throw new IllegalArgumentException("El id de la página a indexar no puede ser nulo.");
		logger.debug("Añadiendo page con id {} al índice de lucene.", page.getId());
		IndexWriter writer = null;
		try {
			// se crea el writer y el documento con la información de la página
			writer = new IndexWriter(directory, iwc);
	    	Document doc = new Document();
	    	Field idField = new StringField(ID_FIELD_NAME, "", Field.Store.YES); doc.add(idField);
	    	Field typeField = new StringField(TYPE_FIELD_NAME, "", Field.Store.YES); doc.add(typeField);
	    	Field contentField = new TextField(INDEXED_FIELD_NAME, "", Field.Store.NO); doc.add(contentField);
	    	transferPageToDocument(page, doc);
        	// se indexa el documento
        	addDocumentToIndex(doc, writer);
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException insertando un documento en el índice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} catch(RuntimeException re) {
			logger.error("Ha ocurrido una RuntimeExcepcion inesperada insertando un documento en el índice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", re.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
			throw re;
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}

	@Override
	public void removePageFromIndex(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Borrando page con id {} del índice de lucene.", id);
		IndexWriter writer = null;
		try {
			// se crea el writer y se borra el documento
			writer = new IndexWriter(directory, iwc);
        	removeDocumentFromIndex(id, writer);
        	writer.close();
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException borrando un documento del índice de lucene. Es recomendable reindexar todo. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}


	@Override
	public void reindex() {
		logger.debug("Recreando por completo el índice de lucene.");
		IndexWriter writer = null;
		try {
			// se crean los objetos de lucene
			writer = new IndexWriter(directory, iwc);
	    	Document doc = new Document();
	    	Field idField = new StringField(ID_FIELD_NAME, "", Field.Store.YES); doc.add(idField);
	    	Field typeField = new StringField(TYPE_FIELD_NAME, "", Field.Store.YES); doc.add(typeField);
	    	Field contentField = new TextField(INDEXED_FIELD_NAME, "", Field.Store.NO); doc.add(contentField);
			// se reindexan todos los post
			List<Post> allPosts = postDao.getAllPosts();
			for (Post p : allPosts) {
				transferPostToDocument(p, doc);
	        	addDocumentToIndex(doc, writer);
			}
			// se reindexan todas las páginas
			List<Page> allPages = pageDao.getAllPages();
			for (Page p : allPages) {
				transferPageToDocument(p, doc);
	        	addDocumentToIndex(doc, writer);
			}
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException reindexando todos los objetos. Hay que verificar la configuración. RARO RARO. -> {}", ioe.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		} catch(RuntimeException re) {
			logger.error("Ha ocurrido una RuntimeExcepcion inesperada reindexando todos los objetos. Hay que verificar la configuración. RARO RARO. -> {}", re.getMessage());
			try { writer.rollback(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
			throw re;
		} finally {
			try { writer.close(); } catch(Exception e) { /* NO MANEJADO, NO HAY NADA QUE HACER */ }
		}
	}


	/** Métodos privados generales */
	
	private void transferPostToDocument(Post post, Document document) {
		assert(post != null);
		assert(post.getId() != null);
		assert(document != null);
		((StringField)document.getField(ID_FIELD_NAME)).setStringValue(post.getId().toString());
		((StringField)document.getField(TYPE_FIELD_NAME)).setStringValue(SearchResult.POST_TYPE);
		String title = StringEscapeUtils.unescapeHtml4(post.getTitle());
		String excerpt = StringEscapeUtils.unescapeHtml4(OWASPUtils.parseTextNoTag(post.getExcerpt()).getCleanHTML());
		String content = StringEscapeUtils.unescapeHtml4(OWASPUtils.parseTextNoTag(post.getContent()).getCleanHTML());
		StringBuilder completeText = new StringBuilder(2 + title.length() + excerpt.length() + content.length());
    	completeText.append(title).append(" ").append(excerpt).append(" ").append(content);
    	((TextField)document.getField(INDEXED_FIELD_NAME)).setStringValue(completeText.toString());
	}

	private void transferPageToDocument(Page page, Document document) {
		assert(page != null);
		assert(page.getId() != null);
		assert(document != null);
		((StringField)document.getField(ID_FIELD_NAME)).setStringValue(page.getId().toString());
		((StringField)document.getField(TYPE_FIELD_NAME)).setStringValue(SearchResult.PAGE_TYPE);
		String title = StringEscapeUtils.unescapeHtml4(page.getTitle());
		String content = StringEscapeUtils.unescapeHtml4(OWASPUtils.parseTextNoTag(page.getContent()).getCleanHTML());
		StringBuilder completeText = new StringBuilder(1 + title.length() + content.length());
    	completeText.append(title).append(" ").append(content);
    	((TextField)document.getField(INDEXED_FIELD_NAME)).setStringValue(completeText.toString());
	}

	private void addDocumentToIndex(Document document, IndexWriter indexWriter) throws IOException {
		assert(document != null);
		assert(indexWriter != null);
		logger.debug("Escribiendo documento en el índice de lucene: {}", document);
		// se inserta o actualiza el documento en el índice
		Term id = new Term(ID_FIELD_NAME, document.get(ID_FIELD_NAME));
		indexWriter.updateDocument(id, document);
	}

	private void removeDocumentFromIndex(Long id, IndexWriter indexWriter) throws IOException {
		assert(id != null);
		assert(indexWriter != null);
		logger.debug("Borrando documento con id {} en el índice de lucene.", id);
		// se inserta o actualiza el documento en el índice
		Term docId = new Term(ID_FIELD_NAME, id.toString());
		indexWriter.deleteDocuments(docId);
	}

}