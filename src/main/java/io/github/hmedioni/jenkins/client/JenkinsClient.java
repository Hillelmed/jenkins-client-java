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
import lombok.*;
import org.springframework.web.reactive.function.client.*;

import java.io.*;

public final class JenkinsClient implements Closeable {

    @Getter
    private final JenkinsAuthentication jenkinsAuthentication;
    private final JenkinsApi jenkinsApi;

    private JenkinsClient(JenkinsProperties jenkinsProperties, JenkinsApi jenkinsApi) {
        JenkinsAuthentication jenkinsAuthentication1;
        if (jenkinsProperties != null) {
            if (jenkinsProperties.getUrl() == null) {
                jenkinsProperties.setUrl(JenkinsUtils.inferEndpoint());
            }
            if (jenkinsProperties.getUser() == null) {
                jenkinsAuthentication1 = JenkinsUtils.inferAuthentication();
            } else {
                jenkinsAuthentication1 = new JenkinsAuthentication(jenkinsProperties.getUser() + ":" + jenkinsProperties.getPassword(),
                    AuthenticationType.USERNAME_PASSWORD);
            }
        } else {
            jenkinsProperties = new JenkinsProperties();
            jenkinsProperties.setUrl(JenkinsUtils.inferEndpoint());
            jenkinsAuthentication1 = JenkinsUtils.inferAuthentication();
        }
        this.jenkinsAuthentication = jenkinsAuthentication1;
        this.jenkinsApi = jenkinsApi;
    }

    /**
     * Create a JenkinsClient inferring endpoint and authentication from
     * environment and system properties.
     */
    public static JenkinsClient create(JenkinsProperties jenkinsProperties) {
        return new JenkinsClient(jenkinsProperties, jenkinsApi(jenkinsProperties, null));
    }

    public static JenkinsClient create(JenkinsProperties jenkinsProperties, WebClient webClient) {
        return new JenkinsClient(jenkinsProperties, jenkinsApi(jenkinsProperties, webClient));
    }

    private static JenkinsApi jenkinsApi(JenkinsProperties jenkinsProperties, WebClient webClient) {
        return new JenkinsApiClientImpl(jenkinsProperties, webClient);
    }

    public JenkinsApi api() {
        return this.jenkinsApi;
    }

    @Override
    public void close() throws IOException {
        if (this.api() != null) {
            this.api().close();
        }
    }

}
