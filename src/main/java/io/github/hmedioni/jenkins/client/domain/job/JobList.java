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
public class JobList {


    public String clazz;

    public List<Job> jobs;


    public String url;


//    @SerializedNames({"_class", "jobs", "url"})
//    public static JobList create(final String clazz, final List<Job> jobs, final String url) {
//        return new AutoValue_JobList(clazz, jobs, url);
//    }
}
