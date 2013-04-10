package com.hectorlopezfernandez.service;

import java.util.List;
import java.util.Set;

import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Comment;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;

public interface PostService {

	/** POSTS **/
	
	// recupera una lista de los post m�s nuevos, limitando en n�mero por el par�metro de entrada
	public List<Post> getNewestPosts(int count);

	// recupera una lista de los post �ltimos posts hasta una fecha determinada, para presentar en los feeds
	// IMPORTANTE: al ser espec�fico para feeds, el contenido de cada post viene sin html (s�lo texto plano)
	public List<SimplifiedPost> getNewestPostsForFeed(int maxPostAgeInDays);

	// calcula los valores de paginacion para una lista de posts, filtrando por fecha
	public PaginationInfo computePaginationOfPostsForDate(Integer year, Integer month, Integer page, Host preferences);
	// recupera una lista de posts, filtrando por una fecha (el a�o y el objeto de paginaci�n son obligatorios)
	public List<Post> listPostsForDate(Integer year, Integer month, PaginationInfo pi);
	// recupera una lista de posts, filtrando por una fecha (el a�o es obligatorio)
	public List<Post> listPostsForDate(Integer year, Integer month);

	// calcula los valores de paginacion para una lista de posts, filtrando por tag
	public PaginationInfo computePaginationOfPostsForTag(Long id, Integer page, Host preferences);
	// recupera una lista de posts, filtrando por el id de un tag que los relaciona, ordenado por fecha de creaci�n descendente
	// el objeto de paginaci�n es obligatorio
	public List<Post> listPostsForTag(Long tagId, PaginationInfo pi);

	// calcula los valores de paginacion para una lista de posts, filtrando por usuario
	public PaginationInfo computePaginationOfPostsForAuthor(Long id, Integer page, Host preferences);
	// recupera una lista de posts, filtrando por autor y ordenado por fecha de creaci�n descendente
	// el objeto de paginaci�n es obligatorio
	public List<Post> listPostsForAuthor(Long id, PaginationInfo pi);

	// recupera un post por id
	public Post getPost(Long id);

	// recupera un post por id, con su ArchiveEntry, su autor, sus tags y sus comentarios, para mostrar un detalle del post
	public Post getDetailedPost(Long id);

	// recupera el id de un post por el t�tulo adaptado a url, filtrando adem�s por la fecha de archivo
	public Long findPostId(String titleUrl, int year, int month);

	// calcula los valores de paginacion para todos los posts del sistema
	public PaginationInfo computePaginationOfPosts(Integer page);
	// recupera todos los posts del sistema, usando paginaci�n
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
	
	// recupera una lista de ciertos valores de los comentarios m�s nuevos, limitando en n�mero por el par�metro de entrada
	// se usa en el footer
	public List<Comment> getRecentComments(int count);


	/** ARCHIVE ENTRIES **/
	
	// recupera una lista de objetos ArchiveEntry que guardan los meses en los que ha habido posts
	public List<ArchiveEntry> getRecentArchiveEntries(int count);

	// recupera todas las entradas de archivo del sistema, adem�s de la cantidad de posts en cada entrada
	public List<ArchiveEntry> getAllArchiveEntriesWithPostCount();


	/** TAGS **/
	
	// recupera una lista de objetos Tag que etiquetan los posts
	public List<Tag> getPopularTags(int count);

	// calcula los valores de paginacion para todos los tags del sistema
	public PaginationInfo computePaginationOfTags(Integer page);
	// recupera todos los tags del sistema, usando paginaci�n
	public List<Tag> getAllTags(PaginationInfo pi);
	// recupera una lista con todos los Tag del sistema
	public List<Tag> getAllTags();

	// recupera un tag por id
	public Tag getTag(Long id);

	// recupera el id de un tag por el nombre adaptado a url
	public Long findTagId(String nameUrl);
	
	
	// persiste un nuevo tag
	public void saveTag(Tag t);
	
	// modifica un tag existente
	public void modifyTag(Tag t);

	// borra un tag
	public void deleteTag(Long id);

}