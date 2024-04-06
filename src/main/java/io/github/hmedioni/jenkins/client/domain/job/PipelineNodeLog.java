package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;


@Data
@NoArgsConstructor
public class PipelineNodeLog {

    private String nodeId;

    private String nodeStatus;

    private int length;

    private boolean hasMore;

    private String text;

    private String consoleUrl;


}
