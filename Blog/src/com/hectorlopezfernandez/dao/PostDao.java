package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;

public interface PostDao {

	/** POSTS **/
	
	// recupera el listado completo de posts con su autor y comentarios, ordenados por id de forma descendente
	public List<Post> listPosts();

	// recupera un n�mero de posts con su autor y comentarios usando paginaci�n, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	public List<Post> listPosts(int firstResult, int maxResults);

	// cuenta el n�mero de posts, filtrados por fecha
	public Long countPosts(Integer year, Integer month, Integer day);
	// recupera un listado de posts filtrados por fecha, ordenados por id de forma descendente
	public List<Post> listPosts(Integer year, Integer month, Integer day);
	// recupera un listado de posts filtrados por fecha y usando paginaci�n, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	public List<Post> listPosts(Integer year, Integer month, Integer day, int firstResult, int maxResults);

	// cuenta el n�mero de posts, filtrados por tag
	public Long countPostsByTag(Long id);
	// recupera un listado de posts filtrados por tag y usando paginaci�n, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	public List<Post> listPostsByTag(Long id, int firstResult, int maxResults);

	// cuenta el n�mero de posts, filtrados por autor
	public Long countPostsByAuthor(Long id);
	// recupera un listado de posts filtrados por autor y usando paginaci�n, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	public List<Post> listPostsByAuthor(Long id, int firstResult, int maxResults);

	// recupera un post por id
	public Post getPost(Long id);
	
	// recupera un post por id, con su ArchiveEntry, su autor, sus tags y sus comentarios, para mostrar un detalle del post
	public Post getDetailedPost(Long id);

	// recupera el id de un post por el t�tulo adaptado a url y la fecha de publicacion
	public Long findPostId(int year, int month, int day, String titleUrl);

	// cuenta el n�mero total de posts en el sistema
	public Long countAllPosts();
	// recupera todos los posts del sistema con paginaci�n, sin ning�n eager fecth y ordenados por id descendente
	public List<Post> getAllPosts(int firstResult, int maxResults);
	// recupera todos los posts del sistema, sin ning�n eager fecth
	public List<Post> getAllPosts();

	
	// persiste un post
	public void savePost(Post post);

	// modifica un post
	public void modifyPost(Post post);

	
	/** COMMENTS **/
	
	// recupera una lista de campos de los �ltimos N comentarios
	// se usa en el footer, principalmente
	public List<Object[]> findLastCommentFieldsForFooter(int numComments);


	/** ARCHIVE ENTRIES **/
	
	// recupera una lista de entradas de archivo por meses
	public List<ArchiveEntry> listArchiveEntries();

	// recupera una lista de propiedades individuales de todas las entradas de archivo, incluyendo el n�mero de posts asociados a cada entrada
	public List<Object[]> listAllArchiveEntriesIncludingPostCount();

	// recupera una lista de propiedades individuales de las �ltimas entradas de archivo, para presentar en el footer
	public List<Object[]> findLastArchiveEntryFieldsForFooter(int numEntries);

	// busca una entrada de archivo por su fecha y la devuelve. si no existe, la crea
	public ArchiveEntry findArchiveEntryCreateIfNotExists(int year, int month);


	/** TAGS **/
	
	// recupera una lista de tags
	public List<Tag> listTags();

	// recupera una lista de etiquetas, para presentar en el footer
	public List<Tag> findMostPopularTagsForFooter(int numTags);

	// recupera un tag por id
	public Tag getTag(Long id);

	// recupera el id de un tag por el nombre adaptado a url
	public Long findTagId(String nameUrl);
	
	// FIXME vomitivo
	// actualiza el contador de referencias de cada Tag
	public void updateTagRefCounts();

}