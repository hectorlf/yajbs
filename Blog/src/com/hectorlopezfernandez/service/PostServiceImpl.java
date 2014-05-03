package com.hectorlopezfernandez.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.BlogDao;
import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.dao.UserDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Comment;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.utils.HTMLUtils;

public class PostServiceImpl implements PostService {

	private final static Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	private final PostDao postDao;
	private final BlogDao blogDao;
	private final UserDao userDao;
	private final SearchService searchService;
	
	/* Constructores */
	
	@Inject
	public PostServiceImpl(PostDao postDao, BlogDao blogDao, UserDao userDao, SearchService searchService) {
		if (postDao == null) throw new IllegalArgumentException("El parametro postDao no puede ser nulo.");
		if (blogDao == null) throw new IllegalArgumentException("El parametro blogDao no puede ser nulo.");
		if (userDao == null) throw new IllegalArgumentException("El parametro userDao no puede ser nulo.");
		if (searchService == null) throw new IllegalArgumentException("El parametro searchService no puede ser nulo.");
		this.postDao = postDao;
		this.blogDao = blogDao;
		this.userDao = userDao;
		this.searchService = searchService;
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
		Long postCount = postDao.countPosts(year, month);
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
		List<Post> posts = postDao.listPosts(year, month, pi.getFirstItem(), pi.getItemsPerPage());
		return posts;
	}
	@Override
	public List<Post> listPostsForDate(Integer year, Integer month) {
		PaginationInfo pi = new PaginationInfo();
		List<Post> posts = listPostsForDate(year, month, pi);
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
		//TODO o activar la paginacion o quitar el limite de elementos
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
	public Long findPostId(String titleUrl, int year, int month) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El título del post a buscar no puede ser nulo.");
		if (logger.isDebugEnabled()) { logger.debug("Buscando id de post por título url: {}", titleUrl); logger.debug("año: {}", year); logger.debug("mes: {}", month); }
		Long id = postDao.findPostId(year, month, titleUrl);
		return id;
	}

	@Override
	public PaginationInfo computePaginationOfPosts(Integer page) {
		Long postCount = postDao.countAllPosts();
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = 10; // TODO esto quizás debería salir de alguna preferencia global
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> getAllPosts(PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El objeto de paginación no puede ser nulo.");
		logger.debug("Recuperando todos los posts del sistema, con paginación. Página solicitada: {}", pi.getCurrentPage());
		List<Post> posts = postDao.getAllPosts(pi.getFirstItem(), pi.getItemsPerPage());
		return posts;
	}
	@Override
	public List<Post> getAllPosts() {
		logger.debug("Recuperando todos los posts del sistema");
		List<Post> posts = postDao.getAllPosts();
		return posts;
	}


	@Override
	public void savePost(Post post, Long ownerHostId, Long authorId, Set<Long> tagIds) {
		if (post == null) throw new IllegalArgumentException("El post a guardar no puede ser nulo.");
		if (post.getId() != null) throw new IllegalArgumentException("El id de un post nuevo debe ser nulo, y este no lo es: " + post.getId().toString());
		if (ownerHostId == null) throw new IllegalArgumentException("El id del Host asociado al post no puede ser nulo.");
		if (authorId == null) throw new IllegalArgumentException("El id del Author asociado al post no puede ser nulo.");
		logger.debug("Guardando nuevo post con título: {}", post.getTitle());
		DateTime now = new DateTime();
		post.setLastModificationDate(now);
		post.setPublicationDate(now);
		ArchiveEntry ae = postDao.findArchiveEntryCreateIfNotExists(now.getYear(), now.getMonthOfYear());
		post.setArchiveEntry(ae);
		Host ownerHost = blogDao.getHost(ownerHostId);
		post.setHost(ownerHost);
		Author author = userDao.getAuthorById(authorId);
		post.setAuthor(author);
		if (tagIds != null && tagIds.size() > 0) {
			List<Tag> tags = new ArrayList<Tag>(tagIds.size());
			for (Long tagId : tagIds) {
				tags.add(postDao.getTag(tagId));
			}
			post.setTags(tags);
		}
		Long nId = postDao.savePost(post);
		// se indexa el post creado
		Post np = postDao.getPost(nId);
		searchService.addPostToIndex(np);
	}

	@Override
	public void modifyPost(Post post, Long ownerHostId, Long authorId, Set<Long> tagIds) {
		if (post == null) throw new IllegalArgumentException("El post a modificar no puede ser nulo.");
		if (post.getId() == null) throw new IllegalArgumentException("El id de un post modificado no puede ser nulo.");
		if (ownerHostId == null) throw new IllegalArgumentException("El id del Host asociado al post no puede ser nulo.");
		if (authorId == null) throw new IllegalArgumentException("El id del Author asociado al post no puede ser nulo.");
		logger.debug("Modificando post con título: {}", post.getTitle());
		DateTime now = new DateTime();
		post.setLastModificationDate(now);
		Host ownerHost = blogDao.getHost(ownerHostId);
		post.setHost(ownerHost);
		Author author = userDao.getAuthorById(authorId);
		post.setAuthor(author);
		if (tagIds != null && tagIds.size() > 0) {
			List<Tag> tags = new ArrayList<Tag>(tagIds.size());
			for (Long tagId : tagIds) {
				tags.add(postDao.getTag(tagId));
			}
			post.setTags(tags);
		}
		postDao.modifyPost(post);
		// se indexa el post modificado
		Post np = postDao.getPost(post.getId());
		searchService.addPostToIndex(np);
	}

	@Override
	public void deletePost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a borrar no puede ser nulo.");
		logger.debug("Borrando post con id: {}", id);
		Post p = postDao.getPost(id);
		// se recupera la entrada de archivo para borrarla si ya no es necesaria
		ArchiveEntry ae = p.getArchiveEntry();
		postDao.deletePost(id);
		int remainingPostCount = postDao.countPostsForArchiveEntry(ae.getId());
		if (remainingPostCount < 1) postDao.deleteArchiveEntry(ae.getId());
		// se borra del índice el post
		searchService.removePostFromIndex(id);
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
	public PaginationInfo computePaginationOfTags(Integer page) {
		Long tagCount = postDao.countAllTags();
		int total = tagCount == null ? 0 : tagCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = 10; // TODO esto quizás debería salir de alguna preferencia global
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Tag> getAllTags(PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El objeto de paginación no puede ser nulo.");
		logger.debug("Recuperando todos los tags del sistema, con paginación. Página solicitada: {}", pi.getCurrentPage());
		List<Tag> tags = postDao.getAllTags(pi.getFirstItem(), pi.getItemsPerPage());
		return tags;
	}
	@Override
	public List<Tag> getAllTags() {
		logger.debug("Recuperando todas las etiquetas.");
		List<Tag> results = postDao.getAllTags();
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


	@Override
	public void saveTag(Tag tag) {
		if (tag == null) throw new IllegalArgumentException("El tag a guardar no puede ser nulo.");
		if (tag.getId() != null) throw new IllegalArgumentException("El id de un tag nuevo debe ser nulo, y este no lo es: " + tag.getId().toString());
		logger.debug("Guardando nuevo tag con nombre: {}", tag.getName());
		postDao.saveTag(tag);
	}

	@Override
	public void modifyTag(Tag tag) {
		if (tag == null) throw new IllegalArgumentException("El tag a modificar no puede ser nulo.");
		if (tag.getId() == null) throw new IllegalArgumentException("El id de un tag modificado no puede ser nulo.");
		logger.debug("Modificando tag con nombre: {}", tag.getName());
		postDao.modifyTag(tag);
	}

	@Override
	public void deleteTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del tag a borrar no puede ser nulo.");
		logger.debug("Borrando tag con id: {}", id);
		postDao.deleteTag(id);
	}

}