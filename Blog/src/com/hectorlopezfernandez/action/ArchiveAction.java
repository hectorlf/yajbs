package com.hectorlopezfernandez.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.PostService;

@UrlBinding("/archive/{year}/{month}/{day}/{name}/{overhead}")
public class ArchiveAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ArchiveAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private String year;
	private String month;
	private String day;
	private String name;
	private String overhead;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ArchiveAction.execute");
		logger.debug("year: {}", year);
		logger.debug("month: {}", month);
		logger.debug("day: {}", day);
		logger.debug("name: {}", name);
		logger.debug("overhead: {}", overhead);
		// si overhead contiene algo, la url no puede ser v�lida y se manda un 404
		if (overhead != null && overhead.length() > 0) return new ForwardResolution(Error404Action.class);
		// si no se ha recibido ningun parametro, se envia al archivo de fechas
		if ((year == null || year.length() == 0) && (month == null || month.length() == 0) && (day == null || day.length() == 0) && (name == null || name.length() == 0))
			return new ForwardResolution(ListArchiveEntriesAction.class);
		// el nombre, dia, mes y a�o son campos incrementalmente obligatorios (no puede estar relleno el dia si no hay mes y a�o)
		if ((name != null && (day == null || month == null || year == null)) || (day != null && (month == null || year == null)) || (month != null && year == null))
			return new ForwardResolution(Error404Action.class);
		// los campos de la fecha, de existir, deben ser convertibles a n�mero (si no, 404)
		int y = 0, m = 0, d = 0;
		try {
			y = Integer.parseInt(year);
			if (month != null) m = Integer.parseInt(month);
			if (day != null) d = Integer.parseInt(day);
		} catch(NumberFormatException nfe) {
			return new ForwardResolution(Error404Action.class);
		}
		// si no se ha indicado un nombre de post, es una busqueda por fecha
		if (name == null || name.length() == 0) {
			// se muestra el listado de post con los par�metros recibidos (el a�o tiene que estar relleno por narices)
			ForwardResolution fr = new ForwardResolution(ListPostsAction.class).addParameter(ListPostsAction.PARAM_YEAR, y);
			if (month != null) fr.addParameter(ListPostsAction.PARAM_MONTH, m);
			if (day != null) fr.addParameter(ListPostsAction.PARAM_DAY, d);
			return fr;
		}
		// todos los campos estan rellenos, se busca un post por nombre y fecha y, si no existe, se env�a un 404
		Long postId = postService.findPostId(name, y, m, d);
		if (postId == null) return new ForwardResolution(Error404Action.class);
		// se muestra el post encontrado
		ForwardResolution fr = new ForwardResolution(ViewPostAction.class).addParameter(ViewPostAction.PARAM_ID, postId);
		return fr;
	}
	
	// Getters y setters

	public void setYear(String year) {
		this.year = year;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	// contexto y servicios

	@Override
	public BlogActionBeanContext getContext() {
		return ctx;
	}
	@Override
	public void setContext(ActionBeanContext ctx) {
		this.ctx = (BlogActionBeanContext)ctx;
	}

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

}