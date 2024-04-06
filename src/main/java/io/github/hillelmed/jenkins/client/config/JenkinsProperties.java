package io.github.hillelmed.jenkins.client.config;

import io.github.hillelmed.jenkins.client.*;
import lombok.*;

@Data
@NoArgsConstructor
@Builder
public class JenkinsProperties {

    private String url;
    private JenkinsAuthentication jenkinsAuthentication;

    private JenkinsProperties(String url, JenkinsAuthentication jenkinsAuthentication) {
        this.url = url;
        this.jenkinsAuthentication = jenkinsAuthentication;
    }

}
