package io.github.hmedioni.jenkins.client.config;

import io.github.hmedioni.jenkins.client.*;
import lombok.*;

@Data
@NoArgsConstructor
@Builder
public class JenkinsProperties {

    private String url;
    private JenkinsAuthentication jenkinsAuthentication;

    public JenkinsProperties(String url, JenkinsAuthentication jenkinsAuthentication) {
        this.url = url;
        this.jenkinsAuthentication = jenkinsAuthentication;
    }

}
