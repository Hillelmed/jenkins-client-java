package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.domain.queue.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

// @RequestFilters(JenkinsAuthenticationFilter.class)
// @Consumes(MediaType.APPLICATION_JSON)
@HttpExchange("/queue")
public interface QueueApi {

    // @Named("queue:queue")
    @GetExchange("/api/json")
//    @SelectJson("items")
    ResponseEntity<QueueItemsArray> queue();

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
    ResponseEntity<Void> cancel(@RequestParam(name = "id") int id);
}
