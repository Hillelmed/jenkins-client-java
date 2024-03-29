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
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;


@RequiredArgsConstructor
public class JenkinsNoCrumbAuthenticationFilter implements ExchangeFilterFunction {

    private final JenkinsAuthentication creds;


    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        if (creds.authType() == AuthenticationType.ANONYMOUS) {
            return next.exchange(request);
        } else {
            final String authHeader = creds.authType().getAuthScheme() + " " + creds.authValue();
            final ClientRequest newRequest = ClientRequest.from(request)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .build();
            return next.exchange(newRequest);
        }
    }
}
