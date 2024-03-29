package com.hectorlopezfernandez.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.PageDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPage;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.utils.Constants;

public class PageServiceImpl implements PageService {

	private final static Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

	private final PageDao pageDao;
	private final SearchService searchService;
	
	/* Constructores */
	
	@Inject
	public PageServiceImpl(PageDao pageDao, SearchService searchService) {
		if (pageDao == null) throw new IllegalArgumentException("El parametro pageDao no puede ser nulo.");
		if (searchService == null) throw new IllegalArgumentException("El parametro searchService no puede ser nulo.");
		this.pageDao = pageDao;
		this.searchService = searchService;
	}
	
	/* Metodos */

	@Override
	public Page getPage(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la pagina a recuperar no puede ser nulo.");
		logger.debug("Recuperando pagina por id: {}", id);
		Page p = pageDao.getPage(id);
		return p;
	}

	@Override
	public Long findPageId(String titleUrl) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El titulo de la pagina a buscar no puede ser nulo.");
		logger.debug("Buscando id de pagina por titulo url: {}", titleUrl);
		Long id = pageDao.findPageId(titleUrl);
		return id;
	}

	@Override
	public PaginationInfo computePaginationOfPages(Integer page) {
		Long pageCount = pageDao.countAllPages();
		int total = pageCount == null ? 0 : pageCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = 10; // TODO esto quizas deberia salir de alguna preferencia global
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Page> getAllPages(PaginationInfo pi) {
		logger.debug("Recuperando todas las paginas del sistema, con paginacion. Pagina solicitada: {}", pi.getCurrentPage());
		List<Page> pages = pageDao.getAllPages(pi.getFirstItem(), pi.getItemsPerPage());
		return pages;
	}
	@Override
	public List<Page> getAllPages() {
		logger.debug("Recuperando todas las paginas del sistema");
		List<Page> pages = pageDao.getAllPages();
		return pages;
	}

	
	@Override
	public void savePage(Page page) {
		if (page == null) throw new IllegalArgumentException("La pagina a guardar no puede ser nula.");
		if (page.getId() != null) throw new IllegalArgumentException("El id de una pagina nueva debe ser nulo, y este no lo es: " + page.getId().toString());
		logger.debug("Guardando nueva pagina con titulo: {}", page.getTitle());
		DateTime now = new DateTime();
		page.setLastModificationDate(now);
		page.setPublicationDate(now);
		Long nId = pageDao.savePage(page);
		// se indexa la pagina creada
		Page np = pageDao.getPage(nId);
		searchService.addPageToIndex(np);
	}

	@Override
	public void modifyPage(Page page) {
		if (page == null) throw new IllegalArgumentException("La pagina a guardar no puede ser nula.");
		if (page.getId() == null) throw new IllegalArgumentException("El id de una pagina modificada no puede ser nulo.");
		logger.debug("Modificando pagina con titulo: {}", page.getTitle());
		DateTime now = new DateTime();
		page.setLastModificationDate(now);
		pageDao.modifyPage(page);
		// se indexa la pagina modificada
		Page mp = pageDao.getPage(page.getId());
		searchService.addPageToIndex(mp);
	}

	@Override
	public void deletePage(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la pagina a borrar no puede ser nulo.");
		logger.debug("Borrando pagina con id: {}", id);
		pageDao.deletePage(id);
		// se borra del indice la pagina
		searchService.removePageFromIndex(id);
	}

	
	@Override
	public List<SimplifiedPage> getPagesForSitemap() {
		List<SimplifiedPage> pages = pageDao.getPagesForSitemap();
		return pages;
	}


	/** EXPORT **/

	@Override
	public void exportPages() {
		File exportOutputDir = new File(Constants.EXPORT_DIRECTORY_FILE_PATH);
		if (!exportOutputDir.isDirectory()) {
			logger.error("El directorio de destino para los exports no existe");
		}
		File exportOutput = new File(exportOutputDir, "pages.xml");
		if (!exportOutput.exists()) {
			try {
				boolean created = exportOutput.createNewFile();
				if (!created) throw new IOException("createNewFile returned false");
			} catch (Exception e) {
				logger.error("El archivo de exportacion no pudo ser creado", e);
			}
		}
		if (!exportOutput.canWrite()) {
			logger.error("No se puede escribir en el archivo " + Constants.EXPORT_DIRECTORY_FILE_PATH + File.pathSeparator + "pages.xml");
			return;
		}
		try {
			FileWriter writer = new FileWriter(exportOutput);
			writer.append("<Pages>").append("\n");
			List<Page> pages = pageDao.getAllPages();
			for (Page p : pages) {
				writer.append("<Page>").append("\n");
				writer.append("<Id>").append(p.getId().toString()).append("</Id>").append("\n");
				writer.append("<CreationDate>").append(String.valueOf(p.getLastModificationDateAsLong())).append("</CreationDate>").append("\n");
				writer.append("<LastModificationDate>").append(String.valueOf(p.getLastModificationDateAsLong())).append("</LastModificationDate>").append("\n");
				writer.append("<PublicationDate>").append(String.valueOf(p.getPublicationDateAsLong())).append("</PublicationDate>").append("\n");
				writer.append("<Content>").append(StringEscapeUtils.escapeXml(p.getContent())).append("</Content>").append("\n");
				writer.append("<MetaDescription>").append(StringEscapeUtils.escapeXml(p.getMetaDescription())).append("</MetaDescription>").append("\n");
				writer.append("<Title>").append(StringEscapeUtils.escapeXml(p.getTitle())).append("</Title>").append("\n");
				writer.append("<Slug>").append(StringEscapeUtils.escapeXml(p.getTitleUrl())).append("</Slug>").append("\n");
				writer.append("</Page>").append("\n");
			}
			writer.append("</Pages>").append("\n");
			writer.close();
		} catch (Exception e) {
			logger.error("Ocurrio un error al exportar las pages", e);
		}	
	}

}