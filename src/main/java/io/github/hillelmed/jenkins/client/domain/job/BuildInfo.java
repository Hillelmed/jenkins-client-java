package io.github.hillelmed.jenkins.client.domain.job;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class BuildInfo {

    private List<Artifact> artifacts;

    private List<Action> actions;

    private boolean building;


    private String description;


    private String displayName;

    private long duration;

    private long estimatedDuration;


    private String fullDisplayName;


    private String id;

    private boolean keepLog;

    private int number;

    private int queueId;


    private String result;

    private long timestamp;


    private String url;

    private List<ChangeSetList> changeSets;


    private String builtOn;

    private List<Culprit> culprits;


}
