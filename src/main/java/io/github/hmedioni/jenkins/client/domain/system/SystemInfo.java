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

package io.github.hmedioni.jenkins.client.domain.system;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class SystemInfo {

    public String _class;
    public ArrayList<AssignedLabel> assignedLabels;
    public String mode;
    public String nodeDescription;
    public String nodeName;
    public int numExecutors;
    public String description;
    public ArrayList<Object> jobs;
    public OverallLoad overallLoad;
    public PrimaryView primaryView;
    public Object quietDownReason;
    public boolean quietingDown;
    public int slaveAgentPort;
    public UnlabeledLoad unlabeledLoad;
    public Object url;
    public boolean useCrumbs;
    public boolean useSecurity;
    public ArrayList<View> views;

    public static class AssignedLabel {
        public String name;
    }

    public static class OverallLoad {
    }

    public static class PrimaryView {
        public String _class;
        public String name;
        public String url;
    }

    public static class UnlabeledLoad {
        public String _class;
    }

    public static class View {
        public String _class;
        public String name;
        public String url;
    }

}
