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

import static io.github.hmedioni.jenkins.client.JenkinsConstants.JENKINS_COOKIES_JSESSIONID;

@RequiredArgsConstructor
public class JenkinsAuthenticationFilter implements ExchangeFilterFunction {

    private final JenkinsProperties jenkinsProperties;
    private final JenkinsAuthentication jenkinsAuthentication;
    private Crumb crumb;
    private HttpCookie crumbCookie;

    // key = Crumb, value = true if exception is ResourceNotFoundException false otherwise
    @Override
    public @NotNull reactor.core.publisher.Mono<ClientResponse> filter(@NotNull ClientRequest request, @NotNull ExchangeFunction next) {
        ClientRequest.Builder builder = ClientRequest.from(request);

        // Password and API Token are both Basic authentication (there is no Bearer authentication in Jenkins)
        if (jenkinsAuthentication.getAuthType() == AuthenticationType.USERNAME_API_TOKEN || jenkinsAuthentication.getAuthType() == AuthenticationType.USERNAME_PASSWORD) {
            final String authHeader = jenkinsAuthentication.getAuthType().getAuthScheme() + " " + jenkinsAuthentication.getEncodedCred();
            builder.header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader);
        }

        //Anon and Password need the crumb and the cookie when POSTing
        //https://www.jenkins.io/doc/book/security/csrf-protection/#working-with-scripted-clients
        if (request.method().equals(HttpMethod.POST) && (jenkinsAuthentication.getAuthType() == AuthenticationType.USERNAME_PASSWORD
            || jenkinsAuthentication.getAuthType() == AuthenticationType.ANONYMOUS)) {
            try {
                final Crumb localCrumb = getCrumb();
                if (localCrumb != null && localCrumb.getValue() != null && localCrumb.getCrumbRequestField() != null) {
                    builder.header(localCrumb.getCrumbRequestField(), localCrumb.getValue());
                    builder.cookie(crumbCookie.getName(), crumbCookie.getValue());
                }
            } catch (JenkinsAppException e) {
                throw new CrumbMissing(e.getMessage(), e.errors(), e.code());
            }
        }
        return next.exchange(builder.build());
    }

    private Crumb getCrumb() {
        if (crumb == null || crumbCookie == null) {
            crumb = WebClient.builder()
                .build().get().uri(jenkinsProperties.getUrl() + "/crumbIssuer/api/json")
                .header(HttpHeaders.AUTHORIZATION,
                    jenkinsAuthentication.getAuthType().getAuthScheme() + " " + jenkinsAuthentication.getEncodedCred())
                .exchangeToMono(clientResponse -> {
                    clientResponse.cookies()
                        .forEach((s, responseCookies) -> {
                            if (responseCookies.get(0).getName().contains(JENKINS_COOKIES_JSESSIONID)) {
                                crumbCookie = new HttpCookie(s, responseCookies.get(0).getValue());
                            }
                        });
                    return clientResponse.bodyToMono(Crumb.class);
                }).block();
        }
        return crumb;
    }

}
