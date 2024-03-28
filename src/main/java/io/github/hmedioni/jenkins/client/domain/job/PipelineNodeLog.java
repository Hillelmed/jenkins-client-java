package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;


@Data
public class PipelineNodeLog {

    public String nodeId;

    public String nodeStatus;

    public int length;

    public boolean hasMore;

    public String text;

    public String consoleUrl;

//    @SerializedNames({"nodeId", "nodeStatus", "length", "hasMore", "text", "consoleUrl"})
//    public static PipelineNodeLog create(String nodeId, String nodeStatus, int length, boolean hasMore, String text, String consoleUrl) {
//        return new AutoValue_PipelineNodeLog(nodeId, nodeStatus, length, hasMore, text, consoleUrl);
//    }
}
