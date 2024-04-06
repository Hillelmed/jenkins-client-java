package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.domain.statistics.*;
import org.springframework.http.*;
import org.springframework.web.service.annotation.*;


// @RequestFilters(JenkinsAuthenticationFilter.class)
// @Consumes(MediaType.APPLICATION_JSON)
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface StatisticsApi {

    // @Named("statistics:overall-load")
    @GetExchange("/overallLoad/api/json")
    OverallLoad overallLoad();
}
