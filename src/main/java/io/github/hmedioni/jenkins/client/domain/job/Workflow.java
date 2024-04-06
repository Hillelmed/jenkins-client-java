package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class Workflow {

    private String name;

    private String status;

    private long startTimeMillis;

    private long durationTimeMillis;

    private List<Stage> stages;


}
