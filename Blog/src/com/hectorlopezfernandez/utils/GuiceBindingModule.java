package com.hectorlopezfernandez.utils;

import javax.persistence.EntityManager;

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
import com.hectorlopezfernandez.service.UserService;
import com.hectorlopezfernandez.service.UserServiceImpl;

public class GuiceBindingModule extends AbstractModule {

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
		bind(EntityManager.class).toProvider(GuiceEntityManagerProvider.class);
	}

}