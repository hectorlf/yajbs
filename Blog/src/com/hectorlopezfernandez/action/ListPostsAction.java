package com.hectorlopezfernandez.action;

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.service.PostService;

public class ListPostsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListPostsAction.class);

	public final static String PARAM_YEAR = "year";
	public final static String PARAM_MONTH = "month";
	public final static String PARAM_DAY = "day";

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private Integer year;
	private Integer month;
	private Integer day;
	private Integer page;
	private DateTime searchDate;
	private List<Post> posts;
	private PaginationInfo paginationInfo;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListPostsAction.execute");
		if (year == null || (day != null && month == null)) return new ForwardResolution(Error404Action.class);
		try {
			// se intenta convertir la fecha a DateTime. Si no existe la fecha, 404.
			int y = year.intValue();
			int m = month == null ? 1 : month.intValue();
			int d = day == null ? 1 : day.intValue();
			searchDate = new DateTime(y, m, d, 0, 0);
		} catch(Exception e) {
			return new ForwardResolution(Error404Action.class);
		}
		// se recuperan las preferencias
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		ctx.setAttribute("preferences", prefs);
		// se realiza la busqueda de posts
		paginationInfo = postService.computePaginationOfPostsForDate(year, month, day, page, prefs);
		posts = postService.listPostsForDate(year, month, day, paginationInfo);
		return new ForwardResolution("/WEB-INF/jsp/post-list.jsp");
	}
	
	// Getters y setters

	public List<Post> getPosts() {
		return posts;
	}

	public DateTime getSearchDate() {
		return searchDate;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

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