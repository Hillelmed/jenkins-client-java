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

@Data
public class Plugin {


    public Boolean active;


    public String backupVersion;


    public Boolean bundled;


    public Boolean deleted;


    public Boolean downgradable;


    public Boolean enabled;


    public Boolean hasUpdate;


    public String longName;


    public Boolean pinned;


    public String requiredCoreVersion;


    public String shortName;


    public String supportsDynamicLoad;


    public String url;


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
