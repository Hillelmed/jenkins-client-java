package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.domain.crumb.*;
import org.springframework.http.*;
import org.springframework.web.service.annotation.*;

//@RequestFilters(JenkinsNoCrumbAuthenticationFilter.class)
@HttpExchange(url = "/crumbIssuer/api/json", accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface CrumbIssuerApi {

    //    // @Named("crumb-issuer:crumb")
//    @Fallback(JenkinsFallbacks.CrumbOnError.class)
//    @ResponseParser(CrumbParser.class)
//    @QueryParams(keys = {"xpath"}, values = {"concat(//crumbRequestField,\":\",//crumb)"})
//    @Consumes(MediaType.TEXT_PLAIN)
//    @GetExchange
    @GetExchange
    ResponseEntity<Crumb> crumb();
}
