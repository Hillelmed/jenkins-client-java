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
