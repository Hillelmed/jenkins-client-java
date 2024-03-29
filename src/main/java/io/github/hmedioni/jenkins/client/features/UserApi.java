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

package io.github.hmedioni.jenkins.client.features;

import io.github.hmedioni.jenkins.client.domain.common.*;
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
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    User get();

    // @Named("user:generateNewToken")
    @PostExchange("/{user}/descriptorByName/jenkins.security.ApiTokenProperty/generateNewToken")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    // @Payload("newTokenName={tokenName}")
    ApiToken generateNewToken(@RequestBody String tokenName);

    // @Named("user:revoke")
    @PostExchange("/{user}/descriptorByName/jenkins.security.ApiTokenProperty/revoke")
    // @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    //@ResponseParser(RequestStatusParser.class)
    // @Produces(MediaType.APPLICATION_FORM_URLENCODED)
//    @Payload("tokenUuid={tokenUuid}")
    ResponseEntity<Void> revoke(@RequestBody String tokenUuid);
}
