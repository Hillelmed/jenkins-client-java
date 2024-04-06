package io.github.hmedioni.jenkins.client.filters;

import io.github.hmedioni.jenkins.client.*;
import lombok.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.net.*;

import static io.github.hmedioni.jenkins.client.JenkinsConstants.*;

@RequiredArgsConstructor
public class JenkinsUserInjectionFilter implements ExchangeFilterFunction {

    private static final String USER_PLACE_HOLDER = "%7B" + USER_IN_USER_API + "%7D";
    private final JenkinsAuthentication creds;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        ClientRequest.Builder builder = ClientRequest.from(request);
        builder.url(URI.create(request.url().toString().replaceAll(USER_PLACE_HOLDER, creds.getEncodedCred())));
        return next.exchange(builder.build());
    }
}
