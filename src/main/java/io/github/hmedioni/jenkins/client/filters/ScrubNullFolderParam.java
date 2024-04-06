package io.github.hmedioni.jenkins.client.filters;

import lombok.*;
import org.springframework.web.reactive.function.client.*;

import java.net.*;


@RequiredArgsConstructor
public class ScrubNullFolderParam implements ExchangeFilterFunction {

    private static final String SCRUB_NULL_PARAM = "%7B.+?%7D";
    private static final String FORWARD_SLASH = "/";
    private static final String DOUBLE_FORWARD_SLASH = "\\" + FORWARD_SLASH + "\\" + FORWARD_SLASH;
    private static final String EMPTY_STRING = "";
    private static final char FORWARD_SLASH_CHAR = '/';


    @Override
    public reactor.core.publisher.Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        String oldPath = request.url().getPath();
        String requestPath = request.url().getPath()
            .replace(SCRUB_NULL_PARAM, EMPTY_STRING)
            .replace(DOUBLE_FORWARD_SLASH, FORWARD_SLASH);
        if (!requestPath.isEmpty()) {
            if (requestPath.charAt(requestPath.length() - 1) == FORWARD_SLASH_CHAR) {
                requestPath = requestPath.substring(0, requestPath.length() - 1);
            }
            String newUrl = request.url().toString().replaceAll(oldPath, requestPath);
            ClientRequest filteredRequest = ClientRequest.from(request)
                .url(URI.create(newUrl))
                .build();
            return next.exchange(filteredRequest);
        } else {
            return next.exchange(request);
        }
    }

}
