package io.github.hmedioni.jenkins.client;

/**
 * Various constants that can be used in a global context.
 */
public class JenkinsConstants {

    public static final String ENDPOINT_SYSTEM_PROPERTY = "jenkins.rest.endpoint";
    public static final String ENDPOINT_ENVIRONMENT_VARIABLE = ENDPOINT_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    public static final String CREDENTIALS_SYSTEM_PROPERTY = "jenkins.rest.credentials";
    public static final String CREDENTIALS_ENVIRONMENT_VARIABLE = CREDENTIALS_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    public static final String API_TOKEN_SYSTEM_PROPERTY = "jenkins.rest.api.token";
    public static final String API_TOKEN_ENVIRONMENT_VARIABLE = API_TOKEN_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    public static final String DEFAULT_ENDPOINT = "http://127.0.0.1:8080";

    public static final String USER_IN_USER_API = "user";

    public static final String JENKINS_COOKIES_JSESSIONID = "JSESSIONID";

    protected JenkinsConstants() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }
}
