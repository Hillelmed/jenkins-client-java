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

package io.github.hmedioni.jenkins.client.filters;

import io.github.hmedioni.jenkins.client.*;
import io.github.hmedioni.jenkins.client.auth.*;
import io.github.hmedioni.jenkins.client.domain.crumb.*;
import io.github.hmedioni.jenkins.client.exception.*;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

import java.net.http.*;
import java.util.*;

public class JenkinsAuthenticationFilter implements ExchangeFilterFunction {

    private static final String CRUMB_HEADER = "Jenkins-Crumb";
    private final JenkinsAuthentication creds;
    private final JenkinsApi jenkinsApi;

    // key = Crumb, value = true if exception is ResourceNotFoundException false otherwise
    public JenkinsAuthenticationFilter(final JenkinsAuthentication creds, JenkinsApi jenkinsApi) {
        this.creds = creds;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        ClientRequest.Builder builder = ClientRequest.from(request);

        // Password and API Token are both Basic authentication (there is no Bearer authentication in Jenkins)
        if (creds.authType() == AuthenticationType.USERNAME_API_TOKEN || creds.authType() == AuthenticationType.USERNAME_PASSWORD) {
            final String authHeader = creds.authType().getAuthScheme() + " " + creds.authValue();
            builder.header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader);
        }

        // Anon and Password need the crumb and the cookie when POSTing
//        if (request.method().equals(HttpMethod.POST) && (creds.authType() == AuthenticationType.USERNAME_PASSWORD
//            || creds.authType() == AuthenticationType.ANONYMOUS)) {
//            final CrumbWrapper localCrumb = getCrumb();
//            if (localCrumb.getCrumb() != null) {
//                builder.header(CRUMB_HEADER, localCrumb.getCrumb().getValue());
//                Optional.ofNullable(localCrumb.getCrumb().getSessionIdCookie())
//                    .ifPresent(sessionId -> builder.header(org.springframework.http.HttpHeaders.COOKIE, sessionId));
//            } else {
//                if (Boolean.FALSE.equals(localCrumb.getABoolean())) {
//                    throw new RuntimeException("Unexpected exception being thrown: error=");
//                }
//            }
//        }
        return next.exchange(builder.build());
    }

//    private CrumbWrapper getCrumb() {
//        try {
//            final Crumb crumb = jenkinsApi.crumbIssuerApi().crumb(null).getBody();
//            return new CrumbWrapper(crumb, true);
//        } catch (JenkinsAppException e) {
//            return new CrumbWrapper(null, false);
//        }
//    }
//
}
