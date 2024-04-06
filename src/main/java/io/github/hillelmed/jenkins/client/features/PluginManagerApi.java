package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.domain.plugins.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


//@RequestFilters(JenkinsAuthenticationFilter.class)
//@Consumes(MediaType.APPLICATION_JSON)
//@Path("/pluginManager")
@HttpExchange(value = "/pluginManager", accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface PluginManagerApi {

    @GetExchange("/api/json")
    PluginsWrapper plugins(@RequestParam("depth") Integer depth,
                           @RequestParam("tree") String tree);

    @GetExchange("/api/json")
    PluginsWrapper plugins(@RequestParam("depth") Integer depth);

    // @Named("pluginManager:install-necessary-plugins")
    @PostExchange(value = "/installNecessaryPlugins", contentType = MediaType.APPLICATION_XML_VALUE)
    ResponseEntity<Void> installNecessaryPlugins(@RequestBody String pluginID);
}
