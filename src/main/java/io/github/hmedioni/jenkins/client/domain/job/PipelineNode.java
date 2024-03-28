package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;

import java.util.*;

@Data
public class PipelineNode {

    public String name;

    public String status;

    public long startTimeMillis;

    public long durationTimeMillis;

    public List<StageFlowNode> stageFlowNodes;


//    @SerializedNames({"name", "status", "startTimeMillis", "durationTimeMillis", "stageFlowNodes"})
//    public static PipelineNode create(String name, String status, long startTimeMillis, long durationTimeMillis, List<StageFlowNode> stageFlowNodes) {
//        return new AutoValue_PipelineNode(name, status, startTimeMillis, durationTimeMillis, stageFlowNodes);
//    }
}
