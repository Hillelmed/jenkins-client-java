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

import io.github.hmedioni.jenkins.client.domain.queue.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.*;

// @RequestFilters(JenkinsAuthenticationFilter.class)
// @Consumes(MediaType.APPLICATION_JSON)
@HttpExchange("/queue")
public interface QueueApi {

    // @Named("queue:queue")
    @GetExchange("/api/json")
//    @SelectJson("items")
    List<QueueItem> queue();

    /**
     * Get a specific queue item.
     * <p>
     * Queue items are builds that have been scheduled to run, but are waiting for a slot.
     * You can poll the queueItem that corresponds to a build to detect whether the build is still pending or is executing.
     *
     * @param queueId The queue id value as returned by the JobsApi build or buildWithParameters methods.
     * @return The queue item corresponding to the queue id.
     */
    // @Named("queue:item")
    @GetExchange("/item/{queueId}/api/json")
    QueueItem queueItem(@PathVariable("queueId") int queueId);

    /**
     * Cancel a queue item before it gets built.
     *
     * @param id The queue id value of the queue item to cancel.
     *           This is the value is returned by the JobsApi build or buildWithParameters methods.
     * @return Always returns true due to JENKINS-21311.
     */
    // @Named("queue:cancel")
    @PostExchange("/cancelItem")
    // @Fallback(JenkinsFallbacks.JENKINS_21311.class)
    //@ResponseParser(ResponseEntity<Void>Parser.class)
    //@FormParam("id")
    ResponseEntity<Void> cancel(@RequestBody int id);
}
