package com.hectorlopezfernandez.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Comment;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.utils.HTMLUtils;

public class PostServiceImpl implements PostService {

	private final static Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	private final PostDao postDao;
	
	/* Constructores */
	
	@Inject
	public PostServiceImpl(PostDao postDao) {
		if (postDao == null) throw new IllegalArgumentException("El parametro postDao no puede ser nulo.");
		this.postDao = postDao;
	}
	
	/* Metodos */

	/** POSTS **/

	@Override
	public List<Post> getPublishedPosts() {
		logger.debug("Recuperando todos los posts publicados");
		List<Post> posts = postDao.listPublishedPosts(PaginationInfo.DISABLED);
		return posts;
	}

	@Override
	public List<Post> getNewestPosts(int count) {
		if (count < 1) throw new IllegalArgumentException("El número de posts a recuperar no puede ser menor que 1.");
		logger.debug("Recuperando los últimos {} posts", count);
		PaginationInfo pi = new PaginationInfo(1, count, count);
		List<Post> posts = postDao.listPublishedPosts(pi);
		return posts;
	}

	@Override
	public List<SimplifiedPost> getNewestPostsForFeed(int maxPostAgeInDays) {
		if (maxPostAgeInDays < 0) throw new IllegalArgumentException("La antigüedad en días de los posts a recuperar no puede ser menor que 0.");
		logger.debug("Recuperando los posts publicados hace menos de {} días", maxPostAgeInDays);
		long maxAge = System.currentTimeMillis() - ((long)maxPostAgeInDays * 24 * 60 * 60 * 1000);
		List<Post> posts = postDao.listPostsPublishedAfter(maxAge);
		if (posts.size() == 0) return Collections.emptyList();
		List<SimplifiedPost> results = new ArrayList<SimplifiedPost>(posts.size());
		for (Post p : posts) {
			String title = p.getTitle(); // el título debería venir ya codificado en entidades html, no se puede arriesgar a recodificar
			String excerpt = HTMLUtils.parseTextForFeeds(p.getExcerpt());
			String content = ""; // no se usa el contenido en la construcción del feed
			String authorName = p.getAuthor().getDisplayName(); // el autor debería venir ya codificado en entidades html, no se puede arriesgar a recodificar
			SimplifiedPost sp = new SimplifiedPost(p.getId(), title, p.getTitleUrl(), excerpt, content, p.getPublicationDate(), authorName);
			results.add(sp);
		}
		return results;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForDate(Integer year, Integer month, Integer page, Host preferences) {
		if (preferences == null) throw new IllegalArgumentException("El parametro preferences no puede ser nulo.");
		Long postCount = postDao.countPublishedPosts(year, month);
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = preferences.getPostsPerIndexPage().intValue();
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> listPostsForDate(Integer year, Integer month, PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El parametro pi no puede ser nulo.");
		logger.debug("Recuperando posts por fecha, página {}, y limitando a {} posts por página", pi.getCurrentPage(), pi.getItemsPerPage());
		List<Post> posts = postDao.listPublishedPosts(year, month, pi);
		return posts;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForTag(Long id, Integer page, Host preferences) {
		if (preferences == null) throw new IllegalArgumentException("El parametro preferences no puede ser nulo.");
		Long postCount = postDao.countPublishedPostsByTag(id);
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = preferences.getPostsPerIndexPage().intValue();
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> listPostsForTag(Long id, PaginationInfo pi) {
		//TODO o activar la paginacion o quitar el limite de elementos
		if (pi == null) throw new IllegalArgumentException("El parametro pi no puede ser nulo.");
		logger.debug("Recuperando posts por tag, página {}, y limitando a {} posts por página", pi.getCurrentPage(), pi.getItemsPerPage());
		List<Post> posts = postDao.listPublishedPostsByTag(id, pi);
		return posts;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForAuthor(Long id, Integer page, Host preferences) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		if (preferences == null) throw new IllegalArgumentException("El parametro preferences no puede ser nulo.");
		Long postCount = postDao.countPublishedPostsByAuthor(id);
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = preferences.getPostsPerIndexPage().intValue();
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> listPostsForAuthor(Long id, PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El parametro pi no puede ser nulo.");
		logger.debug("Recuperando posts por fecha, página {}, y limitando a {} posts por página", pi.getCurrentPage(), pi.getItemsPerPage());
		List<Post> posts = postDao.listPublishedPostsByAuthor(id, pi);
		return posts;
	}

	@Override
	public Post getPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de post a recuperar no puede ser nulo.");
		logger.debug("Recuperando post por id: {}", id);
		Post post = postDao.getPost(id);
		return post;
	}

	@Override
	public Post getDetailedPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de post a recuperar no puede ser nulo.");
		logger.debug("Recuperando post, autor, tags y comentarios por id de post: {}", id);
		Post post = postDao.getDetailedPost(id);
		return post;
	}

	@Override
	public Long findPostId(String titleUrl, int year, int month) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El título del post a buscar no puede ser nulo.");
		if (logger.isDebugEnabled()) { logger.debug("Buscando id de post por título url: {}", titleUrl); logger.debug("año: {}", year); logger.debug("mes: {}", month); }
		Long id = postDao.findPublishedPostId(year, month, titleUrl);
		return id;
	}




	/** COMMENTS **/
	
	@Override
	public List<Comment> getRecentComments(int count) {
		if (count < 1) throw new IllegalArgumentException("El número de comentarios a recuperar no puede ser menor que 1.");
		logger.debug("Recuperando los últimos {} comentarios", count);
		List<Object[]> fields = postDao.findLastCommentFieldsForFooter(count);
		if (fields.size() == 0) return Collections.emptyList();
		List<Comment> results = new ArrayList<Comment>(fields.size());
		for (Object[] array : fields) {
			Comment c = new Comment();
			c.setId((Long)array[0]);
			c.setAuthor(new Author());
			c.getAuthor().setDisplayName((String)array[1]);
			c.setPost(new Post());
			c.getPost().setTitle((String)array[2]);
			c.getPost().setTitleUrl((String)array[3]);
			// el setter de la fecha AsLong también inicializa el campo DateTime
			c.getPost().setPublicationDateAsLong(((Long)array[4]).longValue());
			results.add(c);
		}
		return results;
	}


	/** ARCHIVE ENTRIES **/
	
	@Override
	public List<ArchiveEntry> getRecentArchiveEntries(int count) {
		if (count < 1) throw new IllegalArgumentException("El número de entradas de archivo a recuperar no puede ser menor que 1.");
		logger.debug("Recuperando las últimas {} entradas en el archivo por meses.", count);
		List<Object[]> fields = postDao.findLastArchiveEntryFieldsForFooter(count);
		if (fields.size() == 0) return Collections.emptyList();
		List<ArchiveEntry> results = new ArrayList<ArchiveEntry>(fields.size());
		for (Object[] array : fields) {
			ArchiveEntry ae = new ArchiveEntry();
			ae.setYear(((Integer)array[0]).intValue());
			ae.setMonth(((Integer)array[1]).intValue());
			results.add(ae);
		}
		return results;
	}
	
	@Override
	public List<ArchiveEntry> getAllArchiveEntriesWithPublishedPostCount() {
		logger.debug("Recuperando todas las entradas en el archivo por meses, incluyendo el número de posts asociados a cada entrada.");
		List<Object[]> fields = postDao.listAllArchiveEntriesIncludingPublishedPostCount();
		if (fields.size() == 0) return Collections.emptyList();
		List<ArchiveEntry> results = new ArrayList<ArchiveEntry>(fields.size());
		for (Object[] array : fields) {
			ArchiveEntry ae = new ArchiveEntry();
			ae.setYear(((Integer)array[0]).intValue());
			ae.setMonth(((Integer)array[1]).intValue());
			ae.setCount(((Long)array[2]).intValue());
			results.add(ae);
		}
		return results;
	}

}