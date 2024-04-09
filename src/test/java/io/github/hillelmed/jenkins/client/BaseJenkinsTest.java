package io.github.hillelmed.jenkins.client;

import io.github.hillelmed.jenkins.client.auth.*;
import io.github.hillelmed.jenkins.client.config.*;
import org.testng.annotations.*;

import java.io.*;
import java.nio.charset.*;

/**
 * Base class for Jenkins mock tests and some Live tests.
 */
@Test
public class BaseJenkinsTest {


    // This token can only be used by mock test as real tokens can only be obtained from jenkins itself
    public static final String USERNAME_API_TOKEN = "user:token";

    //    final protected String usernamePassword = System.getProperty("test.jenkins.user")
//        + ":"
//        + System.getProperty("test.jenkins.password");
    //    final protected String endPoint = System.getProperty("test.jenkins.endpoint");
    public final String url = "http://127.0.0.1:8080";
    public final String usernamePassword = "admin:admin";
    public final String user = "admin";
    protected final String provider;

    public BaseJenkinsTest() {
        provider = "jenkins";
    }

    /**
     * Create API from passed URL.
     * <p>
     * The default authentication is the ApiToken, for it requires no crumb and simplifies mockTests expectations.
     *
     * @param url endpoint of instance.
     * @return instance of JenkinsApi.
     */
    public JenkinsApi api(final String url) {
        return api(url, AuthenticationType.USERNAME_API_TOKEN, USERNAME_API_TOKEN);
    }

    /**
     * Create API for Anonymous access using the passed URL.
     *
     * @param url endpoint of instance.
     * @return instance of JenkinsApi.
     */
    public JenkinsApi anonymousAuthApi(final String url) {
        return api(url, AuthenticationType.ANONYMOUS, AuthenticationType.ANONYMOUS.name().toLowerCase());
    }

    /**
     * Create API for the given authentication type and string.
     *
     * @param url        the endpoint of the instance.
     * @param authType   the type of authentication.
     * @param authString the string to use as the credential.
     * @return instance of JenkinsApi.
     */
    public JenkinsApi api(final String url, final AuthenticationType authType, final String authString) {
        final JenkinsAuthentication jenkinsAuth = creds(authType, authString);
        JenkinsProperties jenkinsProperties = JenkinsProperties.builder().url(url).jenkinsAuthentication(jenkinsAuth).build();
        JenkinsClient jenkinsClient = JenkinsClient.create(jenkinsProperties);
        return jenkinsClient.api();
    }

    /**
     * Create the Jenkins Authentication instance.
     *
     * @param authType   authentication type. Falls back to anonymous when null.
     * @param authString the authentication string to use (username:password, username:apiToken, or base64 encoded).
     * @return an authentication instance.
     */
    public JenkinsAuthentication creds(final AuthenticationType authType, final String authString) {
        final JenkinsAuthentication.JenkinsAuthenticationBuilder authBuilder = new JenkinsAuthentication.JenkinsAuthenticationBuilder();
        if (authType == AuthenticationType.USERNAME_PASSWORD) {
            authBuilder.credentials(authString);
        } else if (authType == AuthenticationType.USERNAME_API_TOKEN) {
            authBuilder.apiToken(authString);
        }
        // Anonymous authentication is the default when not specified
        return authBuilder.build();
    }

    /**
     * Get the String representation of some resource to be used as payload.
     *
     * @param resource String representation of a given resource
     * @return payload in String form
     */
    public String payloadFromResource(final String resource) {
        try {
            return new String((getClass().getResourceAsStream(resource)).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
