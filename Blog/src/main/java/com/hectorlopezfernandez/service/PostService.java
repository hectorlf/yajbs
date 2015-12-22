package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Post;

public interface PostService {

	/** POSTS **/

	// recupera todos los post publicados
	public List<Post> getPublishedPosts();

	// recupera una lista de los post mas nuevos, limitando en numero por el parametro de entrada
	public List<Post> getNewestPosts(int count);

	// recupera una lista de los post ultimos posts hasta una fecha determinada, para presentar en los feeds
	// IMPORTANTE: al ser especifico para feeds, el contenido de cada post viene sin html (solo texto plano)
	public List<SimplifiedPost> getNewestPostsForFeed(int maxPostAgeInDays);

	// calcula los valores de paginacion para una lista de posts, filtrando por fecha
	public PaginationInfo computePaginationOfPostsForDate(Integer year, Integer month, Integer page);
	// recupera una lista de posts, filtrando por una fecha (el anio y el objeto de paginacion son obligatorios)
	public List<Post> listPostsForDate(Integer year, Integer month, PaginationInfo pi);

	// calcula los valores de paginacion para una lista de posts, filtrando por tag
	public PaginationInfo computePaginationOfPostsForTag(Long id, Integer page);
	// recupera una lista de posts, filtrando por el id de un tag que los relaciona, ordenado por fecha de creacion descendente
	// el objeto de paginacion es obligatorio
	public List<Post> listPostsForTag(Long tagId, PaginationInfo pi);

	// calcula los valores de paginacion para una lista de posts, filtrando por usuario
	public PaginationInfo computePaginationOfPostsForAuthor(Long id, Integer page);
	// recupera una lista de posts, filtrando por autor y ordenado por fecha de creacion descendente
	// el objeto de paginacion es obligatorio
	public List<Post> listPostsForAuthor(Long id, PaginationInfo pi);

	// recupera un post por id
	public Post getPost(Long id);

	// recupera un post por id, con su ArchiveEntry, su autor, sus tags y sus comentarios, para mostrar un detalle del post
	public Post getDetailedPost(Long id);

	// recupera el id de un post por el titulo adaptado a url, filtrando ademas por la fecha de archivo
	public Long findPostId(String titleUrl, int year, int month);


	// metodo especifico para construir el sitemap
	List<SimplifiedPost> getPostsForSitemap();


	/** ARCHIVE ENTRIES **/
	
	// recupera una lista de objetos ArchiveEntry que guardan los meses en los que ha habido posts
	public List<ArchiveEntry> getRecentArchiveEntries(int count);

	// recupera todas las entradas de archivo del sistema, ademas de la cantidad de posts en cada entrada
	public List<ArchiveEntry> getAllArchiveEntriesWithPublishedPostCount();

}