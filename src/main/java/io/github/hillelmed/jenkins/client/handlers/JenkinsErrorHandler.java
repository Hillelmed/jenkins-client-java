package io.github.hillelmed.jenkins.client.handlers;

import com.fasterxml.jackson.databind.*;
import io.github.hillelmed.jenkins.client.exception.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.*;

import java.util.*;


/**
 * Handle errors and propagate exception
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JenkinsErrorHandler {

    private static final String ERROR_HEADER = "X-Error";
    private static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public static ExchangeFilterFunction handler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            HttpStatusCode statusCode = clientResponse.statusCode();
            if (statusCode.is5xxServerError() || statusCode.is4xxClientError()) {
                return clientResponse.bodyToMono(String.class)
                    .defaultIfEmpty("{}")
                    .flatMap(errorBody -> reactor.core.publisher.Mono.error(new JenkinsAppException(errorBody, getErrors(errorBody, clientResponse), statusCode)));
            } else {
                return reactor.core.publisher.Mono.just(clientResponse);
            }
        });
    }

    private static List<JenkinsError> getErrors(String errorBody, ClientResponse clientResponse) {
        List<JenkinsError> jenkinsErrors = new ArrayList<>();
        HttpHeaders headers = clientResponse.headers().asHttpHeaders();
        if (headers.containsKey(ERROR_HEADER)) {
            JenkinsError error = new JenkinsError();
            error.setMessage(headers.getFirst(ERROR_HEADER));
            jenkinsErrors.add(error);
        }
        jenkinsErrors.addAll(parseMessage(errorBody));
        return jenkinsErrors;
    }

    private static List<JenkinsError> parseMessage(String errorBody) {
        try {
            return List.of(mapper.readValue(errorBody, JenkinsError.class));
        } catch (Exception e) {
            JenkinsError error = new JenkinsError();
            error.setMessage(errorBody);
            return List.of(error);
        }
    }

}
