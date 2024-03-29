/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hmedioni.jenkins.client.features;

import io.github.hmedioni.jenkins.client.domain.common.*;
import io.github.hmedioni.jenkins.client.domain.plugins.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


//@RequestFilters(JenkinsAuthenticationFilter.class)
//@Consumes(MediaType.APPLICATION_JSON)
//@Path("/pluginManager")
@HttpExchange(value = "/pluginManager", accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface PluginManagerApi {

    // @Named("pluginManager:plugins")
    // @Fallback(JenkinsFallbacks.PluginsOnError.class)
    @GetExchange("/api/json")

    Plugins plugins(@Nullable @RequestParam("depth") Integer depth,
                    @Nullable @RequestParam("tree") String tree);

    // @Named("pluginManager:install-necessary-plugins")
    @PostExchange("/installNecessaryPlugins")
    // @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    //@ResponseParser(RequestStatusParser.class)
    // @Produces(MediaType.APPLICATION_XML)
    // @Payload("<jenkins><install plugin=\"{pluginID}\"/></jenkins>")
    ResponseEntity<Void> installNecessaryPlugins(@RequestBody String pluginID);
}
