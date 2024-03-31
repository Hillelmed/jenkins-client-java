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
//import okhttp3.mockwebserver.*;
//import org.testng.annotations.*;
//
//import static org.testng.Assert.*;
//
///**
// * Mock tests for the {@link ConfigurationAsCodeApi} class.
// */
//@Test(groups = "unit", testName = "ConfigurationAsCodeApiMockTest")
//public class ConfigurationAsCodeApiMockTest extends BaseJenkinsMockTest {
//
//    public void testCascCheck() throws Exception {
//        try (MockWebServer server = mockWebServer();
//             JenkinsApi jenkinsApi = api(server.url("/").url())) {
//            server.enqueue(new MockResponse().setResponseCode(200));
//
//            ConfigurationAsCodeApi api = jenkinsApi.configurationAsCodeApi();
//            ResponseEntity<Void> requestStatus = api.check("random");
//            System.out.println(requestStatus.errors());
//            assertNotNull(requestStatus);
//            assertTrue(requestStatus.getValues());
//            assertEquals(requestStatus.errors().size(), 0);
//        }
//    }
//
//    public void testCascApply() throws Exception {
//        try (MockWebServer server = mockWebServer();
//             JenkinsApi jenkinsApi = api(server.url("/").url())) {
//            server.enqueue(new MockResponse().setResponseCode(200));
//
//            ConfigurationAsCodeApi api = jenkinsApi.configurationAsCodeApi();
//            ResponseEntity<Void> requestStatus = api.apply("random");
//            assertNotNull(requestStatus);
//            assertTrue(requestStatus.getValues());
//            assertEquals(requestStatus.errors().size(), 0);
//        }
//    }
//
//    public void testBadCascCheck() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(500));
//        JenkinsApi jenkinsApi = api(server.url("/").url());
//        ConfigurationAsCodeApi api = jenkinsApi.configurationAsCodeApi();
//        try {
//            ResponseEntity<Void> requestStatus = api.check("random");
//            assertNotNull(requestStatus);
//            assertFalse(requestStatus.getValues());
//            assertEquals(requestStatus.errors().size(), 1);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testBadCascApply() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(500));
//        JenkinsApi jenkinsApi = api(server.url("/").url());
//        ConfigurationAsCodeApi api = jenkinsApi.configurationAsCodeApi();
//        try {
//            ResponseEntity<Void> requestStatus = api.apply("random");
//            assertNotNull(requestStatus);
//            assertFalse(requestStatus.getValues());
//            assertEquals(requestStatus.errors().size(), 1);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//}
