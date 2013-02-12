package com.hectorlopezfernandez.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

@MappedSuperclass
@TableGenerator(name="jpa_seq",table="sequence_table",pkColumnName="seq",pkColumnValue="blog_seq",valueColumnName="val",initialValue=100,allocationSize=2)
public abstract class PersistentObject {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="jpa_seq")
	private Long id;

	// getters & setters

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}