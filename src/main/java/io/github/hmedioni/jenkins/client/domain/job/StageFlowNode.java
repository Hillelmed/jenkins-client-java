package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class StageFlowNode {

    private String name;

    private String status;

    private long startTimeMillis;

    private long durationTimeMillis;

    private List<Long> parentNodes;


}
