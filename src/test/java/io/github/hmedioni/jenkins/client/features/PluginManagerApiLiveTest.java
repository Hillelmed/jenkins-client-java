///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package io.github.hmedioni.jenkins.client.features;
//
//import io.github.hmedioni.jenkins.client.*;
//import io.github.hmedioni.jenkins.client.domain.common.*;
//import io.github.hmedioni.jenkins.client.domain.plugins.*;
//import org.testng.annotations.*;
//
//import static org.testng.Assert.*;
//
//@Test(groups = "live", testName = "PluginManagerApiLiveTest", singleThreaded = true)
//public class PluginManagerApiLiveTest extends BaseJenkinsApiLiveTest {
//
//    @Test
//    public void testGetPlugins() {
//        final Plugins plugins = api().plugins(3, null);
//        assertNotNull(plugins);
//        assertTrue(plugins.errors().isEmpty());
//        assertFalse(plugins.plugins().isEmpty());
//        assertNotNull(plugins.plugins().get(0).shortName());
//    }
//
//    @Test
//    public void testInstallNecessaryPlugins() {
//        final ResponseEntity<Void> status = api().installNecessaryPlugins("artifactory@2.2.1");
//        assertNotNull(status);
//        assertTrue(status.getValues());
//        assertTrue(status.errors().isEmpty());
//    }
//
//    private PluginManagerApi api() {
//        return api.pluginManagerApi();
//    }
//}
