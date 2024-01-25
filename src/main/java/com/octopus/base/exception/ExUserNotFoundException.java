package com.octopus.base.exception;

public class ExUserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExUserNotFoundException( String msg, Throwable t) {
		super(msg, t);
	}

	public ExUserNotFoundException( String msg) {
		super(msg);
	}

	public ExUserNotFoundException() {
		super();
	}
}