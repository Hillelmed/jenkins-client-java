package io.github.hmedioni.jenkins.client.auth;

/**
 * Supported Authentication Types for Jenkins.
 */
public enum AuthenticationType {

    USERNAME_PASSWORD("UsernamePassword", "Basic"),
    USERNAME_API_TOKEN("UsernameApiToken", "Basic"),
    ANONYMOUS("Anonymous", "");

    private final String authName;
    private final String authScheme;

    AuthenticationType(final String authName, final String authScheme) {
        this.authName = authName;
        this.authScheme = authScheme;
    }

    public String getAuthScheme() {
        return authScheme;
    }

    @Override
    public String toString() {
        return authName;
    }
}
