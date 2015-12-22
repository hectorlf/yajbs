package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.Tag;

public interface TagDao {

	// recupera una lista de etiquetas, para presentar en el footer
	public List<Tag> findMostPopularTagsForFooter(int numTags);

	// recupera un tag por id
	public Tag getTag(Long id);

	// recupera el id de un tag por el nombre adaptado a url
	public Long findTagId(String nameUrl);

	// cuenta el numero total de tags en el sistema
	public Long countAllTags();
	// recupera todos los tags del sistema con paginacion y ordenados por id descendente
	public List<Tag> getAllTags(int firstResult, int maxResults);
	// recupera todos los tags del sistema, sin ningun eager fecth
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