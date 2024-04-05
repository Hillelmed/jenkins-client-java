package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;

import java.util.*;

@Data
public class StageFlowNode {

    public String name;

    public String status;

    public long startTimeMillis;

    public long durationTimeMillis;

    public List<Long> parentNodes;


//    @SerializedNames({"name", "status", "startTimeMillis", "durationTimeMillis", "parentNodes"})
//    public static StageFlowNode create(String name, String status, long startTimeMillis, long durationTimeMillis, List<Long> parentNodes) {
//        return new AutoValue_StageFlowNode(name, status, startTimeMillis, durationTimeMillis, parentNodes);
//    }
}
