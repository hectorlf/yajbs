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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	/** POSTS **/

	// recupera la lista de posts de la base de datos
	@Override
	public List<Post> listPosts() {
		List<Post> posts = listPosts(0, 0);
		return posts;
	}

	// recupera la lista de posts de la base de datos, paginando con los parametros de entrada
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	@Override
	public List<Post> listPosts(int firstResult, int maxResults) {
		if (maxResults > 0 && firstResult < 0) throw new IllegalArgumentException("El parametro firstResult no puede ser menor que 0 si maxResults es mayor que 0.");
		String q = "select p from Post p left join fetch p.author left join fetch p.comments order by p.id desc";
		List<Post> posts = null;
		if (maxResults > 0) {
			// paginacion activada
			logger.debug("Recuperando lista de posts empezando en el elemento {}, limitando a {} resultados", firstResult, maxResults);
			posts = find(q, null, Post.class, firstResult, maxResults);
		} else {
			// paginacion desactivada
			logger.debug("Recuperando lista de todos los posts del sistema.");
			posts = find(q, null, Post.class);
		}
		return posts;
	}

	// cuenta el número de posts, filtrados por fecha
	@Override
	public Long countPosts(Integer year, Integer month) {
		if (year == null) throw new IllegalArgumentException("El parametro year no puede ser nulo.");
		String q = "select count(p.id) from Post p inner join p.archiveEntry ae where ae.year = :year";
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
	// recupera un listado de posts filtrados por fecha, ordenados por id de forma descendente
	@Override
	public List<Post> listPosts(Integer year, Integer month) {
		List<Post> posts = listPosts(year, month, 0, 0);
		return posts;
	}
	// recupera un listado de posts filtrados por fecha y usando paginación, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	@Override
	public List<Post> listPosts(Integer year, Integer month, int firstResult, int maxResults) {
		if (year == null) throw new IllegalArgumentException("El parametro year no puede ser nulo.");
		if (maxResults > 0 && firstResult < 0) throw new IllegalArgumentException("El parametro firstResult no puede ser menor que 0 si maxResults es mayor que 0.");
		String q = "select p from Post p inner join p.archiveEntry ae left join fetch p.author left join fetch p.comments where ae.year = :year";
		String w1 = " and ae.month = :month";
		String o = " order by p.id desc";
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
		if (maxResults > 0) {
			// paginacion activada
			logger.debug("Recuperando lista de posts empezando en el elemento {}, limitando a {} resultados", firstResult, maxResults);
			posts = find(sb.toString(), params, Post.class, firstResult, maxResults);
		} else {
			// paginacion desactivada
			logger.debug("Recuperando lista de todos los posts del sistema.");
			posts = find(sb.toString(), params, Post.class);
		}
		return posts;
	}

	
	
	// cuenta el número de posts, filtrados por tag
	@Override
	public Long countPostsByTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		String q = "select count(p.id) from Post p inner join p.tags t where t.id = :id";
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("id", id);
		Long count = count(q, params);
		return count;
	}
	// recupera un listado de posts filtrados por tag y usando paginación, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	@Override
	public List<Post> listPostsByTag(Long id, int firstResult, int maxResults) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		if (maxResults > 0 && firstResult < 0) throw new IllegalArgumentException("El parametro firstResult no puede ser menor que 0 si maxResults es mayor que 0.");
		String q = "select p from Post p left join fetch p.author left join fetch p.comments inner join p.tags t where t.id = :id order by p.id desc";
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("id", id);
		List<Post> posts = null;
		if (maxResults > 0) {
			// paginacion activada
			logger.debug("Recuperando lista de posts empezando en el elemento {}, limitando a {} resultados", firstResult, maxResults);
			posts = find(q, params, Post.class, firstResult, maxResults);
		} else {
			// paginacion desactivada
			logger.debug("Recuperando lista de todos los posts del sistema por id de tag: {}", id);
			posts = find(q, params, Post.class);
		}
		return posts;
	}

	
	
	// cuenta el número de posts, filtrados por autor
	@Override
	public Long countPostsByAuthor(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		String q = "select count(p.id) from Post p inner join p.author a where a.id = :id";
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("id", id);
		Long count = count(q, params);
		return count;
	}
	// recupera un listado de posts filtrados por autor y usando paginación, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	@Override
	public List<Post> listPostsByAuthor(Long id, int firstResult, int maxResults) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		if (maxResults > 0 && firstResult < 0) throw new IllegalArgumentException("El parametro firstResult no puede ser menor que 0 si maxResults es mayor que 0.");
		String q = "select p from Post p left join p.author a left join fetch p.comments where a.id = :id order by p.id desc";
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("id", id);
		List<Post> posts = null;
		if (maxResults > 0) {
			// paginacion activada
			logger.debug("Recuperando lista de posts empezando en el elemento {}, limitando a {} resultados", firstResult, maxResults);
			posts = find(q, params, Post.class, firstResult, maxResults);
		} else {
			// paginacion desactivada
			logger.debug("Recuperando lista de todos los posts del sistema, filtrando por id de autor: {}", id);
			posts = find(q, params, Post.class);
		}
		return posts;
	}


	// recupera el listado de posts cuya fecha de publicación sea igual o mayor que el parámetro (es decir, que sean posts más nuevos)
	@Override
	public List<Post> listPostsPublishedAfter(long milliseconds) {
		logger.debug("Recuperando lista de todos los posts con fecha de publicación en milisegundos mayor que {}", milliseconds);
		// nunca se va a encontrar nada si el número es negativo, no merece la pena lanzar excepciones
		if (milliseconds < 0) return Collections.emptyList();
		// se recuperan sólo los ids para intentar ganar velocidad con la caché (los post que aparezcan en el feed serán los más recientes)
		String q = "select p.id from Post p where p.publicationDateAsLong >= :minPublicationDate";
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("minPublicationDate", milliseconds);
		List<Long> ids = listIds(q, params);
		if (ids.size() == 0) return Collections.emptyList();
		List<Post> posts = new ArrayList<Post>(ids.size());
		for (Long id : ids) {
			Post p = get(id, Post.class);
			posts.add(p);
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
	// NOTA: se podría traer también la lista de tags, pero no se hace por limitaciones de sql
	@Override
	public Post getDetailedPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a recuperar no puede ser nulo.");
		logger.debug("Recuperando post detallado con id: {}", id);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("id", id);
		Post p = findUnique("select p from Post p left join fetch p.author left join fetch p.comments where p.id = :id", params, Post.class);
		return p;
	}
	
	// recupera el id de un post a partir del nombre
	@Override
	public Long findPostId(int year, int month, String titleUrl) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El parametro titleUrl no puede ser nulo ni vacio.");
		if (logger.isDebugEnabled()) { logger.debug("Recuperando post con nombre {}", titleUrl); logger.debug("year {}", year); logger.debug("month {}", month); }
		Map<String,Object> params = new HashMap<String, Object>(3);
		params.put("year", Integer.valueOf(year));
		params.put("month", Integer.valueOf(month));
		params.put("titleUrl", titleUrl);
		List<Long> ids = listIds("select p.id from Post p inner join p.archiveEntry ae where ae.year = :year and ae.month = :month and p.titleUrl = :titleUrl", params);
		if (ids.size() > 1) throw new DataIntegrityException("Se han encontrado varios post para el nombre especificado. La columna de base de datos debería tener una restricción de unicidad que no lo habría permitido.");
		if (ids.size() == 0) return null;
		Long id = ids.get(0);
		return id;
	}

	// cuenta el número total de posts del sistema
	@Override
	public Long countAllPosts() {
		String q = "select count(p.id) from Post p";
		Long count = count(q, null);
		return count;
	}
	// recupera todos los posts del sistema con paginación, ordenados por id descendentemente
	@Override
	public List<Post> getAllPosts(int firstResult, int maxResults) {
		logger.debug("Recuperando {} elementos de todos los posts del sistema. Primer elemento: {}", maxResults, firstResult);
		List<Post> posts = find("select p from Post p order by p.id desc", null, Post.class, firstResult, maxResults);
		if (posts.size() == 0) return Collections.emptyList();
		return posts;
	}
	// recupera todos los posts del sistema
	@Override
	public List<Post> getAllPosts() {
		logger.debug("Recuperando todos los posts del sistema");
		List<Post> posts = find("select p from Post p order by p.id desc", null, Post.class);
		if (posts.size() == 0) return Collections.emptyList();
		return posts;
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
		dbp.setHeaderImageUrl(post.getHeaderImageUrl());
		if (!dbp.getHost().getId().equals(post.getHost().getId())) dbp.setHost(post.getHost());
		dbp.setLastModificationDate(post.getLastModificationDate());
		dbp.setMetaDescription(post.getMetaDescription());
		dbp.setSideImageUrl(post.getSideImageUrl());
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
		// por último se añaden los nuevos tags
		Iterator<Tag> newTagsIterator = post.getTags().iterator();
		while (newTagsIterator.hasNext()) {
			Tag t = newTagsIterator.next();
			if (!oldTagsIds.contains(t.getId())) dbp.getTags().add(t);
		}
//		flush(); // este flush debería ir en un interceptor de AOP asociado a los servicios o a los actions
	}

	// borra un post de la base de datos
	@Override
	public void deletePost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a borrar no puede ser nulo.");
		logger.debug("Borrando post con id {} de la base de datos", id);
		Post p = getReference(id, Post.class);
		delete(p);
	}


	/** COMMENTS **/

	// recupera una lista de propiedades individuales de los últimos comentarios, para presentar en el footer
	@Override
	public List<Object[]> findLastCommentFieldsForFooter(int numComments) {
		if (numComments < 1) throw new IllegalArgumentException("El numero de comentarios recientes debe ser mayor que 0.");
		logger.debug("Recuperando los campos necesarios de los {} comentarios más recientes.", numComments);
		List<Object[]> fields = list("select c.id, c.author.displayName, c.post.title, c.post.titleUrl, c.post.publicationDateAsLong from Comment c order by c.id desc", null, 0, numComments);
		if (fields.size() == 0) return Collections.emptyList();
		return fields;
	}


	/** ARCHIVE ENTRIES **/

	// recupera la lista de todas las entradas de archivo de la base de datos
	@Override
	public List<ArchiveEntry> listArchiveEntries() {
		logger.debug("Recuperando todas las entradas de archivo.");
		List<ArchiveEntry> entries = find("select ae from ArchiveEntry ae order by ae.id desc", null, ArchiveEntry.class);
		return entries;
	}

	// recupera una lista de propiedades individuales de todas las entradas de archivo, incluyendo el número de posts asociados a cada entrada
	@Override
	public List<Object[]> listAllArchiveEntriesIncludingPostCount() {
		logger.debug("Recuperando los campos necesarios de todas las entradas de archivo.");
		List<Object[]> fields = list("select ae.year, ae.month, count(p) from ArchiveEntry ae, Post p where ae.id = p.archiveEntry.id group by ae.year, ae.month order by ae.id desc", null);
		if (fields.size() == 0) return Collections.emptyList();
		return fields;
	}

	// recupera una lista de propiedades individuales de las últimas entradas de archivo, para presentar en el footer
	@Override
	public List<Object[]> findLastArchiveEntryFieldsForFooter(int numEntries) {
		if (numEntries < 1) throw new IllegalArgumentException("El parámetro numEntries debe ser mayor que 0.");
		logger.debug("Recuperando los campos necesarios de las {} entradas de archivo más recientes.", numEntries);
		List<Object[]> fields = list("select ae.year, ae.month from ArchiveEntry ae order by ae.id desc", null, 0, numEntries);
		if (fields.size() == 0) return Collections.emptyList();
		return fields;
	}
	
	// busca una entrada de archivo por su fecha y la devuelve. si no existe, la crea
	@Override
	public ArchiveEntry findArchiveEntryCreateIfNotExists(int year, int month) {
		if (year < 0) throw new IllegalArgumentException("El parámetro year debe ser mayor que 0.");
		if (month < 1 || month > 12) throw new IllegalArgumentException("El parámetro month debe ser mayor que 0 y menor que 13.");
		// se busca el ArchiveEntry solicitado, si existe se devuelve
		Map<String,Object> params = new HashMap<String,Object>(2);
		params.put("year", year);
		params.put("month", month);
		ArchiveEntry ae = null;
		try {
			ae = findUnique("select ae from ArchiveEntry ae where ae.year = :year and ae.month = :month", params, ArchiveEntry.class);
		} catch(NoResultException nre) {
			// no se hace nada, es un error posible y probable
			logger.debug("No se ha encontrado un ArchiveEntry para el año {} y el mes {}. Se insertará un objeto nuevo.", year, month);
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

	// cuenta el número de posts asociados a una entrada de archivo
	@Override
	public int countPostsForArchiveEntry(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la entrada de archivo a contar no puede ser nulo.");
		String q = "select count(p.id) from Post p where p.archiveEntry.id = :archiveEntryId";
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


	/** TAGS **/

	// recupera una lista de tags populares, para presentar en el footer
	@Override
	public List<Tag> findMostPopularTagsForFooter(int numTags) {
		if (numTags < 1) throw new IllegalArgumentException("El parámetro numTags debe ser mayor que 0.");
		logger.debug("Recuperando las {} etiquetas más populares.", numTags);
		List<Tag> tags = find("select t from Tag t order by t.count desc", null, Tag.class, 0, numTags);
		if (tags.size() == 0) return Collections.emptyList();
		return tags;
	}

	// recupera un tag por id
	public Tag getTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Recuperando tag con id: {}", id);
		Tag t = get(id, Tag.class);
		return t;
	}

	// recupera el id de un tag por el nombre adaptado a url
	public Long findTagId(String nameUrl) {
		if (nameUrl == null || nameUrl.length() == 0) throw new IllegalArgumentException("El parametro nameUrl no puede ser nulo ni vacio.");
		logger.debug("Recuperando tag con nombre {}", nameUrl);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("nameUrl", nameUrl);
		List<Long> ids = listIds("select t.id from Tag t where t.nameUrl = :nameUrl", params);
		if (ids.size() > 1) throw new DataIntegrityException("Se han encontrado varios tags para el nombre especificado. La columna de base de datos debería tener una restricción de unicidad que no lo habría permitido.");
		if (ids.size() == 0) return null;
		Long id = ids.get(0);
		return id;
	}

	// cuenta el número total de tags del sistema
	@Override
	public Long countAllTags() {
		String q = "select count(t.id) from Tag t";
		Long count = count(q, null);
		return count;
	}
	// recupera todos los tags del sistema con paginación, ordenados por id descendentemente
	@Override
	public List<Tag> getAllTags(int firstResult, int maxResults) {
		logger.debug("Recuperando {} elementos de todos los tags del sistema. Primer elemento: {}", maxResults, firstResult);
		List<Tag> tags = find("select t from Tag t order by t.id desc", null, Tag.class, firstResult, maxResults);
		if (tags.size() == 0) return Collections.emptyList();
		return tags;
	}
	// recupera todos los tags del sistema
	@Override
	public List<Tag> getAllTags() {
		logger.debug("Recuperando todos los tags del sistema");
		List<Tag> tags = find("select t from Tag t order by t.id desc", null, Tag.class);
		if (tags.size() == 0) return Collections.emptyList();
		return tags;
	}


	// inserta un tag en la base de datos
	@Override
	public void saveTag(Tag tag) {
		if (tag == null) throw new IllegalArgumentException("El objeto tag a persistir no puede ser nulo.");
		logger.debug("Insertando tag con nombre '{}' en base de datos", tag.getName());
		save(tag);
	}

	// modifica un tag en la base de datos
	@Override
	public void modifyTag(Tag tag) {
		if (tag == null) throw new IllegalArgumentException("El objeto tag a persistir no puede ser nulo.");
		logger.debug("Modificando tag con nombre '{}' en base de datos", tag.getName());
		Tag dbt = getTag(tag.getId());
		dbt.setName(tag.getName());
		dbt.setNameUrl(tag.getNameUrl());
//		flush(); // este flush debería ir en un interceptor de AOP asociado a los servicios o a los actions
	}

	// borra un tag de la base de datos
	@Override
	public void deleteTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del tag a borrar no puede ser nulo.");
		logger.debug("Borrando tag con id {} de la base de datos", id);
		Tag t = getReference(id, Tag.class);
		delete(t);
	}



	// actualiza el contador de referencias de cada Tag
	public void updateTagRefCounts() {
		logger.debug("Actualizando los campos count de cada objeto Tag.");
		List<Tag> tags = find("select t from Tag t", null, Tag.class);
		for (Tag tag : tags) {
//			String q = "select count(p.id) from Post p join p.tags t where t.id = :id";
			String q = "select count(p.id) from Post p where :tag member of p.tags";
			Map<String,Object> params = new HashMap<String,Object>(1);
//			params.put("id", tag.getId());
			params.put("tag", tag);
			Long count = count(q, params);
			tag.setCount(count.intValue());
		}
	}

}