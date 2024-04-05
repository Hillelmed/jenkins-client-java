package io.github.hmedioni.jenkins.client.features;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


//@RequestFilters(JenkinsAuthenticationFilter.class)
@HttpExchange(url = "/configuration-as-code", accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface ConfigurationAsCodeApi {

    //    // @Named("casc:check")
//    @Path("/check")
//    @Fallback(JenkinsFallbacks.ResponseEntity<Void>OnError.class)
//    @ResponseParser(ResponseEntity<Void>Parser.class)
//    @Payload("{cascYml}")cascYml
//    @PostExchange
    @PostExchange("/check")
    ResponseEntity<Void> check(@RequestBody String cascYml);

    //    // @Named("casc:apply")
//    @Path("/apply")
//    @Fallback(JenkinsFallbacks.ResponseEntity<Void>OnError.class)
//    @ResponseParser(ResponseEntity<Void>Parser.class)
//    @Payload("{cascYml}")
//    @PostExchange
    @PostExchange("/apply")
    ResponseEntity<Void> apply(@RequestBody String cascYml);
}
