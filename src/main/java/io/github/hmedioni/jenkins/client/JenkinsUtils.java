package io.github.hmedioni.jenkins.client;

import org.springframework.lang.*;

import static io.github.hmedioni.jenkins.client.JenkinsConstants.*;

/**
 * Collection of static methods to be used globally.
 */
public class JenkinsUtils {


    /**
     * Convert passed Map into a JsonElement.
     *
     * @param input the Map to convert.
     * @return JsonElement or empty JsonElement if `input` is null.
     */

    /**
     * Convert passed Map into a JsonElement.
     *
     * @param input the Map to convert.
     * @return JsonElement or empty JsonElement if `input` is null.
     */

    /**
     * Convert passed String into a JsonElement.
     *
     * @param input the String to convert.
     * @return JsonElement or empty JsonElement if `input` is null.
     */

    /**
     * If the passed systemProperty is non-null we will attempt to query
     * the `System Properties` for a value and return it. If no value
     * was found, and environmentVariable is non-null, we will attempt to
     * query the `Environment Variables` for a value and return it. If
     * both are either null or can't be found than null will be returned.
     *
     * @param systemProperty      possibly existent System Property.
     * @param environmentVariable possibly existent Environment Variable.
     * @return found external value or null.
     */
    public static String retrieveExternalValue(@Nullable final String systemProperty,
                                               @Nullable final String environmentVariable) {

        // 1.) Search for System Property
        if (systemProperty != null) {
            final String value = System.getProperty(systemProperty);
            if (value != null) {
                return value;
            }
        }

        if (environmentVariable != null) {
            return System.getenv().get(environmentVariable);
        }

        return null;
    }

    /**
     * Find endpoint searching first within `System Properties` and
     * then within `Environment Variables` returning whichever has a
     * value first.
     *
     * @return endpoint or null if it can't be found.
     */
    public static String inferEndpoint() {
        final String possibleValue = JenkinsUtils
            .retrieveExternalValue(ENDPOINT_SYSTEM_PROPERTY,
                ENDPOINT_ENVIRONMENT_VARIABLE);
        return possibleValue != null ? possibleValue : DEFAULT_ENDPOINT;
    }

    /**
     * Find credentials (ApiToken, UsernamePassword, or Anonymous) from system/environment.
     *
     * @return JenkinsAuthentication
     */
    public static JenkinsAuthentication inferAuthentication() {

        final JenkinsAuthentication.Builder inferAuth = new JenkinsAuthentication.Builder();
        // 1.) Check for API Token as this requires no crumb hence is faster
        String authValue = JenkinsUtils
            .retrieveExternalValue(API_TOKEN_SYSTEM_PROPERTY,
                API_TOKEN_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.apiToken(authValue);
            return inferAuth.build();
        }

        // 2.) Check for UsernamePassword auth credentials.
        authValue = JenkinsUtils
            .retrieveExternalValue(CREDENTIALS_SYSTEM_PROPERTY,
                CREDENTIALS_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.credentials(authValue);
            return inferAuth.build();
        }

        // 3.) If neither #1 or #2 find anything "Anonymous" access is assumed.
        return inferAuth.build();
    }
}
