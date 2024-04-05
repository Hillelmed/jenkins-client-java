package io.github.hmedioni.jenkins.client.features;

import io.github.hmedioni.jenkins.client.domain.system.*;
import org.springframework.http.*;
import org.springframework.web.service.annotation.*;


// @RequestFilters(JenkinsAuthenticationFilter.class)
// @Consumes(MediaType.APPLICATION_JSON)
@HttpExchange
public interface SystemApi {

    @GetExchange("/api/json/systemInfo")
    ResponseEntity<SystemInfo> systemInfo();

    @PostExchange("/restart")
    ResponseEntity<Void> restart();

    @PostExchange("/safeRestart")
    ResponseEntity<Void> safeRestart();

    @PostExchange("/exit")
    ResponseEntity<Void> exit();

    @PostExchange("/safeExit")
    ResponseEntity<Void> safeExit();


    @PostExchange("/quietDown")
    ResponseEntity<Void> quietDown();

    @PostExchange("/cancelQuietDown")
    ResponseEntity<Void> cancelQuietDown();


}
