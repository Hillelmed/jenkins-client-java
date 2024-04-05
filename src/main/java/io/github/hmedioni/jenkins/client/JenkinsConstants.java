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

    public static final String DEFAULT_ENDPOINT = "http://127.0.0.1:7990";

    public static final String JCLOUDS_PROPERTY_ID = "jclouds.";
    public static final String JENKINS_REST_PROPERTY_ID = "jenkins.rest." + JCLOUDS_PROPERTY_ID;

    public static final String JCLOUDS_VARIABLE_ID = "JCLOUDS_";
    public static final String JENKINS_REST_VARIABLE_ID = "JENKINS_REST_" + JCLOUDS_VARIABLE_ID;

    public static final String OPTIONAL_FOLDER_PATH_PARAM = "optionalFolderPath";

    public static final String USER_IN_USER_API = "user";

    public static final String JENKINS_COOKIES_JSESSIONID = "JSESSIONID";

    protected JenkinsConstants() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }
}
