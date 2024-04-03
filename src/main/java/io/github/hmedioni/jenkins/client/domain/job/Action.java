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

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class Action {

    private List<Cause> causes;

    private List<Parameter> parameters;


    private String text;


    private String iconPath;


    @JsonProperty("_class")
    private String clazz;

//    @SerializedNames({"causes", "parameters", "text", "iconPath", "_class"})
//    public static Action create(final List<Cause> causes, final List<Parameter> parameters, final String text, final String iconPath, final String _class) {
//        return new AutoValue_Action(
//            causes != null ? ImmutableList.copyOf(causes) : ImmutableList.<Cause>of,
//            parameters != null ? ImmutableList.copyOf(parameters) : ImmutableList.<Parameter>of,
//            text, iconPath, _class
//        );
//    }
}

