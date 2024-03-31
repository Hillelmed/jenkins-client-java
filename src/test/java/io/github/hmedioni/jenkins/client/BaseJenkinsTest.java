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
import io.github.hmedioni.jenkins.client.config.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;

/**
 * Base class for Jenkins mock tests and some Live tests.
 */
public class BaseJenkinsTest {

    // This token can only be used by mock test as real tokens can only be obtained from jenkins itself
    public static final String USERNAME_APITOKEN = "user:token";

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
    public JenkinsApi api(final URL url) {
        return api(url, AuthenticationType.USERNAME_API_TOKEN, USERNAME_APITOKEN);
    }

    /**
     * Create API for Anonymous access using the passed URL.
     *
     * @param url endpoint of instance.
     * @return instance of JenkinsApi.
     */
    public JenkinsApi anonymousAuthApi(final URL url) {
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
    public JenkinsApi api(final URL url, final AuthenticationType authType, final String authString) {
        final JenkinsAuthentication creds = creds(authType, authString);
        JenkinsProperties jenkinsProperties = new JenkinsProperties(url.toString(), creds);
        if (authString != null) {
            jenkinsProperties.setUser(authString.split(":")[0]);
            jenkinsProperties.setPassword(authString.split(":")[1]);
        }
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
        final JenkinsAuthentication.Builder authBuilder = new JenkinsAuthentication.Builder();
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
