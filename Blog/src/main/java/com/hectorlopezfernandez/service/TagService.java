package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.model.Tag;

public interface TagService {

	// recupera una lista de objetos Tag que etiquetan los posts
	public List<Tag> getPopularTags(int count);

	// calcula los valores de paginacion para todos los tags del sistema
	public PaginationInfo computePaginationOfTags(Integer page);
	// recupera todos los tags del sistema, usando paginacion
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

	
	/** EXPORT **/
	
	public void exportTags();

}