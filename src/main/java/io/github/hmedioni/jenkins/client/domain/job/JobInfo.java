package io.github.hmedioni.jenkins.client.domain.job;


import io.github.hmedioni.jenkins.client.domain.queue.*;
import lombok.*;

import java.util.*;

@Data
public class JobInfo {


    public String description;


    public String displayName;


    public String displayNameOrNull;

    public String name;

    public String url;

    public boolean buildable;

    public List<BuildInfo> builds;


    public String color;


    public BuildInfo firstBuild;

    public boolean inQueue;

    public boolean keepDependencies;


    public BuildInfo lastBuild;


    public BuildInfo lastCompleteBuild;


    public BuildInfo lastFailedBuild;


    public BuildInfo lastStableBuild;


    public BuildInfo lastSuccessfulBuild;


    public BuildInfo lastUnstableBuild;


    public BuildInfo lastUnsuccessfulBuild;

    public int nextBuildNumber;


    public QueueItem queueItem;

    public boolean concurrentBuild;


//    @SerializedNames({"description", "displayName", "displayNameOrNull", "name", "url", "buildable", "builds", "color",
//        "firstBuild", "inQueue", "keepDependencies", "lastBuild", "lastCompleteBuild", "lastFailedBuild",
//        "lastStableBuild", "lastSuccessfulBuild", "lastUnstableBuild", "lastUnsuccessfulBuild", "nextBuildNumber",
//        "queueItem", "concurrentBuild"})
//    public static JobInfo create(String description, String displayName, String displayNameOrNull, String name,
//                                 String url, boolean buildable, List<BuildInfo> builds, String color, BuildInfo firstBuild, boolean inQueue,
//                                 boolean keepDependencies, BuildInfo lastBuild, BuildInfo lastCompleteBuild, BuildInfo lastFailedBuild,
//                                 BuildInfo lastStableBuild, BuildInfo lastSuccessfulBuild, BuildInfo lastUnstableBuild, BuildInfo lastUnsuccessfulBuild,
//                                 int nextBuildNumber, QueueItem queueItem, boolean concurrentBuild) {
//        return new AutoValue_JobInfo(description, displayName, displayNameOrNull, name, url, buildable,
//            builds != null ? ImmutableList.copyOf(builds) : ImmutableList.<BuildInfo>of, color, firstBuild, inQueue,
//            keepDependencies, lastBuild, lastCompleteBuild, lastFailedBuild, lastStableBuild, lastSuccessfulBuild,
//            lastUnstableBuild, lastUnsuccessfulBuild, nextBuildNumber, queueItem, concurrentBuild);
//    }
}
