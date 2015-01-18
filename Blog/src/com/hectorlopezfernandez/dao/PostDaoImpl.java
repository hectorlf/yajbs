package com.hectorlopezfernandez.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.exception.DataIntegrityException;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;

public class PostDaoImpl extends BaseDaoImpl implements PostDao {

	private final static Logger logger = LoggerFactory.getLogger(PostDaoImpl.class);

	/* Constructores */
	
	@Inject
	public PostDaoImpl(EntityManager em) {
		super(em);
	}
	
	/* Metodos */

	/** ADMIN RELATED **/

	// cuenta el n�mero total de posts del sistema
	@Override
	public Long countAllPosts() {
		String q = "select count(p.id) from Post p";
		Long count = count(q, null);
		return count;
	}
	// recupera todos los posts del sistema con paginaci�n, ordenados por id descendentemente
	@Override
	public List<Post> getAllPosts(PaginationInfo pagination) {
		if (pagination == null) throw new IllegalArgumentException("El parametro pagination no puede ser nulo.");
		logger.debug("Recuperando {} elementos de todos los posts del sistema. Primer elemento: {}", pagination.getItemsPerPage(), pagination.getFirstItem());
		List<Post> posts = null;
		if (pagination.isEnabled()) posts = find("select p from Post p order by p.id desc", null, Post.class, pagination.getFirstItem(), pagination.getItemsPerPage());
		else posts = find("select p from Post p order by p.id desc", null, Post.class);
		if (posts.size() == 0) return Collections.emptyList();
		return posts;
	}
	// cuenta el n�mero total de posts asociados a una entrada de archivo
	@Override
	public int countAllPostsForArchiveEntry(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la entrada de archivo a contar no puede ser nulo.");
		String q = "select count(p.id) from Post p where p.archiveEntry.id = :archiveEntryId";
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("archiveEntryId", id);
		Long count = count(q, params);
		if (count == null) return 0;
		return count.intValue();
	}
	
	// inserta un post en la base de datos
	@Override
	public Long savePost(Post post) {
		if (post == null) throw new IllegalArgumentException("El objeto post a persistir no puede ser nulo.");
		logger.debug("Insertando post con titulo '{}' en base de datos", post.getTitle());
		Long id = save(post);
		return id;
	}
	// modifica un post en la base de datos
	@Override
	public void modifyPost(Post post) {
		if (post == null) throw new IllegalArgumentException("El objeto post a persistir no puede ser nulo.");
		logger.debug("Modificando post con titulo '{}' en base de datos", post.getTitle());
		Post dbp = getPost(post.getId());
		if (!dbp.getAuthor().getId().equals(post.getAuthor().getId())) dbp.setAuthor(post.getAuthor());
		dbp.setCommentsClosed(post.isCommentsClosed());
		dbp.setContent(post.getContent());
		dbp.setExcerpt(post.getExcerpt());
		dbp.setFeedContent(post.getFeedContent());
		if (!dbp.getHost().getId().equals(post.getHost().getId())) dbp.setHost(post.getHost());
		dbp.setLastModificationDate(post.getLastModificationDate());
		dbp.setMetaDescription(post.getMetaDescription());
		dbp.setTitle(post.getTitle());
		dbp.setTitleUrl(post.getTitleUrl());
		// los tags tienen un procesado especial, ya que hay que eliminar e insertar en la coleccion segun el caso
		List<Tag> emptyList = Collections.emptyList();
		if (post.getTags() == null) post.setTags(emptyList);
		if (dbp.getTags() == null) dbp.setTags(emptyList);
		Set<Long> newTagsIds = new HashSet<Long>(post.getTags().size());
		for (Tag tag : post.getTags()) {
			newTagsIds.add(tag.getId());
		}
		Set<Long> oldTagsIds = new HashSet<Long>(dbp.getTags().size());
		for (Tag tag : dbp.getTags()) {
			oldTagsIds.add(tag.getId());
		}
		// primero se eliminan los que ya no van a estar
		Iterator<Tag> oldTagsIterator = dbp.getTags().iterator();
		while (oldTagsIterator.hasNext()) {
			Tag t = oldTagsIterator.next();
			if (!newTagsIds.contains(t.getId())) oldTagsIterator.remove();
		}
		// por �ltimo se a�aden los nuevos tags
		Iterator<Tag> newTagsIterator = post.getTags().iterator();
		while (newTagsIterator.hasNext()) {
			Tag t = newTagsIterator.next();
			if (!oldTagsIds.contains(t.getId())) dbp.getTags().add(t);
		}
//		flush(); // este flush deber�a ir en un interceptor de AOP asociado a los servicios o a los actions
	}
	// borra un post de la base de datos
	@Override
	public void deletePost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a borrar no puede ser nulo.");
		logger.debug("Borrando post con id {} de la base de datos", id);
		Post p = getReference(id, Post.class);
		delete(p);
	}
	// cambia el estado de un post a publicado
	@Override
	public void publishPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a publicar no puede ser nulo.");
		logger.debug("Publicando post con id {}", id);
		Post p = getReference(id, Post.class);
		p.setPublished(true);
		save(p);
	}
	// cambia el estado de un post a no publicado
	@Override
	public void unpublishPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a despublicar no puede ser nulo.");
		logger.debug("Despublicando post con id {}", id);
		Post p = getReference(id, Post.class);
		p.setPublished(false);
		save(p);
	}
	// cambia la fecha de publicacion de un post
	@Override
	public void changePostPublicationDate(Long id, DateTime publicationDate) {
		if (id == null) throw new IllegalArgumentException("El id del post a modificar no puede ser nulo.");
		if (publicationDate == null) throw new IllegalArgumentException("El parametro publicationDate no puede ser nulo.");
		logger.debug("Cambiando fecha de publicacion del post con id {} a {}", id, publicationDate);
		Post p = getReference(id, Post.class);
		p.setPublicationDate(publicationDate);
		save(p);
	}


	/** POSTS **/

	// cuenta los posts publicados en la base de datos
	@Override
	public Long countPublishedPosts() {
		String q = "select count(p.id) from Post p where p.published = true";
		Long count = count(q, null);
		return count;
	}
	// recupera la lista de posts publicados de la base de datos
	@Override
	public List<Post> listPublishedPosts(PaginationInfo pagination) {
		if (pagination == null) throw new IllegalArgumentException("El parametro pagination no puede ser nulo.");
		String q = "select p from Post p left join fetch p.author where p.published = true order by p.publicationDateAsLong desc";
		List<Post> posts = null;
		if (pagination.isEnabled()) {
			// paginacion activada
			logger.debug("Recuperando lista de posts empezando en el elemento {}, limitando a {} resultados", pagination.getFirstItem(), pagination.getItemsPerPage());
			posts = find(q, null, Post.class, pagination.getFirstItem(), pagination.getItemsPerPage());
		} else {
			// paginacion desactivada
			logger.debug("Recuperando lista de todos los posts del sistema.");
			posts = find(q, null, Post.class);
		}
		return posts;
	}

	// cuenta el n�mero de posts, filtrados por fecha
	@Override
	public Long countPublishedPosts(Integer year, Integer month) {
		if (year == null) throw new IllegalArgumentException("El parametro year no puede ser nulo.");
		String q = "select count(p.id) from Post p inner join p.archiveEntry ae where p.published = true and ae.year = :year";
		String w1 = " and ae.month = :month";
		StringBuilder sb = new StringBuilder(q.length() + w1.length());
		Map<String,Object> params = new HashMap<String, Object>(2);
		sb.append(q);
		params.put("year", year);
		if (month != null) {
			sb.append(w1);
			params.put("month", month);
		}
		Long count = count(sb.toString(), params);
		return count;
	}
	// recupera un listado de posts publicados filtrados por fecha, ordenados por id de forma descendente
	@Override
	public List<Post> listPublishedPosts(Integer year, Integer month, PaginationInfo pagination) {
		if (year == null) throw new IllegalArgumentException("El parametro year no puede ser nulo.");
		if (pagination == null) throw new IllegalArgumentException("El parametro pagination no puede ser nulo.");
		String q = "select p from Post p inner join p.archiveEntry ae left join fetch p.author"
				+ " where p.published = true and ae.year = :year";
		String w1 = " and ae.month = :month";
		String o = " order by p.publicationDateAsLong desc";
		StringBuilder sb = new StringBuilder(q.length() + w1.length() + o.length());
		Map<String,Object> params = new HashMap<String,Object>(2);
		sb.append(q);
		params.put("year", year);
		if (month != null) {
			sb.append(w1);
			params.put("month", month);
		}
		sb.append(o);
		List<Post> posts = null;
		if (pagination.isEnabled()) {
			// paginacion activada
			logger.debug("Recuperando lista de posts empezando en el elemento {}, limitando a {} resultados", pagination.getFirstItem(), pagination.getItemsPerPage());
			posts = find(sb.toString(), params, Post.class, pagination.getFirstItem(), pagination.getItemsPerPage());
		} else {
			// paginacion desactivada
			logger.debug("Recuperando lista de todos los posts del sistema.");
			posts = find(sb.toString(), params, Post.class);
		}
		return posts;
	}

	
	
	// cuenta el n�mero de posts, filtrados por tag
	@Override
	public Long countPublishedPostsByTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		String q = "select count(p.id) from Post p inner join p.tags t where p.published = true and t.id = :id";
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("id", id);
		Long count = count(q, params);
		return count;
	}
	// recupera un listado de posts filtrados por tag, ordenados por id de forma descendente
	@Override
	public List<Post> listPublishedPostsByTag(Long id, PaginationInfo pagination) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		if (pagination == null) throw new IllegalArgumentException("El parametro pagination no puede ser nulo.");
		String q = "select p from Post p left join fetch p.author inner join p.tags t"
				+ " where p.published = true and t.id = :id order by p.publicationDateAsLong desc";
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("id", id);
		List<Post> posts = null;
		if (pagination.isEnabled()) {
			// paginacion activada
			logger.debug("Recuperando lista de posts empezando en el elemento {}, limitando a {} resultados", pagination.getFirstItem(), pagination.getItemsPerPage());
			posts = find(q, params, Post.class, pagination.getFirstItem(), pagination.getItemsPerPage());
		} else {
			// paginacion desactivada
			logger.debug("Recuperando lista de todos los posts del sistema por id de tag: {}", id);
			posts = find(q, params, Post.class);
		}
		return posts;
	}

	
	
	// cuenta el n�mero de posts, filtrados por autor
	@Override
	public Long countPublishedPostsByAuthor(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		String q = "select count(p.id) from Post p inner join p.author a where p.published = true and a.id = :id";
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("id", id);
		Long count = count(q, params);
		return count;
	}
	// recupera un listado de posts filtrados por autor, ordenados por id de forma descendente
	@Override
	public List<Post> listPublishedPostsByAuthor(Long id, PaginationInfo pagination) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		if (pagination == null) throw new IllegalArgumentException("El parametro pagination no puede ser nulo.");
		String q = "select p from Post p left join p.author a"
				+ " where p.published = true and a.id = :id order by p.publicationDateAsLong desc";
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("id", id);
		List<Post> posts = null;
		if (pagination.isEnabled()) {
			// paginacion activada
			logger.debug("Recuperando lista de posts empezando en el elemento {}, limitando a {} resultados", pagination.getFirstItem(), pagination.getItemsPerPage());
			posts = find(q, params, Post.class, pagination.getFirstItem(), pagination.getItemsPerPage());
		} else {
			// paginacion desactivada
			logger.debug("Recuperando lista de todos los posts del sistema, filtrando por id de autor: {}", id);
			posts = find(q, params, Post.class);
		}
		return posts;
	}


	// recupera el listado de posts cuya fecha de publicaci�n sea igual o mayor que el par�metro (es decir, que sean posts m�s nuevos)
	@Override
	public List<SimplifiedPost> listPostsForFeedPublishedAfter(long milliseconds) {
		logger.debug("Recuperando posts para el feed con fecha de publicaci�n en milisegundos mayor que {}", milliseconds);
		// nunca se va a encontrar nada si el n�mero es negativo, no merece la pena lanzar excepciones
		if (milliseconds < 0) return Collections.emptyList();
		// al guardar el contenido para los feeds en una propiedad lazy, nunca se guarda en cache, 
		// asi que hay que recuperarlo a proposito para no hacer n+1 consultas
		// NOTA: el orden de las propiedades importa!
		String q = "select p.id, p.title, p.titleUrl, p.feedContent, p.publicationDateAsLong, a.displayName from Post p left join p.author a where p.published = true and p.publicationDateAsLong >= :minPublicationDate";
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("minPublicationDate", milliseconds);
		List<Object[]> fields = list(q, params);
		if (fields.size() == 0) return Collections.emptyList();
		List<SimplifiedPost> posts = new ArrayList<SimplifiedPost>(fields.size());
		for (Object[] field : fields) {
			Long id = (Long)field[0];
			String title = (String)field[1];; // el t�tulo deber�a venir ya codificado en entidades html, no se puede arriesgar a recodificar
			String titleUrl = (String)field[2];
			String excerpt = (String)field[3]; // el contenido del feed se obtiene ya procesado
			DateTime publicationDate = new DateTime(((Long)field[4]).longValue());
			String authorName = (String)field[5]; // el autor deber�a venir ya codificado en entidades html, no se puede arriesgar a recodificar
			SimplifiedPost sp = new SimplifiedPost(id, title, titleUrl, excerpt, null, publicationDate, authorName);
			posts.add(sp);
		}
		return posts;
	}


	// recupera los datos necesarios para el sitemap
	@Override
	public List<SimplifiedPost> getPostsForSitemap() {
		String q = "select p.id, p.titleUrl, p.publicationDateAsLong, p.lastModificationDateAsLong from Post p where p.published = true";
		List<Object[]> fields = list(q, null);
		if (fields.size() == 0) return Collections.emptyList();
		List<SimplifiedPost> posts = new ArrayList<SimplifiedPost>(fields.size());
		for (Object[] field : fields) {
			Long id = (Long)field[0];
			String titleUrl = (String)field[1];
			DateTime publicationDate = new DateTime(((Long)field[2]).longValue());
			DateTime lastModificationDate = new DateTime(((Long)field[3]).longValue());
			SimplifiedPost sp = new SimplifiedPost(id, null, titleUrl, null, null, publicationDate, lastModificationDate, null);
			posts.add(sp);
		}
		return posts;
	}
	

	// recupera un post por id
	@Override
	public Post getPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a recuperar no puede ser nulo.");
		logger.debug("Recuperando post con id: {}", id);
		Post p = get(id, Post.class);
		return p;
	}
	
	// recupera un post por id, con su autor y sus comentarios cargados con eager fetch
	// NOTA: se podr�a traer tambi�n la lista de tags, pero no se hace por limitaciones de sql
	@Override
	public Post getDetailedPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a recuperar no puede ser nulo.");
		logger.debug("Recuperando post detallado con id: {}", id);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("id", id);
		Post p = findUnique("select p from Post p left join fetch p.author where p.id = :id", params, Post.class);
		return p;
	}
	
	// recupera el id de un post a partir del nombre
	@Override
	public Long findPublishedPostId(int year, int month, String titleUrl) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El parametro titleUrl no puede ser nulo ni vacio.");
		if (logger.isDebugEnabled()) { logger.debug("Recuperando post con nombre {}", titleUrl); logger.debug("year {}", year); logger.debug("month {}", month); }
		Map<String,Object> params = new HashMap<String, Object>(3);
		params.put("year", Integer.valueOf(year));
		params.put("month", Integer.valueOf(month));
		params.put("titleUrl", titleUrl);
		List<Long> ids = listIds("select p.id from Post p inner join p.archiveEntry ae where p.published = true and ae.year = :year and ae.month = :month and p.titleUrl = :titleUrl", params);
		if (ids.size() > 1) throw new DataIntegrityException("Se han encontrado varios post para el nombre especificado. La columna de base de datos deber�a tener una restricci�n de unicidad que no lo habr�a permitido.");
		if (ids.size() == 0) return null;
		Long id = ids.get(0);
		return id;
	}




	/** ARCHIVE ENTRIES **/

	// recupera la lista de todas las entradas de archivo de la base de datos
	@Override
	public List<ArchiveEntry> listArchiveEntries() {
		logger.debug("Recuperando todas las entradas de archivo.");
		List<ArchiveEntry> entries = find("select ae from ArchiveEntry ae order by ae.id desc", null, ArchiveEntry.class);
		return entries;
	}

	// recupera una lista de propiedades individuales de todas las entradas de archivo, incluyendo el n�mero de posts asociados a cada entrada
	@Override
	public List<Object[]> listAllArchiveEntriesIncludingPublishedPostCount() {
		logger.debug("Recuperando los campos necesarios de todas las entradas de archivo.");
		List<Object[]> fields = list("select ae.year, ae.month, count(p) from ArchiveEntry ae, Post p where p.published = true and ae.id = p.archiveEntry.id group by ae.year, ae.month order by ae.id desc", null);
		if (fields.size() == 0) return Collections.emptyList();
		return fields;
	}

	// recupera una lista de propiedades individuales de las �ltimas entradas de archivo, para presentar en el footer
	@Override
	public List<Object[]> findLastArchiveEntryFieldsForFooter(int numEntries) {
		if (numEntries < 1) throw new IllegalArgumentException("El par�metro numEntries debe ser mayor que 0.");
		logger.debug("Recuperando los campos necesarios de las {} entradas de archivo m�s recientes.", numEntries);
		List<Object[]> fields = list("select ae.year, ae.month from ArchiveEntry ae order by ae.id desc", null, 0, numEntries);
		if (fields.size() == 0) return Collections.emptyList();
		return fields;
	}
	
	// busca una entrada de archivo por su fecha y la devuelve. si no existe, la crea
	@Override
	public ArchiveEntry findArchiveEntryCreateIfNotExists(int year, int month) {
		if (year < 0) throw new IllegalArgumentException("El par�metro year debe ser mayor que 0.");
		if (month < 1 || month > 12) throw new IllegalArgumentException("El par�metro month debe ser mayor que 0 y menor que 13.");
		// se busca el ArchiveEntry solicitado, si existe se devuelve
		Map<String,Object> params = new HashMap<String,Object>(2);
		params.put("year", year);
		params.put("month", month);
		ArchiveEntry ae = null;
		try {
			ae = findUnique("select ae from ArchiveEntry ae where ae.year = :year and ae.month = :month", params, ArchiveEntry.class);
		} catch(NoResultException nre) {
			// no se hace nada, es un error posible y probable
			logger.debug("No se ha encontrado un ArchiveEntry para el a�o {} y el mes {}. Se insertar� un objeto nuevo.", year, month);
		}
		if (ae != null) return ae;
		// si no existe la entrada, se crea y se devuelve
		ae = new ArchiveEntry();
		ae.setCount(0);
		ae.setMonth(month);
		ae.setYear(year);
		save(ae);
		return ae;
	}

	// cuenta el n�mero de posts asociados a una entrada de archivo
	@Override
	public int countPublishedPostsForArchiveEntry(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la entrada de archivo a contar no puede ser nulo.");
		String q = "select count(p.id) from Post p where p.published = true and p.archiveEntry.id = :archiveEntryId";
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("archiveEntryId", id);
		Long count = count(q, params);
		if (count == null) return 0;
		return count.intValue();
	}

	// borra una entrada de archivo de la base de datos
	@Override
	public void deleteArchiveEntry(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la entrada de archivo a borrar no puede ser nulo.");
		logger.debug("Borrando archiveentry con id {} de la base de datos", id);
		ArchiveEntry ae = getReference(id, ArchiveEntry.class);
		delete(ae);
	}

}
