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

package io.github.hmedioni.jenkins.client.domain.plugins;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;

@Data
public class Plugins {

    @Nullable
    public String clazz;

    public List<Plugin> plugins;

//    @SerializedNames({"_class", "plugins", "errors"})
//    public static Plugins create(final String clazz,
//                                 final List<Plugin> plugins,
//                                 final List<Error> errors) {
//        return new AutoValue_Plugins(JenkinsUtils.nullToEmpty(errors),
//            clazz,
//            JenkinsUtils.nullToEmpty(plugins));
//    }
}
