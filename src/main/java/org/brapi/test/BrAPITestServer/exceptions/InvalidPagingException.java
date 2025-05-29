package org.brapi.test.BrAPITestServer.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidPagingException extends BrAPIServerException {
	private static final long serialVersionUID = 6250184179200451757L;

	private static final String pageNumExceedsTotalPages =
			"A page was requested which exceeds total amount of data available. Please make a RQ with page < totalPages - 1. " +
					"Page numbers start at 0, and you can find out totalPages by making a RQ with \"page\": 0";
		
	public InvalidPagingException(String field) {
		super(HttpStatus.BAD_REQUEST, "\'" + field + "\' value is invalid");
	}

	// This constructor should only be used when the pageNum of the RQ exceeds the total number of pages available.
	public InvalidPagingException() {
		super(HttpStatus.BAD_REQUEST, pageNumExceedsTotalPages);
	}
	
}
