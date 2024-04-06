package io.github.hillelmed.jenkins.client.exception;

import org.springframework.http.*;

import java.io.*;
import java.util.*;

/**
 * Thrown when an action has breached the licensed user limit of the server, or
 * degrading the authenticated user's permission level.
 */
public class JenkinsAppException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1L;
    private final List<JenkinsError> errors;
    private final HttpStatusCode httpStatusCode;


    public JenkinsAppException(final String responseBody, final List<JenkinsError> errors, HttpStatusCode httpStatusCode) {
        super(responseBody);
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    public JenkinsAppException(final String responseBody, final List<JenkinsError> errors, HttpStatusCode httpStatusCode, final Throwable arg1) {
        super(responseBody, arg1);
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    public List<JenkinsError> errors() {
        return errors;
    }

    public HttpStatusCode code() {
        return httpStatusCode;
    }
}
