package io.github.hmedioni.jenkins.client.domain.job;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class ChangeSet {

    private List<String> affectedPaths;

    private String commitId;

    private long timestamp;

    private Culprit author;


    private String authorEmail;


    private String comment;


}
