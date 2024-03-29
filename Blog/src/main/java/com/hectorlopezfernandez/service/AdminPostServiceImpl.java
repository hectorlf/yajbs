package com.hectorlopezfernandez.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.dao.TagDao;
import com.hectorlopezfernandez.dao.UserDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.utils.Constants;
import com.hectorlopezfernandez.utils.HTMLUtils;

public class AdminPostServiceImpl implements AdminPostService {

	private final static Logger logger = LoggerFactory.getLogger(AdminPostServiceImpl.class);

	private final PostDao postDao;
	private final TagDao tagDao;
	private final UserDao userDao;
	private final SearchService searchService;
	
	/* Constructores */
	
	@Inject
	public AdminPostServiceImpl(PostDao postDao, TagDao tagDao, UserDao userDao, SearchService searchService) {
		if (postDao == null) throw new IllegalArgumentException("El parametro postDao no puede ser nulo.");
		if (tagDao == null) throw new IllegalArgumentException("El parametro tagDao no puede ser nulo.");
		if (userDao == null) throw new IllegalArgumentException("El parametro userDao no puede ser nulo.");
		if (searchService == null) throw new IllegalArgumentException("El parametro searchService no puede ser nulo.");
		this.postDao = postDao;
		this.tagDao = tagDao;
		this.userDao = userDao;
		this.searchService = searchService;
	}
	
	/* Metodos */

	/** POSTS **/
	
	@Override
	public Post getPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de post a recuperar no puede ser nulo.");
		logger.debug("Recuperando post por id: {}", id);
		Post post = postDao.getPost(id);
		return post;
	}

	@Override
	public PaginationInfo computePaginationOfPosts(Integer page) {
		Long postCount = postDao.countAllPosts();
		int total = postCount == null ? 0 : postCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = 10; // TODO esto quizas deberia salir de alguna preferencia global
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Post> getAllPosts(PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El objeto de paginacion no puede ser nulo.");
		logger.debug("Recuperando todos los posts del sistema, con paginacion. Pagina solicitada: {}", pi.getCurrentPage());
		List<Post> posts = postDao.getAllPosts(pi);
		return posts;
	}

	@Override
	public void savePost(Post post, Long authorId, Set<Long> tagIds, Set<Long> relatedPostIds) {
		if (post == null) throw new IllegalArgumentException("El post a guardar no puede ser nulo.");
		if (post.getId() != null) throw new IllegalArgumentException("El id de un post nuevo debe ser nulo, y este no lo es: " + post.getId().toString());
		if (authorId == null) throw new IllegalArgumentException("El id del Author asociado al post no puede ser nulo.");
		logger.debug("Guardando nuevo post con titulo: {}", post.getTitle());
		DateTime now = new DateTime();
		post.setCreationDate(now);
		post.setLastModificationDate(now);
		post.setPublicationDate(now);
		post.setFeedContent(HTMLUtils.parseTextForFeeds(post.getExcerpt()));
		// el post se crea como no publicado, por lo que no se indexa en lucene
		post.setPublished(false);
		ArchiveEntry ae = postDao.findArchiveEntryCreateIfNotExists(now.getYear(), now.getMonthOfYear());
		post.setArchiveEntry(ae);
		Author author = userDao.getAuthorById(authorId);
		post.setAuthor(author);
		if (tagIds != null && tagIds.size() > 0) {
			List<Tag> tags = new ArrayList<Tag>(tagIds.size());
			for (Long tagId : tagIds) {
				tags.add(tagDao.getTag(tagId));
			}
			post.setTags(tags);
		}
		if (relatedPostIds != null && relatedPostIds.size() > 0) {
			List<Post> relatedPosts = new ArrayList<Post>(relatedPostIds.size());
			for (Long relatedPostId : relatedPostIds) {
				relatedPosts.add(postDao.getPost(relatedPostId));
			}
			post.setRelatedPosts(relatedPosts);
		}
		postDao.savePost(post);
	}

	@Override
	public void modifyPost(Post post, Long authorId, Set<Long> tagIds, Set<Long> relatedPostIds) {
		if (post == null) throw new IllegalArgumentException("El post a modificar no puede ser nulo.");
		if (post.getId() == null) throw new IllegalArgumentException("El id de un post modificado no puede ser nulo.");
		if (authorId == null) throw new IllegalArgumentException("El id del Author asociado al post no puede ser nulo.");
		logger.debug("Modificando post con titulo: {}", post.getTitle());
		DateTime now = new DateTime();
		post.setLastModificationDate(now);
		post.setFeedContent(HTMLUtils.parseTextForFeeds(post.getExcerpt()));
		Author author = userDao.getAuthorById(authorId);
		post.setAuthor(author);
		if (tagIds != null && tagIds.size() > 0) {
			List<Tag> tags = new ArrayList<Tag>(tagIds.size());
			for (Long tagId : tagIds) {
				tags.add(tagDao.getTag(tagId));
			}
			post.setTags(tags);
		}
		if (relatedPostIds != null && relatedPostIds.size() > 0) {
			List<Post> relatedPosts = new ArrayList<Post>(relatedPostIds.size());
			for (Long relatedPostId : relatedPostIds) {
				relatedPosts.add(postDao.getPost(relatedPostId));
			}
			post.setRelatedPosts(relatedPosts);
		}
		postDao.modifyPost(post);
		// se indexa el post modificado, si esta publicado
		Post np = postDao.getPost(post.getId());
		if (np.isPublished()) searchService.addPostToIndex(np);
	}

	@Override
	public void deletePost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a borrar no puede ser nulo.");
		logger.debug("Borrando post con id: {}", id);
		Post p = postDao.getPost(id);
		// se recupera la entrada de archivo para borrarla si ya no es necesaria
		ArchiveEntry ae = p.getArchiveEntry();
		postDao.deletePost(id);
		int remainingPostCount = postDao.countAllPostsForArchiveEntry(ae.getId());
		if (remainingPostCount < 1) postDao.deleteArchiveEntry(ae.getId());
		// se borra del indice el post
		searchService.removePostFromIndex(id);
	}

	@Override
	public void publishPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a publicar no puede ser nulo.");
		logger.debug("Publicando post con id: {}", id);
		postDao.publishPost(id);
		// se indexa el post modificado
		Post np = postDao.getPost(id);
		searchService.addPostToIndex(np);
	}
	
	@Override
	public void unpublishPost(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del post a despublicar no puede ser nulo.");
		logger.debug("Despublicando post con id: {}", id);
		postDao.unpublishPost(id);
		// se borra el post del indice
		searchService.removePostFromIndex(id);
	}
	
	@Override
	public void changePostPublicationDate(Long id, DateTime publicationDate) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		if (publicationDate == null) throw new IllegalArgumentException("El parametro publicationDate no puede ser nulo.");
		logger.debug("Cambiando la fecha de publicacion del post con id {} a {}", id, publicationDate);
		postDao.changePostPublicationDate(id, publicationDate);
		Post p = postDao.getPost(id);
		// se cambia el ArchiveEntry, a pesar de que puede ser el mismo
		ArchiveEntry oldAe = p.getArchiveEntry();
		ArchiveEntry newAe = postDao.findArchiveEntryCreateIfNotExists(publicationDate.getYear(), publicationDate.getMonthOfYear());
		p.setArchiveEntry(newAe);
		int remainingPostCount = postDao.countAllPostsForArchiveEntry(oldAe.getId());
		if (remainingPostCount < 1) postDao.deleteArchiveEntry(oldAe.getId());
		// se indexa el post modificado, si esta publicado
		if (p.isPublished()) searchService.addPostToIndex(p);
	}


	@Override
	public void reprocessFeeds() {
		logger.debug("Reprocesando los feeds de todas las entradas");
		List<Post> posts = postDao.getAllPosts(PaginationInfo.DISABLED);
		if (posts.size() == 0) return;
		for (Post p : posts) {
			String feedText = HTMLUtils.parseTextForFeeds(p.getExcerpt());
			p.setFeedContent(feedText);
		}
	}


	/** ARCHIVE ENTRIES **/

	
	/** EXPORT **/

	@Override
	public void exportPosts() {
		File exportOutputDir = new File(Constants.EXPORT_DIRECTORY_FILE_PATH);
		if (!exportOutputDir.isDirectory()) {
			logger.error("El directorio de destino para los exports no existe");
		}
		File exportOutput = new File(exportOutputDir, "posts.xml");
		if (!exportOutput.exists()) {
			try {
				boolean created = exportOutput.createNewFile();
				if (!created) throw new IOException("createNewFile returned false");
			} catch (Exception e) {
				logger.error("El archivo de exportacion no pudo ser creado", e);
			}
		}
		if (!exportOutput.canWrite()) {
			logger.error("No se puede escribir en el archivo " + Constants.EXPORT_DIRECTORY_FILE_PATH + File.pathSeparator + "posts.xml");
			return;
		}
		try {
			FileWriter writer = new FileWriter(exportOutput);
			writer.append("<Posts>").append("\n");
			List<Post> posts = postDao.getAllPosts(PaginationInfo.DISABLED);
			for (Post p : posts) {
				writer.append("<Post>").append("\n");
				writer.append("<Id>").append(p.getId().toString()).append("</Id>").append("\n");
				writer.append("<CreationDate>").append(String.valueOf(p.getCreationDateAsLong())).append("</CreationDate>").append("\n");
				writer.append("<LastModificationDate>").append(String.valueOf(p.getLastModificationDateAsLong())).append("</LastModificationDate>").append("\n");
				writer.append("<PublicationDate>").append(String.valueOf(p.getPublicationDateAsLong())).append("</PublicationDate>").append("\n");
				writer.append("<Content>").append(StringEscapeUtils.escapeXml(p.getContent())).append("</Content>").append("\n");
				writer.append("<Excerpt>").append(StringEscapeUtils.escapeXml(p.getExcerpt())).append("</Excerpt>").append("\n");
				writer.append("<MetaDescription>").append(StringEscapeUtils.escapeXml(p.getMetaDescription())).append("</MetaDescription>").append("\n");
				writer.append("<Tags>").append("\n");
				for (Tag t : p.getTags()) {
					writer.append("<Tag>").append(t.getId().toString()).append("</Tag>").append("\n");
				}
				writer.append("</Tags>").append("\n");
				writer.append("<Title>").append(StringEscapeUtils.escapeXml(p.getTitle())).append("</Title>").append("\n");
				writer.append("<Slug>").append(StringEscapeUtils.escapeXml(p.getTitleUrl())).append("</Slug>").append("\n");
				writer.append("</Post>").append("\n");
			}
			writer.append("</Posts>").append("\n");
			writer.close();
		} catch (Exception e) {
			logger.error("Ocurrio un error al exportar los posts", e);
		}
	}

}