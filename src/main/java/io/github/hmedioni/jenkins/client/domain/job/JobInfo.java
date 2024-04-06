package io.github.hmedioni.jenkins.client.domain.job;


import io.github.hmedioni.jenkins.client.domain.queue.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class JobInfo {


    private String description;


    private String displayName;


    private String displayNameOrNull;

    private String name;

    private String url;

    private boolean buildable;

    private List<BuildInfo> builds;


    private String color;


    private BuildInfo firstBuild;

    private boolean inQueue;

    private boolean keepDependencies;


    private BuildInfo lastBuild;


    private BuildInfo lastCompleteBuild;


    private BuildInfo lastFailedBuild;


    private BuildInfo lastStableBuild;


    private BuildInfo lastSuccessfulBuild;


    private BuildInfo lastUnstableBuild;


    private BuildInfo lastUnsuccessfulBuild;

    private int nextBuildNumber;


    private QueueItem queueItem;

    private boolean concurrentBuild;


}
