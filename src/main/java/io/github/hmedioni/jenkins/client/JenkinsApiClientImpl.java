package io.github.hmedioni.jenkins.client;

import io.github.hmedioni.jenkins.client.config.*;
import io.github.hmedioni.jenkins.client.features.*;
import io.github.hmedioni.jenkins.client.filters.*;
import io.github.hmedioni.jenkins.client.handlers.*;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.support.*;
import org.springframework.web.service.invoker.*;
import org.springframework.web.util.*;

import java.io.*;
import java.util.*;

import static org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode.*;

public class JenkinsApiClientImpl implements JenkinsApi {

    private final HttpServiceProxyFactory httpServiceProxyFactory;
    private final Map<Class<?>, Object> singletons;

    public JenkinsApiClientImpl(JenkinsProperties jenkinsProperties, WebClient webClient) {
        this.httpServiceProxyFactory = buildHttpServiceProxyFactory(jenkinsProperties, webClient, NONE);
        this.singletons = Collections.synchronizedMap(new HashMap<>());
    }

    private static HttpServiceProxyFactory buildHttpServiceProxyFactory(JenkinsProperties jenkinsProperties, WebClient webClient,
                                                                        DefaultUriBuilderFactory.EncodingMode encodingMode) {
        ExchangeFilterFunction jenkinsAuthenticationFilter = new JenkinsAuthenticationFilter(jenkinsProperties, jenkinsProperties.jenkinsAuthentication());
        ExchangeFilterFunction scrubNullFromPathFilter = new ScrubNullFolderParam();
        ExchangeFilterFunction jenkinsUserInjectionFilter = new JenkinsUserInjectionFilter(jenkinsProperties.jenkinsAuthentication());
        if (webClient == null) {
            DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(jenkinsProperties.getUrl());
            factory.setEncodingMode(encodingMode);

            webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .filter(jenkinsAuthenticationFilter)
                .filter(scrubNullFromPathFilter)
                .filter(jenkinsUserInjectionFilter)
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
