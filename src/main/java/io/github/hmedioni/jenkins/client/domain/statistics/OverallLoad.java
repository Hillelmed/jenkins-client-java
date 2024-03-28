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

package io.github.hmedioni.jenkins.client.domain.statistics;

import lombok.*;
import org.springframework.lang.*;

import java.util.*;

@Data
public class OverallLoad {

    @Nullable
    public Map<String, String> availableExecutors;

    @Nullable
    public Map<String, String> busyExecutors;

    @Nullable
    public Map<String, String> connectingExecutors;

    @Nullable
    public Map<String, String> definedExecutors;

    @Nullable
    public Map<String, String> idleExecutors;

    @Nullable
    public Map<String, String> onlineExecutors;

    @Nullable
    public Map<String, String> queueLength;

    @Nullable
    public Map<String, String> totalExecutors;

    @Nullable
    public Map<String, String> totalQueueLength;


//    @SerializedNames({"availableExecutors", "busyExecutors", "connectingExecutors", "definedExecutors", "idleExecutors",
//        "onlineExecutors", "queueLength", "totalExecutors", "totalQueueLength"})
//    public static OverallLoad create(Map<String, String> availableExecutors, Map<String, String> busyExecutors,
//                                     Map<String, String> connectingExecutors, Map<String, String> definedExecutors,
//                                     Map<String, String> idleExecutors, Map<String, String> onlineExecutors, Map<String, String> queueLength,
//                                     Map<String, String> totalExecutors, Map<String, String> totalQueueLength) {
//        return new AutoValue_OverallLoad(availableExecutors, busyExecutors,
//            connectingExecutors, definedExecutors,
//            idleExecutors, onlineExecutors,
//            queueLength, totalExecutors,
//            totalQueueLength);
//    }
}
