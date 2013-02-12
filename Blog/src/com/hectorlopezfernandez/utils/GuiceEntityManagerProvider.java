package com.hectorlopezfernandez.utils;

import javax.persistence.EntityManager;

import com.google.inject.Provider;

public class GuiceEntityManagerProvider implements Provider<EntityManager> {

	@Override
	public EntityManager get() {
		EntityManager em = PersistenceThreadLocalHelper.get();
		return em;
	}

}
