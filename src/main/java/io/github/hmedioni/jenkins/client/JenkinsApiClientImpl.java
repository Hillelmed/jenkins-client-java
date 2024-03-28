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

import io.github.hmedioni.jenkins.client.config.*;
import io.github.hmedioni.jenkins.client.features.*;
import io.github.hmedioni.jenkins.client.filters.*;
import io.github.hmedioni.jenkins.client.handlers.*;
import lombok.experimental.*;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.support.*;
import org.springframework.web.service.invoker.*;
import org.springframework.web.util.*;

import java.io.*;
import java.util.*;

import static org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES;

public class JenkinsApiClientImpl implements JenkinsApi {

    private final JenkinsProperties jenkinsProperties;
    private final WebClient webClient;
    private final HttpServiceProxyFactory httpServiceProxyFactory;
    private final Map<Class<?>, Object> singletons;

    public JenkinsApiClientImpl(JenkinsProperties jenkinsProperties, WebClient webClient) {
        this.jenkinsProperties = jenkinsProperties;
        this.webClient = webClient;
        this.httpServiceProxyFactory = buildHttpServiceProxyFactory(jenkinsProperties, webClient, TEMPLATE_AND_VALUES);
        this.singletons = Collections.synchronizedMap(new HashMap<>());
    }

    private static HttpServiceProxyFactory buildHttpServiceProxyFactory(JenkinsProperties jenkinsProperties, WebClient webClient,
                                                                        DefaultUriBuilderFactory.EncodingMode encodingMode) {
        JenkinsAuthenticationFilter bitbucketAuthenticationFilter = new JenkinsAuthenticationFilter(jenkinsProperties.jenkinsAuthentication());
        ScrubNullFolderParam scrubNullFromPathFilter = new ScrubNullFolderParam();
        if (webClient == null) {
            DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(jenkinsProperties.getUrl());
            factory.setEncodingMode(encodingMode);

            webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .filter(bitbucketAuthenticationFilter)
                .filter(scrubNullFromPathFilter)
                .filter(JenkinsErrorHandler.handler())
                .build();
        }
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
            .build();
    }


    private synchronized <T> T getSingleton(Class<T> klass, Object o) {
        return klass.cast(singletons.computeIfAbsent(klass, aClass -> o));
    }

    private synchronized <T> T getSingleton(Class<T> klass) {
        return klass.cast(singletons.computeIfAbsent(klass, httpServiceProxyFactory::createClient));
    }


    @Override
    public CrumbIssuerApi crumbIssuerApi() {
        return getSingleton(CrumbIssuerApi.class);
    }

    @Override
    public JobsApi jobsApi() {
        return getSingleton(JobsApi.class);
    }

    @Override
    public PluginManagerApi pluginManagerApi() {
        return getSingleton(PluginManagerApi.class);
    }

    @Override
    public QueueApi queueApi() {
        return getSingleton(QueueApi.class);
    }

    @Override
    public StatisticsApi statisticsApi() {
        return getSingleton(StatisticsApi.class);
    }

    @Override
    public SystemApi systemApi() {
        return getSingleton(SystemApi.class);
    }

    @Override
    public ConfigurationAsCodeApi configurationAsCodeApi() {
        return getSingleton(ConfigurationAsCodeApi.class);
    }

    @Override
    public UserApi userApi() {
        return getSingleton(UserApi.class);
    }

    @Override
    public void close() throws IOException {

    }
}
