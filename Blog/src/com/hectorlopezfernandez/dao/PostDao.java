package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;

public interface PostDao {

	/** POSTS **/
	
	// recupera el listado completo de posts con su autor y comentarios, ordenados por id de forma descendente
	public List<Post> listPosts();

	// recupera un número de posts con su autor y comentarios usando paginación, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	public List<Post> listPosts(int firstResult, int maxResults);

	// cuenta el número de posts, filtrados por fecha
	public Long countPosts(Integer year, Integer month);
	// recupera un listado de posts filtrados por fecha, ordenados por id de forma descendente
	public List<Post> listPosts(Integer year, Integer month);
	// recupera un listado de posts filtrados por fecha y usando paginación, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	public List<Post> listPosts(Integer year, Integer month, int firstResult, int maxResults);

	// cuenta el número de posts, filtrados por tag
	public Long countPostsByTag(Long id);
	// recupera un listado de posts filtrados por tag y usando paginación, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	public List<Post> listPostsByTag(Long id, int firstResult, int maxResults);

	// cuenta el número de posts, filtrados por autor
	public Long countPostsByAuthor(Long id);
	// recupera un listado de posts filtrados por autor y usando paginación, ordenados por id de forma descendente
	// IMPORTANTE: si maxResults es menor que 1, se desactiva la paginacion
	public List<Post> listPostsByAuthor(Long id, int firstResult, int maxResults);

	// recupera un post por id
	public Post getPost(Long id);
	
	// recupera un post por id, con su ArchiveEntry, su autor, sus tags y sus comentarios, para mostrar un detalle del post
	public Post getDetailedPost(Long id);

	// recupera el id de un post por el título adaptado a url y la fecha de publicacion
	public Long findPostId(int year, int month, String titleUrl);

	// cuenta el número total de posts en el sistema
	public Long countAllPosts();
	// recupera todos los posts del sistema con paginación, sin ningún eager fecth y ordenados por id descendente
	public List<Post> getAllPosts(int firstResult, int maxResults);
	// recupera todos los posts del sistema, sin ningún eager fecth
	public List<Post> getAllPosts();
	
	
	// recupera el listado de posts cuya fecha de publicación sea igual o mayor que el parámetro (es decir, que sean posts más nuevos)
	public List<Post> listPostsPublishedAfter(long millisecondsFrom1970);

	
	// persiste un post y devuelve el id generado
	public Long savePost(Post post);

	// modifica un post
	public void modifyPost(Post post);

	// borra un post
	public void deletePost(Long id);

	
	/** COMMENTS **/
	
	// recupera una lista de campos de los últimos N comentarios
	// se usa en el footer, principalmente
	public List<Object[]> findLastCommentFieldsForFooter(int numComments);


	/** ARCHIVE ENTRIES **/
	
	// recupera una lista de entradas de archivo por meses
	public List<ArchiveEntry> listArchiveEntries();

	// recupera una lista de propiedades individuales de todas las entradas de archivo, incluyendo el número de posts asociados a cada entrada
	public List<Object[]> listAllArchiveEntriesIncludingPostCount();

	// recupera una lista de propiedades individuales de las últimas entradas de archivo, para presentar en el footer
	public List<Object[]> findLastArchiveEntryFieldsForFooter(int numEntries);

	// busca una entrada de archivo por su fecha y la devuelve. si no existe, la crea
	public ArchiveEntry findArchiveEntryCreateIfNotExists(int year, int month);

	// cuenta el número de posts que tiene asociados una entrada de archivo
	public int countPostsForArchiveEntry(Long archiveEntryId);

	// borra una entrada de archivo
	public void deleteArchiveEntry(Long id);


	/** TAGS **/
	
	// recupera una lista de etiquetas, para presentar en el footer
	public List<Tag> findMostPopularTagsForFooter(int numTags);

	// recupera un tag por id
	public Tag getTag(Long id);

	// recupera el id de un tag por el nombre adaptado a url
	public Long findTagId(String nameUrl);

	// cuenta el número total de tags en el sistema
	public Long countAllTags();
	// recupera todos los tags del sistema con paginación y ordenados por id descendente
	public List<Tag> getAllTags(int firstResult, int maxResults);
	// recupera todos los tags del sistema, sin ningún eager fecth
	public List<Tag> getAllTags();
	
	
	// persiste un tag
	public void saveTag(Tag tag);

	// modifica un tag
	public void modifyTag(Tag tag);

	// borra un tag
	public void deleteTag(Long id);


	// FIXME vomitivo
	// actualiza el contador de referencias de cada Tag
	public void updateTagRefCounts();

}