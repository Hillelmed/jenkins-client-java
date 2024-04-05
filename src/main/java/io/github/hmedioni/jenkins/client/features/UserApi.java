package io.github.hmedioni.jenkins.client.features;

import io.github.hmedioni.jenkins.client.domain.user.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


/**
 * The UserApi.
 * <p>
 * Implements some of the User Rest Api defined in Jenkins.
 * For the User Api, see <a href="https://github.com/jenkinsci/jenkins/blob/master/core/src/main/java/hudson/model/User.java">User.java</a>
 * For the Api Token, see <a href="https://github.com/jenkinsci/jenkins/blob/master/core/src/main/java/jenkins/security/ApiTokenProperty.java">ApiTokenProperty.java</a>.
 */
// @RequestFilters({JenkinsAuthenticationFilter.class, JenkinsUserInjectionFilter.class})
@HttpExchange("/user")
public interface UserApi {

    // @Named("user:get")
    @GetExchange("/{user}/api/json")
    ResponseEntity<User> get(@PathVariable String user);

    // @Named("user:generateNewToken")
    @PostExchange(contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE, accept = MediaType.APPLICATION_JSON_VALUE,
        value = "/{user}/descriptorByName/jenkins.security.ApiTokenProperty/generateNewToken")
    ResponseEntity<ApiToken> generateNewToken(@PathVariable(value = "user") String user, @RequestParam(name = "newTokenName") String newTokenName);

    // @Named("user:revoke")
    @PostExchange(contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        value = "/{user}/descriptorByName/jenkins.security.ApiTokenProperty/revoke")
    ResponseEntity<Void> revoke(@PathVariable(value = "user") String user, @RequestParam(name = "tokenUuid") String newTokenName);

}
