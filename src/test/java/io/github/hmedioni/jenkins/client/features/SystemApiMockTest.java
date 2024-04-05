//package io.github.hmedioni.jenkins.client.features;
//
//import io.github.hmedioni.jenkins.client.*;
//import io.github.hmedioni.jenkins.client.domain.common.*;
//import io.github.hmedioni.jenkins.client.domain.system.*;
//import okhttp3.mockwebserver.*;
//import org.springframework.http.*;
//import org.testng.annotations.*;
//
//
//
//import static org.testng.Assert.*;
//
///**
// * Mock tests for the {@link SystemApi} class.
// */
//@Test(groups = "unit", testName = "SystemApiMockTest")
//public class SystemApiMockTest extends BaseJenkinsMockTest {
//
//    public void testGetSystemInfo() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(
//            new MockResponse().setHeader("X-Hudson", "1.395").setHeader("X-Jenkins", JenkinsApiMetadata.BUILD_VERSION)
//                .setHeader("X-Jenkins-Session", "cc323b8d").setHeader("X-Hudson-CLI-Port", "50000")
//                .setHeader("X-Jenkins-CLI-Port", "50000").setHeader("X-Jenkins-CLI2-Port", "50000")
//                .setHeader("X-Instance-Identity", "fdsa").setHeader("X-SSH-Endpoint", "127.0.1.1:46126")
//                .setHeader("Server", "Jetty(winstone-2.9)").setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        SystemApi api = jenkinsApi.systemApi();
//        try {
//            final SystemInfo version = api.systemInfo();
//            assertNotNull(version);
//            assertTrue(version.jenkinsVersion().equalsIgnoreCase(JenkinsApiMetadata.BUILD_VERSION));
//            assertSent(server, "HEAD", "/");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetSystemInfoOnError() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(
//            new MockResponse().setBody("Not Authorized").setResponseCode(401));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        SystemApi api = jenkinsApi.systemApi();
//        try {
//            final SystemInfo version = api.systemInfo();
//            assertNotNull(version);
//            assertFalse(version.errors().isEmpty());
//            assertSent(server, "HEAD", "/");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testQuietDown() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        SystemApi api = jenkinsApi.systemApi();
//        try {
//            ResponseEntity<Void> success = api.quietDown();
//            assertNotNull(success);
//            assertTrue(success.getValues());
//            assertSentAccept(server, "POST", "/quietDown", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testQuietDownOnAuthException() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(401));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        SystemApi api = jenkinsApi.systemApi();
//        try {
//            ResponseEntity<Void> status = api.quietDown();
//            assertFalse(status.getValues());
//            assertFalse(status.errors().isEmpty());
//            assertTrue(status.errors().get(0).exceptionName().endsWith("AuthorizationException"));
//            assertSentAccept(server, "POST", "/quietDown", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testCancelQuietDown() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        SystemApi api = jenkinsApi.systemApi();
//        try {
//            ResponseEntity<Void> success = api.cancelQuietDown();
//            assertNotNull(success);
//            assertTrue(success.getValues());
//            assertSentAccept(server, "POST", "/cancelQuietDown", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testCancelQuietDownOnAuthException() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(401));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        SystemApi api = jenkinsApi.systemApi();
//        try {
//            ResponseEntity<Void> status = api.cancelQuietDown();
//            assertFalse(status.getValues());
//            assertFalse(status.errors().isEmpty());
//            assertTrue(status.errors().get(0).exceptionName().endsWith("AuthorizationException"));
//            assertSentAccept(server, "POST", "/cancelQuietDown", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//}
