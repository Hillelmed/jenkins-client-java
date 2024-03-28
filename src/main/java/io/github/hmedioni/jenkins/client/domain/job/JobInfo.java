/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hmedioni.jenkins.client.domain.job;


import io.github.hmedioni.jenkins.client.domain.queue.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;

@Data
public class JobInfo {

    @Nullable
    public String description;

    @Nullable
    public String displayName;

    @Nullable
    public String displayNameOrNull;

    public String name;

    public String url;

    public boolean buildable;

    public List<BuildInfo> builds;

    @Nullable
    public String color;

    @Nullable
    public BuildInfo firstBuild;

    public boolean inQueue;

    public boolean keepDependencies;

    @Nullable
    public BuildInfo lastBuild;

    @Nullable
    public BuildInfo lastCompleteBuild;

    @Nullable
    public BuildInfo lastFailedBuild;

    @Nullable
    public BuildInfo lastStableBuild;

    @Nullable
    public BuildInfo lastSuccessfulBuild;

    @Nullable
    public BuildInfo lastUnstableBuild;

    @Nullable
    public BuildInfo lastUnsuccessfulBuild;

    public int nextBuildNumber;

    @Nullable
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
