package com.hectorlopezfernandez.service;

import java.util.List;
import java.util.Set;

import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Comment;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.dto.PaginationInfo;

public interface PostService {

	/** POSTS **/
	
	// recupera una lista de los post más nuevos, limitando en número por el parámetro de entrada
	public List<Post> getNewestPosts(int count);

	// calcula los valores de paginacion para una lista de posts, filtrando por fecha
	public PaginationInfo computePaginationOfPostsForDate(Integer year, Integer month, Integer day, Integer page, Host preferences);
	// recupera una lista de posts, filtrando por una fecha (el año es obligatorio, los demás no lo son)
	// el objeto de paginación es obligatorio
	public List<Post> listPostsForDate(Integer year, Integer month, Integer day, PaginationInfo pi);
	// recupera una lista de posts, filtrando por una fecha (el año es obligatorio, los demás no lo son)
	public List<Post> listPostsForDate(Integer year, Integer month, Integer day);

	// calcula los valores de paginacion para una lista de posts, filtrando por tag
	public PaginationInfo computePaginationOfPostsForTag(Long id, Integer page, Host preferences);
	// recupera una lista de posts, filtrando por el id de un tag que los relaciona, ordenado por fecha de creación descendente
	// el objeto de paginación es obligatorio
	public List<Post> listPostsForTag(Long tagId, PaginationInfo pi);

	// calcula los valores de paginacion para una lista de posts, filtrando por usuario
	public PaginationInfo computePaginationOfPostsForAuthor(Long id, Integer page, Host preferences);
	// recupera una lista de posts, filtrando por autor y ordenado por fecha de creación descendente
	// el objeto de paginación es obligatorio
	public List<Post> listPostsForAuthor(Long id, PaginationInfo pi);

	// recupera un post por id
	public Post getPost(Long id);

	// recupera un post por id, con su ArchiveEntry, su autor, sus tags y sus comentarios, para mostrar un detalle del post
	public Post getDetailedPost(Long id);

	// recupera el id de un post por el título adaptado a url, filtrando además por la fecha de archivo
	public Long findPostId(String titleUrl, int year, int month, int day);

	// calcula los valores de paginacion para todos los posts del sistema
	public PaginationInfo computePaginationOfPosts(Integer page);
	// recupera todos los posts del sistema, usando paginación
	public List<Post> getAllPosts(PaginationInfo pi);
	// recupera todos los posts del sistema ordenados por id descendentemente
	public List<Post> getAllPosts();
	
	
	// persiste un nuevo post
	public void savePost(Post p, Long hostId, Long authorId, Set<Long> tagIds);
	
	// modifica un post existente
	public void modifyPost(Post p, Long hostId, Long authorId, Set<Long> tagIds);

	// borra un post
	public void deletePost(Long id);

	
	/** COMMENTS **/
	
	// recupera una lista de ciertos valores de los comentarios más nuevos, limitando en número por el parámetro de entrada
	// se usa en el footer
	public List<Comment> getRecentComments(int count);


	/** ARCHIVE ENTRIES **/
	
	// recupera una lista de objetos ArchiveEntry que guardan los meses en los que ha habido posts
	public List<ArchiveEntry> getRecentArchiveEntries(int count);

	// recupera todas las entradas de archivo del sistema, además de la cantidad de posts en cada entrada
	public List<ArchiveEntry> getAllArchiveEntriesWithPostCount();


	/** TAGS **/
	
	// recupera una lista de objetos Tag que etiquetan los posts
	public List<Tag> getPopularTags(int count);

	// recupera una lista con todos los Tag del sistema
	public List<Tag> getAllTags();

	// recupera un tag por id
	public Tag getTag(Long id);

	// recupera el id de un tag por el nombre adaptado a url
	public Long findTagId(String nameUrl);

}