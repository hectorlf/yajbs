package com.hectorlopezfernandez.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Comment;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.dto.PaginationInfo;

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
	public List<Post> getNewestPosts(int count) {
		if (count < 1) throw new IllegalArgumentException("El número de posts a recuperar no puede ser menor que 1.");
		logger.debug("Recuperando los últimos {} posts", count);
		List<Post> posts = postDao.listPosts(0, count);
		return posts;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForDate(Integer year, Integer month, Integer day, Integer page, Host preferences) {
		if (preferences == null) throw new IllegalArgumentException("El parametro preferences no puede ser nulo.");
		Long postCount = postDao.countPosts(year, month, day);
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = preferences.getPostsPerIndexPage().intValue();
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> listPostsForDate(Integer year, Integer month, Integer day, PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El parametro pi no puede ser nulo.");
		logger.debug("Recuperando posts por fecha, página {}, y limitando a {} posts por página", pi.getCurrentPage(), pi.getItemsPerPage());
		List<Post> posts = postDao.listPosts(year, month, day, pi.getFirstItem(), pi.getItemsPerPage());
		return posts;
	}
	@Override
	public List<Post> listPostsForDate(Integer year, Integer month, Integer day) {
		PaginationInfo pi = new PaginationInfo();
		List<Post> posts = listPostsForDate(year, month, day, pi);
		return posts;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForTag(Long id, Integer page, Host preferences) {
		if (preferences == null) throw new IllegalArgumentException("El parametro preferences no puede ser nulo.");
		Long postCount = postDao.countPostsByTag(id);
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = preferences.getPostsPerIndexPage().intValue();
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> listPostsForTag(Long id, PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El parametro pi no puede ser nulo.");
		logger.debug("Recuperando posts por tag, página {}, y limitando a {} posts por página", pi.getCurrentPage(), pi.getItemsPerPage());
		List<Post> posts = postDao.listPostsByTag(id, pi.getFirstItem(), pi.getItemsPerPage());
		return posts;
	}

	@Override
	public PaginationInfo computePaginationOfPostsForAuthor(Long id, Integer page, Host preferences) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		if (preferences == null) throw new IllegalArgumentException("El parametro preferences no puede ser nulo.");
		Long postCount = postDao.countPostsByAuthor(id);
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
		List<Post> posts = postDao.listPostsByAuthor(id, pi.getFirstItem(), pi.getItemsPerPage());
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
	public Long findPostId(String titleUrl, int year, int month, int day) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El título del post a buscar no puede ser nulo.");
		if (logger.isDebugEnabled()) { logger.debug("Buscando id de post por título url: {}", titleUrl); logger.debug("año: {}", year); logger.debug("mes: {}", month); logger.debug("dia: {}", day); }
		Long id = postDao.findPostId(year, month, day, titleUrl);
		return id;
	}
	
	@Override
	public List<Post> getAllPosts() {
		logger.debug("Recuperando todos los posts del sistema");
		List<Post> posts = postDao.getAllPosts();
		return posts;
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
			DateTime dt = new DateTime(((Long)array[4]).longValue());
			c.getPost().setPublicationDate(dt);
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
	public List<ArchiveEntry> getAllArchiveEntriesWithPostCount() {
		logger.debug("Recuperando todas las entradas en el archivo por meses, incluyendo el número de posts asociados a cada entrada.");
		List<Object[]> fields = postDao.listAllArchiveEntriesIncludingPostCount();
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


	/** TAGS **/
	
	@Override
	public List<Tag> getPopularTags(int count) {
		if (count < 1) throw new IllegalArgumentException("El número de etiquetas a recuperar no puede ser menor que 1.");
		logger.debug("Recuperando las {} etiquetas más populares.", count);
		List<Tag> results = postDao.findMostPopularTagsForFooter(count);
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}
	
	@Override
	public List<Tag> getAllTags() {
		logger.debug("Recuperando todas las etiquetas.");
		List<Tag> results = postDao.findMostPopularTagsForFooter(100);
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}

	@Override
	public Tag getTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del tag a recuperar no puede ser nulo.");
		logger.debug("Recuperando tag por id: {}", id);
		Tag t = postDao.getTag(id);
		return t;
	}

	@Override
	public Long findTagId(String nameUrl) {
		if (nameUrl == null || nameUrl.length() == 0) throw new IllegalArgumentException("El nombre del tag a buscar no puede ser nulo.");
		logger.debug("Buscando id de tag por nombre url: {}", nameUrl);
		Long id = postDao.findTagId(nameUrl);
		return id;
	}

}