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

@Data
public class Plugin {

    @Nullable
    public Boolean active;

    @Nullable
    public String backupVersion;

    @Nullable
    public Boolean bundled;

    @Nullable
    public Boolean deleted;

    @Nullable
    public Boolean downgradable;

    @Nullable
    public Boolean enabled;

    @Nullable
    public Boolean hasUpdate;

    @Nullable
    public String longName;

    @Nullable
    public Boolean pinned;

    @Nullable
    public String requiredCoreVersion;

    @Nullable
    public String shortName;

    @Nullable
    public String supportsDynamicLoad;

    @Nullable
    public String url;

    @Nullable
    public String version;


//    @SerializedNames({"active", "backupVersion", "bundled",
//        "deleted", "downgradable", "enabled",
//        "hasUpdate", "longName", "pinned",
//        "requiredCoreVersion", "shortName", "supportsDynamicLoad",
//        "url", "version"})
//    public static Plugin create(Boolean active, String backupVersion, Boolean bundled,
//                                Boolean deleted, Boolean downgradable, Boolean enabled,
//                                Boolean hasUpdate, String longName, Boolean pinned,
//                                String requiredCoreVersion, String shortName, String supportsDynamicLoad,
//                                String url, String version) {
//        return new AutoValue_Plugin(active, backupVersion, bundled,
//            deleted, downgradable, enabled,
//            hasUpdate, longName, pinned,
//            requiredCoreVersion, shortName, supportsDynamicLoad,
//            url, version);
//    }
}
