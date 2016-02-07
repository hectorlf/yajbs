package com.hectorlopezfernandez.service;

import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.model.Post;

public interface AdminPostService {

	/** POSTS **/

	// recupera un post por id
	public Post getPost(Long id);

	// calcula los valores de paginacion para todos los posts del sistema
	public PaginationInfo computePaginationOfPosts(Integer page);
	// recupera todos los posts del sistema, usando paginacion
	public List<Post> getAllPosts(PaginationInfo pi);
	
	// persiste un nuevo post
	public void savePost(Post p, Long authorId, Set<Long> tagIds, Set<Long> relatedPostIds);
	
	// modifica un post existente
	public void modifyPost(Post p, Long authorId, Set<Long> tagIds, Set<Long> relatedPostIds);

	// borra un post
	public void deletePost(Long id);

	// publica un post
	public void publishPost(Long id);
	// despublica un post
	public void unpublishPost(Long id);
	// cambia la fecha de publicacion de un post
	public void changePostPublicationDate(Long id, DateTime publicationDate);
	
	// reprocesa los feeds, en caso de cambiar la forma de generarlos
	public void reprocessFeeds();

	
	
	/** ARCHIVE ENTRIES **/
	
}