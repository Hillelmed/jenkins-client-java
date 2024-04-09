package io.github.hillelmed.jenkins.client.exception;

import org.springframework.http.*;

import java.util.*;

/**
 * Thrown when an action has breached the licensed user limit of the server, or
 * degrading the authenticated user's permission level.
 */
public class CrumbMissing extends JenkinsAppException {

    public CrumbMissing(String responseBody, List<JenkinsError> errors, HttpStatusCode httpStatusCode, Throwable e) {
        super(responseBody, errors, httpStatusCode, e);
    }
}
