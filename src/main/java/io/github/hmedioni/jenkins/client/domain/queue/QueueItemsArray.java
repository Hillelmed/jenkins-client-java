package io.github.hmedioni.jenkins.client.domain.queue;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class QueueItemsArray {

    @JsonProperty("_class")
    private String clazz;
    private List<Object> discoverableItems;
    private List<QueueItem> items;

}
