package io.github.hmedioni.jenkins.client.config;

import io.github.hmedioni.jenkins.client.*;
import io.github.hmedioni.jenkins.client.auth.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenkinsProperties {

    private String url;
    private String user;
    private String password;
    private JenkinsAuthentication jenkinsAuthentication;

    public JenkinsProperties(String url) {
        this.url = url;
        this.user = null;
        this.password = null;
    }

    public JenkinsProperties(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public JenkinsProperties(String url, JenkinsAuthentication jenkinsAuthentication) {
        this.url = url;
        this.jenkinsAuthentication = jenkinsAuthentication;
    }

    public JenkinsAuthentication jenkinsAuthentication() {
        if (this.user == null) {
            return new JenkinsAuthentication("", AuthenticationType.ANONYMOUS);
        } else {
            return new JenkinsAuthentication(this.user + ":" + this.password,
                AuthenticationType.USERNAME_PASSWORD);
        }
    }
}
