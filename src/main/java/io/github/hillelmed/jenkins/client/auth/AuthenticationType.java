package io.github.hillelmed.jenkins.client.auth;

import lombok.*;

/**
 * Supported Authentication Types for Jenkins.
 */
@Getter
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

}
