package com.hectorlopezfernandez.utils;

import javax.persistence.EntityManager;

import org.apache.lucene.store.Directory;

import com.google.inject.AbstractModule;
import com.hectorlopezfernandez.dao.BlogDao;
import com.hectorlopezfernandez.dao.BlogDaoImpl;
import com.hectorlopezfernandez.dao.PageDao;
import com.hectorlopezfernandez.dao.PageDaoImpl;
import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.dao.PostDaoImpl;
import com.hectorlopezfernandez.dao.UserDao;
import com.hectorlopezfernandez.dao.UserDaoImpl;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.BlogServiceImpl;
import com.hectorlopezfernandez.service.PageService;
import com.hectorlopezfernandez.service.PageServiceImpl;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.service.PostServiceImpl;
import com.hectorlopezfernandez.service.SearchService;
import com.hectorlopezfernandez.service.SearchServiceImpl;
import com.hectorlopezfernandez.service.UserService;
import com.hectorlopezfernandez.service.UserServiceImpl;

public class GuiceBindingModule extends AbstractModule {

	private final GuiceEntityManagerProvider entityManagerProvider;
	private final Directory luceneDirectory;

	// constructores
	
	public GuiceBindingModule(GuiceEntityManagerProvider entityManagerProvider, Directory luceneDirectory) {
		super();
		this.entityManagerProvider = entityManagerProvider;
		this.luceneDirectory = luceneDirectory;
	}

	@Override
	protected void configure() {
		bind(UserService.class).to(UserServiceImpl.class);
		bind(UserDao.class).to(UserDaoImpl.class);
		bind(BlogService.class).to(BlogServiceImpl.class);
		bind(BlogDao.class).to(BlogDaoImpl.class);
		bind(PostService.class).to(PostServiceImpl.class);
		bind(PostDao.class).to(PostDaoImpl.class);
		bind(PageService.class).to(PageServiceImpl.class);
		bind(PageDao.class).to(PageDaoImpl.class);
		bind(SearchService.class).to(SearchServiceImpl.class);
		bind(EntityManager.class).toProvider(entityManagerProvider);
		bind(Directory.class).toInstance(luceneDirectory);
	}

}