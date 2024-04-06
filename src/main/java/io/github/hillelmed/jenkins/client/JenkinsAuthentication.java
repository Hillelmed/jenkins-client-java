package io.github.hillelmed.jenkins.client;


import io.github.hillelmed.jenkins.client.auth.*;
import lombok.*;
import org.springframework.web.reactive.function.client.*;

import java.nio.charset.*;
import java.util.*;


/**
 * Credentials instance for Jenkins authentication.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder(builderClassName = "JenkinsAuthenticationBuilder")
public class JenkinsAuthentication extends ExchangeFilterFunctions {

    private final AuthenticationType authType;
    private final String identity;
    private final String credential;
    private final String encodedCred;


    public static class JenkinsAuthenticationBuilder {

        private String identity = "anonymous";
        private String credential = identity + ":";
        private String encodedCred = Base64.getEncoder().encodeToString(credential.getBytes(StandardCharsets.UTF_8));
        private AuthenticationType authType = AuthenticationType.ANONYMOUS;

        /**
         * Set 'UsernamePassword' credentials.
         *
         * @param usernamePassword value to use for 'UsernamePassword' credentials. It can be the {@code username:password} in clear text or its base64 encoded value.
         * @return this Builder.
         */
        public JenkinsAuthenticationBuilder credentials(final String usernamePassword) {
            this.identity = Objects.requireNonNull(extractIdentity(usernamePassword));
            this.credential = Objects.requireNonNull(usernamePassword);
            this.authType = AuthenticationType.USERNAME_PASSWORD;

            this.encodedCred = Base64.getEncoder().encodeToString(this.credential.getBytes());
            return this;
        }

        public JenkinsAuthenticationBuilder encodedCred(final String encodedCred) {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedCred);
            String decodedString = new String(decodedBytes);
            this.identity = Objects.requireNonNull(extractIdentity(decodedString));
            this.credential = Objects.requireNonNull(decodedString);
            this.encodedCred = encodedCred;
            if (authType == AuthenticationType.ANONYMOUS) {
                this.authType = AuthenticationType.USERNAME_PASSWORD;
            }
            return this;
        }

        /**
         * Set 'UsernameApiToken' credentials.
         *
         * @param apiTokenCredentials value to use for 'ApiToken' credentials. It can be the {@code username:apiToken} in clear text or its base64 encoded value.
         * @return this Builder.
         */
        public JenkinsAuthenticationBuilder apiToken(final String apiTokenCredentials) {
            this.identity = Objects.requireNonNull(extractIdentity(apiTokenCredentials));
            this.credential = Objects.requireNonNull(apiTokenCredentials);
            this.authType = AuthenticationType.USERNAME_API_TOKEN;
            this.encodedCred = Base64.getEncoder().encodeToString(this.credential.getBytes());
            return this;
        }

        /**
         * Extract the identity from the credential.
         * <p>
         * The credential is entered by the user in one of two forms:
         * <ol>
         *  <li>Colon separated form: <code>username:password</code> or <code>username:password</code>
         *  <li>Base64 encoded of the colon separated form.
         * </ol>
         * Either way the identity is the username, and it can be extracted directly or by decoding.
         */
        private String extractIdentity(final String credentialString) {
            String decoded;
            if (!credentialString.contains(":")) {
                decoded = new String(Base64.getDecoder().decode(credentialString), StandardCharsets.UTF_8);
            } else {
                decoded = credentialString;
            }
            if (!decoded.contains(":")) {
                throw new RuntimeException("Unable to detect the identity being used in '" + credentialString + "'. Supported types are a user:password, or a user:apiToken, or their base64 encoded value.");
            }
            if (decoded.equals(":")) {
                return "";
            }
            return decoded.split(":")[0];
        }


    }
}
