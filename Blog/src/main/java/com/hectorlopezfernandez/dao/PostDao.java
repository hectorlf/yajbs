package com.hectorlopezfernandez.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Post;

public interface PostDao {

	/** ADMIN RELATED **/

	// cuenta el numero total de posts en el sistema
	public Long countAllPosts();
	// recupera todos los posts del sistema con paginacion, sin ningun eager fecth y ordenados por id descendente
	public List<Post> getAllPosts(PaginationInfo pagination);
	// cuenta el numero total de posts que tiene asociados una entrada de archivo
	public int countAllPostsForArchiveEntry(Long archiveEntryId);
	
	// persiste un post y devuelve el id generado
	public Long savePost(Post post);
	// modifica un post
	public void modifyPost(Post post);
	// borra un post
	public void deletePost(Long id);
	// cambia el estado de un post a publicado
	public void publishPost(Long id);
	// cambia el estado de un post a no publicado
	public void unpublishPost(Long id);
	// cambia la fecha de publicacion de un post
	public void changePostPublicationDate(Long id, DateTime publicationDate);


	/** POSTS **/
	
	// cuenta el numero total de posts publicados
	public Long countPublishedPosts();
	// recupera el listado completo de posts con su autor y comentarios, ordenados por id de forma descendente
	// se usa un objeto de paginacion que puede estar activo o no, pero es obligatorio
	public List<Post> listPublishedPosts(PaginationInfo pagination);

	// cuenta el numero de posts, filtrados por fecha
	public Long countPublishedPosts(Integer year, Integer month);
	// recupera un listado de posts filtrados por fecha, ordenados por id de forma descendente
	// se usa un objeto de paginacion que puede estar activo o no, pero es obligatorio
	public List<Post> listPublishedPosts(Integer year, Integer month, PaginationInfo pagination);

	// cuenta el numero de posts, filtrados por tag
	public Long countPublishedPostsByTag(Long id);
	// recupera un listado de posts filtrados por tag, ordenados por id de forma descendente
	// se usa un objeto de paginacion que puede estar activo o no, pero es obligatorio
	public List<Post> listPublishedPostsByTag(Long id, PaginationInfo pagination);

	// cuenta el numero de posts, filtrados por autor
	public Long countPublishedPostsByAuthor(Long id);
	// recupera un listado de posts filtrados por autor, ordenados por id de forma descendente
	// se usa un objeto de paginacion que puede estar activo o no, pero es obligatorio
	public List<Post> listPublishedPostsByAuthor(Long id, PaginationInfo pagination);

	// recupera un post por id
	public Post getPost(Long id);
	
	// recupera un post por id, con su ArchiveEntry, su autor, sus tags y sus comentarios, para mostrar un detalle del post
	public Post getDetailedPost(Long id);

	// recupera el id de un post por el titulo adaptado a url y la fecha de publicacion
	public Long findPublishedPostId(int year, int month, String titleUrl);
	
	
	// metodo especifico para recuperar en una sola consulta todo lo necesario para mostrar los feeds
	public List<SimplifiedPost> listPostsForFeedPublishedAfter(long millisecondsFrom1970);


	// metodo especifico para construir el sitemap
	List<SimplifiedPost> getPostsForSitemap();
	
	
	/** ARCHIVE ENTRIES **/
	
	// recupera una lista de entradas de archivo por meses
	public List<ArchiveEntry> listArchiveEntries();

	// recupera una lista de propiedades individuales de todas las entradas de archivo, incluyendo el numero de posts asociados a cada entrada
	public List<Object[]> listAllArchiveEntriesIncludingPublishedPostCount();

	// recupera una lista de propiedades individuales de las ultimas entradas de archivo, para presentar en el footer
	public List<Object[]> findLastArchiveEntryFieldsForFooter(int numEntries);

	// busca una entrada de archivo por su fecha y la devuelve. si no existe, la crea
	public ArchiveEntry findArchiveEntryCreateIfNotExists(int year, int month);

	// cuenta el numero de posts publicados que tiene asociados una entrada de archivo
	public int countPublishedPostsForArchiveEntry(Long archiveEntryId);

	// borra una entrada de archivo
	public void deleteArchiveEntry(Long id);

}