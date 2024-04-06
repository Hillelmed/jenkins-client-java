package io.github.hmedioni.jenkins.client.handlers;

import com.fasterxml.jackson.databind.*;
import io.github.hmedioni.jenkins.client.exception.*;
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
//        if (response.getPayload() != null) {
//            try {
//                return Strings2.toStringAndClose(response.getPayload().openStream());
//            } catch (IOException e) {
//                throw Throwables.propagate(e);
//            }
//        } else {
//            final String errorMessage = response.getFirstHeaderOrNull("X-Error");
//            return command.getCurrentRequest().getRequestLine() +
//                " -> " +
//                response.getStatusLine() +
//                " -> " +
//                (errorMessage != null ? errorMessage : "");
//        }
    }


//    @Override
//    public void handleError(final HttpCommand command, final HttpResponse response) {
//
//        Exception exception = null;
//        try {
//            final String message = parseMessage(command, response);
//
//            switch (response.getStatusCode()) {
//                case 400:
//                    if (command.getCurrentRequest().getMethod().equals("POST")) {
//                        if (command.getCurrentRequest().getRequestLine().contains("/createItem")) {
//                            if (message.contains("A job already exists with the name")) {
//                                exception = new ResourceAlreadyExistsException(message);
//                                break;
//                            }
//                        }
//                    }
//                    exception = new IllegalArgumentException(message);
//                    break;
//                case 401:
//                    exception = new AuthorizationException(message);
//                    break;
//                case 403:
//                    exception = new ForbiddenException(message);
//                    break;
//                case 404:
//                    // When Jenkins replies to term or kill with a redirect to a non-existent URL
//                    // we want to return a custom error message and avoid an exception in the user code.
//                    if (command.getCurrentRequest().getMethod().equals("POST")) {
//                        final String path = command.getCurrentRequest().getEndpoint().getPath();
//                        if (path.endsWith("/term/")) {
//                            exception = new RedirectTo404Exception("The term operation does not exist for " + command.getCurrentRequest().getEndpoint().toString() + ", try stop instead.");
//                            break;
//                        } else if (path.endsWith("/kill/")) {
//                            exception = new RedirectTo404Exception("The kill operation does not exist for " + command.getCurrentRequest().getEndpoint().toString() + ", try stop instead.");
//                            break;
//                        }
//                    }
//                    exception = new ResourceNotFoundException(message);
//                    break;
//                case 405:
//                    exception = new MethodNotAllowedException(message);
//                    break;
//                case 409:
//                    exception = new ResourceAlreadyExistsException(message);
//                    break;
//                case 415:
//                    exception = new UnsupportedMediaTypeException(message);
//                    break;
//                default:
//                    exception = new HttpResponseException(command, response);
//            }
//        } catch (Exception e) {
//            exception = new HttpResponseException(command, response, e);
//        } finally {
//            closeQuietly(response.getPayload());
//            command.setException(exception);
//        }
//    }

}
