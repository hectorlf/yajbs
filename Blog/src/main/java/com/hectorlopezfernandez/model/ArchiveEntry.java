package com.hectorlopezfernandez.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;

@Entity
@Table(name="archive_entries")
public class ArchiveEntry extends PersistentObject {

	@Basic(optional=false)
	@Column(name="year")
	private int year;

	@Basic(optional=false)
	@Column(name="month")
	private int month;

	@Transient
	private int count;

	// getters sinteticos
	
	public DateTime getDate() {
		DateTime dt = new DateTime(year, month, 1, 0, 0);
		return dt;
	}
	
	// getters & setters
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	public int getMonth() {
		return month;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}