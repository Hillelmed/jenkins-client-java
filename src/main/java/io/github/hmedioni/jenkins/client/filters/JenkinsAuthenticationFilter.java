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
import io.github.hmedioni.jenkins.client.config.*;
import io.github.hmedioni.jenkins.client.domain.crumb.*;
import io.github.hmedioni.jenkins.client.exception.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

@RequiredArgsConstructor
public class JenkinsAuthenticationFilter implements ExchangeFilterFunction {

    private final JenkinsProperties jenkinsProperties;
    private final JenkinsAuthentication jenkinsAuthentication;
    private Crumb crumb;
    private HttpCookie crumbCookie;

    // key = Crumb, value = true if exception is ResourceNotFoundException false otherwise
    @Override
    public @NotNull Mono<ClientResponse> filter(@NotNull ClientRequest request, @NotNull ExchangeFunction next) {
        ClientRequest.Builder builder = ClientRequest.from(request);

        // Password and API Token are both Basic authentication (there is no Bearer authentication in Jenkins)
        if (jenkinsAuthentication.authType() == AuthenticationType.USERNAME_API_TOKEN || jenkinsAuthentication.authType() == AuthenticationType.USERNAME_PASSWORD) {
            final String authHeader = jenkinsAuthentication.authType().getAuthScheme() + " " + jenkinsAuthentication.authValue();
            builder.header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader);
        }

        //Anon and Password need the crumb and the cookie when POSTing
        //https://www.jenkins.io/doc/book/security/csrf-protection/#working-with-scripted-clients
        if (request.method().equals(HttpMethod.POST) && (jenkinsAuthentication.authType() == AuthenticationType.USERNAME_PASSWORD
            || jenkinsAuthentication.authType() == AuthenticationType.ANONYMOUS)) {
            final Crumb localCrumb = getCrumb();
            if (localCrumb != null && localCrumb.getValue() != null && localCrumb.getCrumbRequestField() != null) {
                builder.header(localCrumb.getCrumbRequestField(), localCrumb.getValue());
                builder.cookie(crumbCookie.getName(), crumbCookie.getValue());
            }
        }
        return next.exchange(builder.build());
    }

    private Crumb getCrumb() {
        try {
            if (crumb == null || crumbCookie == null) {
                crumb = WebClient.builder()
                    .build().get().uri(jenkinsProperties.getUrl() + "/crumbIssuer/api/json")
                    .header(HttpHeaders.AUTHORIZATION,
                        jenkinsAuthentication.authType().getAuthScheme() + " " + jenkinsAuthentication.authValue())
                    .exchangeToMono(clientResponse -> {
                        clientResponse.cookies()
                            .forEach((s, responseCookies) -> crumbCookie =
                                new HttpCookie(s, responseCookies.get(0).getValue()));
                        return clientResponse.bodyToMono(Crumb.class);
                    }).block();
            }
            return crumb;
        } catch (JenkinsAppException e) {
            return new Crumb();
        }
    }

}
