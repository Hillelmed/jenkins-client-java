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
//
//package io.github.hmedioni.jenkins.client.filters;
//
//
//import io.github.hmedioni.jenkins.client.*;
//import io.github.hmedioni.jenkins.client.auth.*;
//import okhttp3.mockwebserver.*;
//import org.springframework.http.*;
//import org.testng.annotations.*;
//
//
//
//import static org.testng.Assert.*;
//
//public class JenkinsAuthenticationFilterMockTest extends BaseJenkinsMockTest {
//
//    @Test
//    public void testAnonymousNeedsCrumb() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        final String value = "04a1109fc2db171362c966ebe9fc87f0";
//        server.enqueue(new MockResponse().setBody("Jenkins-Crumb:" + value).setResponseCode(200));
//        JenkinsApi jenkinsApi = anonymousAuthApi("http://localhost:" + server.getPort());
//
//        JenkinsAuthentication creds = creds(AuthenticationType.Anonymous, null);
//        JenkinsAuthenticationFilter filter = new JenkinsAuthenticationFilter(creds, jenkinsApi);
//        HttpRequest httpRequest = HttpRequest.builder().endpoint("http://localhost:" + server.getPort().toString()).method("POST").build();
//        try {
//            httpRequest = filter.filter(httpRequest);
//            assertEquals(httpRequest.getEndpoint().toString(), "http://localhost:" + server.getPort().toString());
//            assertSentAccept(server, "GET", "/crumbIssuer/api/xml?xpath=concat%28//crumbRequestField,%22%3A%22,//crumb%29", MediaType.TEXT_PLAIN);
//            Multimap<String, String> headers = httpRequest.getHeaders();
//            assertEquals(headers.size(), 2);
//            assertTrue(headers.containsEntry("Jenkins-Crumb", value));
//            assertTrue(headers.containsEntry("Cookie", ""));
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    @Test
//    public void testUsernamePasswordNeedsCrumb() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        final String value = "04a1109fc2db171362c966ebe9fc87f0";
//        final String usernamePassword = "random_user:random_password";
//        server.enqueue(new MockResponse().setBody("Jenkins-Crumb:" + value).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort(), AuthenticationType.UsernamePassword, usernamePassword);
//
//        JenkinsAuthentication creds = creds(AuthenticationType.UsernamePassword, usernamePassword);
//        JenkinsAuthenticationFilter filter = new JenkinsAuthenticationFilter(creds, jenkinsApi);
//        HttpRequest httpRequest = HttpRequest.builder().endpoint("http://localhost:" + server.getPort().toString()).method("POST").build();
//        try {
//            httpRequest = filter.filter(httpRequest);
//            assertEquals(httpRequest.getEndpoint().toString(), "http://localhost:" + server.getPort().toString());
//            assertSentAccept(server, "GET", "/crumbIssuer/api/xml?xpath=concat%28//crumbRequestField,%22%3A%22,//crumb%29", MediaType.TEXT_PLAIN);
//            Multimap<String, String> headers = httpRequest.getHeaders();
//            assertEquals(headers.size(), 3);
//            assertTrue(headers.containsEntry("Jenkins-Crumb", value));
//            assertTrue(headers.containsEntry("Authorization", creds.authType().getAuthScheme() + " " + creds.authValue()));
//            assertTrue(headers.containsEntry("Cookie", ""));
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    @Test
//    public void testUsernameApiTokenNeedsNoCrumb() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//
//        JenkinsAuthentication creds = creds(AuthenticationType.UsernameApiToken, "random_user:random_token");
//        JenkinsAuthenticationFilter filter = new JenkinsAuthenticationFilter(creds, jenkinsApi);
//        HttpRequest httpRequest = HttpRequest.builder().endpoint("http://localhost:" + server.getPort().toString()).method("POST").build();
//        try {
//            httpRequest = filter.filter(httpRequest);
//            assertEquals(httpRequest.getEndpoint().toString(), "http://localhost:" + server.getPort().toString());
//            Multimap<String, String> headers = httpRequest.getHeaders();
//            assertEquals(headers.size(), 1);
//            assertTrue(headers.containsEntry("Authorization", creds.authType().getAuthScheme() + " " + creds.authValue()));
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    @Test
//    public void getMethodNeedsNoCrumb() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//
//        JenkinsAuthentication creds = creds(AuthenticationType.UsernameApiToken, "random_user:random_token");
//        JenkinsAuthenticationFilter filter = new JenkinsAuthenticationFilter(creds, jenkinsApi);
//        HttpRequest httpRequest = HttpRequest.builder().endpoint("http://localhost:" + server.getPort().toString()).method("GET").build();
//        try {
//            httpRequest = filter.filter(httpRequest);
//            assertEquals(httpRequest.getEndpoint().toString(), "http://localhost:" + server.getPort().toString());
//            Multimap<String, String> headers = httpRequest.getHeaders();
//            assertEquals(headers.size(), 1);
//            assertTrue(headers.containsEntry("Authorization", creds.authType().getAuthScheme() + " " + creds.authValue()));
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//}
