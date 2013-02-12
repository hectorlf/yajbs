package com.hectorlopezfernandez.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

//@Entity
//@Table(name="users")
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@MappedSuperclass
public abstract class User extends PersistentObject {

	@Basic(optional=false)
	@Column(name="username",length=50)
	private String username;
	
	@Basic(optional=false)
	@Column(name="password",length=50)
	private String password;
	
	// getters & setters
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}