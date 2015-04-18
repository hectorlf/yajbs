package com.hectorlopezfernandez.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.BlogDao;
import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Post;

public class PostServiceImpl implements PostService {

	private final static Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	private final BlogDao blogDao;
	private final PostDao postDao;
	
	/* Constructores */
	
	@Inject
	public PostServiceImpl(BlogDao blogDao, PostDao postDao) {
		if (blogDao == null) throw new IllegalArgumentException("El parametro blogDao no puede ser nulo.");
		if (postDao == null) throw new IllegalArgumentException("El parametro postDao no puede ser nulo.");
		this.blogDao = blogDao;
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
		if (count < 1) throw new IllegalArgumentException("El n�mero de posts a recuperar no puede ser menor que 1.");
		logger.debug("Recuperando los �ltimos {} posts", count);
		PaginationInfo pi = new PaginationInfo(1, count, count);
		List<Post> posts = postDao.listPublishedPosts(pi);
		return posts;
	}

	@Override
	public List<SimplifiedPost> getNewestPostsForFeed(int maxPostAgeInDays) {
		if (maxPostAgeInDays < 0) throw new IllegalArgumentException("La antig�edad en d�as de los posts a recuperar no puede ser menor que 0.");
		logger.debug("Recuperando los posts publicados hace menos de {} d�as", maxPostAgeInDays);
		long maxAge = System.currentTimeMillis() - ((long)maxPostAgeInDays * 24 * 60 * 60 * 1000);
		List<SimplifiedPost> posts = postDao.listPostsForFeedPublishedAfter(maxAge);
		return posts;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForDate(Integer year, Integer month, Integer page) {
		Long postCount = postDao.countPublishedPosts(year, month);
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = blogDao.getPreferences().getPostsPerIndexPage().intValue();
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> listPostsForDate(Integer year, Integer month, PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El parametro pi no puede ser nulo.");
		logger.debug("Recuperando posts por fecha, p�gina {}, y limitando a {} posts por p�gina", pi.getCurrentPage(), pi.getItemsPerPage());
		List<Post> posts = postDao.listPublishedPosts(year, month, pi);
		return posts;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForTag(Long id, Integer page) {
		Long postCount = postDao.countPublishedPostsByTag(id);
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = blogDao.getPreferences().getPostsPerIndexPage().intValue();
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> listPostsForTag(Long id, PaginationInfo pi) {
		//TODO o activar la paginacion o quitar el limite de elementos
		if (pi == null) throw new IllegalArgumentException("El parametro pi no puede ser nulo.");
		logger.debug("Recuperando posts por tag, p�gina {}, y limitando a {} posts por p�gina", pi.getCurrentPage(), pi.getItemsPerPage());
		List<Post> posts = postDao.listPublishedPostsByTag(id, pi);
		return posts;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForAuthor(Long id, Integer page) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		Long postCount = postDao.countPublishedPostsByAuthor(id);
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = blogDao.getPreferences().getPostsPerIndexPage().intValue();
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> listPostsForAuthor(Long id, PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El parametro pi no puede ser nulo.");
		logger.debug("Recuperando posts por fecha, p�gina {}, y limitando a {} posts por p�gina", pi.getCurrentPage(), pi.getItemsPerPage());
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
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El t�tulo del post a buscar no puede ser nulo.");
		if (logger.isDebugEnabled()) { logger.debug("Buscando id de post por t�tulo url: {}", titleUrl); logger.debug("a�o: {}", year); logger.debug("mes: {}", month); }
		Long id = postDao.findPublishedPostId(year, month, titleUrl);
		return id;
	}


	@Override
	public List<SimplifiedPost> getPostsForSitemap() {
		return postDao.getPostsForSitemap();
	}


	/** ARCHIVE ENTRIES **/
	
	@Override
	public List<ArchiveEntry> getRecentArchiveEntries(int count) {
		if (count < 1) throw new IllegalArgumentException("El n�mero de entradas de archivo a recuperar no puede ser menor que 1.");
		logger.debug("Recuperando las �ltimas {} entradas en el archivo por meses.", count);
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
		logger.debug("Recuperando todas las entradas en el archivo por meses, incluyendo el n�mero de posts asociados a cada entrada.");
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