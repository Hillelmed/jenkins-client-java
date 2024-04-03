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

//    @SerializedNames({"artifacts", "actions", "building", "description", "displayName", "duration", "estimatedDuration",
//        "fullDisplayName", "id", "keepLog", "number", "queueId", "result", "timestamp", "url", "changeSets", "builtOn", "culprits"})
//    public static BuildInfo create(List<Artifact> artifacts, List<Action> actions, boolean building, String description, String displayName,
//                                   long duration, long estimatedDuration, String fullDisplayName, String id, boolean keepLog, int number,
//                                   int queueId, String result, long timestamp, String url, List<ChangeSetList> changeSets, String builtOn, List<Culprit> culprits) {
//        return new AutoValue_BuildInfo(
//            artifacts != null ? ImmutableList.copyOf(artifacts) : ImmutableList.<Artifact>of,
//            actions != null ? ImmutableList.copyOf(actions) : ImmutableList.<Action>of,
//            building, description, displayName, duration, estimatedDuration, fullDisplayName,
//            id, keepLog, number, queueId, result, timestamp, url,
//            changeSets != null ? ImmutableList.copyOf(changeSets) : ImmutableList.<ChangeSetList>of,
//            builtOn,
//            culprits != null ? ImmutableList.copyOf(culprits) : ImmutableList.<Culprit>of);
//    }
}
