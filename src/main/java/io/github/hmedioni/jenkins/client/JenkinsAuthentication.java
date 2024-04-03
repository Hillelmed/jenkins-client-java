/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hmedioni.jenkins.client;


import io.github.hmedioni.jenkins.client.auth.*;
import lombok.*;
import org.springframework.web.reactive.function.client.*;

import java.nio.charset.*;
import java.util.*;


/**
 * Credentials instance for Jenkins authentication.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JenkinsAuthentication extends ExchangeFilterFunctions {

    private final AuthenticationType authType;
    private final String encodedCred;


    /**
     * Create instance of JenkinsAuthentication.
     *
     * @param authType authentication type (e.g. UsernamePassword, UsernameApiToken, Anonymous).
     */
    public JenkinsAuthentication(final String authValue, final AuthenticationType authType) {
        if (authType == AuthenticationType.USERNAME_PASSWORD || authType == AuthenticationType.USERNAME_API_TOKEN) {
            this.encodedCred = Base64.getEncoder().encodeToString(authValue.getBytes());
        } else {
            this.encodedCred = "";
        }
        this.authType = authType;
    }

    /**
     * Return the base64 encoded value of the credential.
     *
     * @return the base 64 encoded authentication value.
     */

    public String authValue() {
        return this.encodedCred;
    }

    /**
     * Return the authentication type.
     *
     * @return the authentication type.
     */
    public AuthenticationType authType() {
        return authType;
    }

    public static class Builder {

        private String identity = "anonymous";
        private String credential = identity + ":";
        private AuthenticationType authType = AuthenticationType.ANONYMOUS;

        /**
         * Set 'UsernamePassword' credentials.
         *
         * @param usernamePassword value to use for 'UsernamePassword' credentials. It can be the {@code username:password} in clear text or its base64 encoded value.
         * @return this Builder.
         */
        public Builder credentials(final String usernamePassword) {
            this.identity = Objects.requireNonNull(extractIdentity(usernamePassword));
            this.credential = Objects.requireNonNull(usernamePassword);
            this.authType = AuthenticationType.USERNAME_PASSWORD;
            return this;
        }

        /**
         * Set 'UsernameApiToken' credentials.
         *
         * @param apiTokenCredentials value to use for 'ApiToken' credentials. It can be the {@code username:apiToken} in clear text or its base64 encoded value.
         * @return this Builder.
         */
        public Builder apiToken(final String apiTokenCredentials) {
            this.identity = Objects.requireNonNull(extractIdentity(apiTokenCredentials));
            this.credential = Objects.requireNonNull(apiTokenCredentials);
            this.authType = AuthenticationType.USERNAME_API_TOKEN;
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

        /**
         * Build and instance of JenkinsCredentials.
         *
         * @return instance of JenkinsCredentials.
         */
        public JenkinsAuthentication build() {
            return new JenkinsAuthentication(credential, authType);
        }
    }
}
