package io.github.hmedioni.jenkins.client;

/**
 * Static methods for generating test data.
 */
public class TestUtilities extends JenkinsUtils {

    public static final String TEST_CREDENTIALS_SYSTEM_PROPERTY = "test.jenkins.usernamePassword";
    public static final String TEST_CREDENTIALS_ENVIRONMENT_VARIABLE = TEST_CREDENTIALS_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    public static final String TEST_API_TOKEN_SYSTEM_PROPERTY = "test.jenkins.usernameApiToken";
    public static final String TEST_API_TOKEN_ENVIRONMENT_VARIABLE = TEST_API_TOKEN_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    private TestUtilities() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }

    /**
     * Find credentials (ApiToken, UsernamePassword, or Anonymous) from system/environment.
     *
     * @return JenkinsCredentials
     */
    public static JenkinsAuthentication inferTestAuthentication() {

        final JenkinsAuthentication inferAuth = JenkinsAuthentication.builder().build();

        // 1.) Check for API Token as this requires no crumb hence is faster
        String authValue = JenkinsUtils
            .retrieveExternalValue(TEST_API_TOKEN_SYSTEM_PROPERTY,
                TEST_API_TOKEN_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            return JenkinsAuthentication.builder().apiToken(authValue).build();
        }

        // 2.) Check for UsernamePassword auth credentials.
        authValue = JenkinsUtils
            .retrieveExternalValue(TEST_CREDENTIALS_SYSTEM_PROPERTY,
                TEST_CREDENTIALS_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            return JenkinsAuthentication.builder().credentials(authValue).build();
        }

        // 3.) If neither #1 or #2 find anything "Anonymous" access is assumed.
        return inferAuth;
    }
}
