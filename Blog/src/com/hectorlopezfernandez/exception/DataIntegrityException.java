package com.hectorlopezfernandez.exception;

public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataIntegrityException() {
	}
	
	public DataIntegrityException(String msg) {
		super(msg);
	}

}