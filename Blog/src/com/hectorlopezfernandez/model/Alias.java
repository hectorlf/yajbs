package com.hectorlopezfernandez.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.CacheIndex;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

@Entity
@Table(name="aliases")
@NamedQueries(value={
@NamedQuery(name=Alias.ID_BY_NAME_QUERY,query="select a.id from Alias a where a.name = :hostname",hints={@QueryHint(name=QueryHints.READ_ONLY,value=HintValues.TRUE)}),
@NamedQuery(name=Alias.BY_NAME_QUERY,query="select a from Alias a where a.name = :hostname",hints={@QueryHint(name=QueryHints.READ_ONLY,value=HintValues.TRUE)})
})
public class Alias extends PersistentObject {
	
	public final static String ID_BY_NAME_QUERY = "Alias.aliasIdByName";
	public final static String BY_NAME_QUERY = "Alias.aliasByName";

	@Basic(optional=false)
	@Column(name="name",length=50,nullable=false)
	@CacheIndex
	private String name;
	
	@ManyToOne(optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="host_id",nullable=false)
	private Host host;
	
	// equals & hashcode
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || name == null || name.length() == 0) return false;
		if (!(obj instanceof Alias)) return false;
		Alias other = (Alias)obj;
		return this.getName().equals(other.getName());
	}
	
	@Override
	public int hashCode() {
		if (name == null || name.length() == 0) return 0;
		return name.hashCode();
	}
	
	// getters & setters
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}

}