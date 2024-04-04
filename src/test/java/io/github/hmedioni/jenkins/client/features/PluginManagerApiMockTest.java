//package io.github.hmedioni.jenkins.client.features;
//
//import io.github.hmedioni.jenkins.client.*;
//import io.github.hmedioni.jenkins.client.domain.common.*;
//import io.github.hmedioni.jenkins.client.domain.plugins.*;
//import okhttp3.mockwebserver.*;
//import org.testng.annotations.*;
//
//import java.util.*;
//
//import static org.testng.Assert.*;
//
///**
// * Mock tests for the {@link PluginManagerApi} class.
// */
//@Test(groups = "unit", testName = "PluginManagerApiMockTest")
//public class PluginManagerApiMockTest extends BaseJenkinsMockTest {
//
//    public void testGetPlugins() throws Exception {
//        final MockWebServer server = mockWebServer();
//        server.enqueue(new MockResponse().setBody(payloadFromResource("/plugins.json")).setResponseCode(200));
//
//        final JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        final PluginManagerApi api = jenkinsApi.pluginManagerApi();
//        try {
//            final Plugins plugins = api.plugins(3, null);
//            assertNotNull(plugins);
//            assertTrue(plugins.errors().isEmpty());
//            assertFalse(plugins.plugins().isEmpty());
//            assertNotNull(plugins.plugins().get(0).shortName());
//            final Map<String, Object> queryParams = Maps.newHashMap();
//            queryParams.put("depth", 3);
//            assertSent(server, "GET", "/pluginManager/api/json", queryParams);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetPluginsOnAuthException() throws Exception {
//        final MockWebServer server = mockWebServer();
//        server.enqueue(new MockResponse().setResponseCode(401));
//
//        final JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        final PluginManagerApi api = jenkinsApi.pluginManagerApi();
//        try {
//            final Plugins plugins = api.plugins(3, null);
//            assertNotNull(plugins);
//            assertNull(plugins.clazz());
//            assertFalse(plugins.errors().isEmpty());
//            assertTrue(plugins.errors().get(0).exceptionName().endsWith("AuthorizationException"));
//            final Map<String, Object> queryParams = Maps.newHashMap();
//            queryParams.put("depth", 3);
//            assertSent(server, "GET", "/pluginManager/api/json", queryParams);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testInstallNecessaryPlugins() throws Exception {
//        final MockWebServer server = mockWebServer();
//        server.enqueue(new MockResponse().setResponseCode(200));
//
//        final JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        final PluginManagerApi api = jenkinsApi.pluginManagerApi();
//        try {
//            final ResponseEntity<Void> status = api.installNecessaryPlugins("artifactory@2.2.1");
//            assertNotNull(status);
//            assertTrue(status.getValues());
//            assertTrue(status.errors().isEmpty());
//            assertSent(server, "POST", "/pluginManager/installNecessaryPlugins");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testInstallNecessaryPluginsOnAuthException() throws Exception {
//        final MockWebServer server = mockWebServer();
//        server.enqueue(new MockResponse().setResponseCode(401));
//
//        final JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        final PluginManagerApi api = jenkinsApi.pluginManagerApi();
//        try {
//            final ResponseEntity<Void> status = api.installNecessaryPlugins("artifactory@2.2.1");
//            assertNotNull(status);
//            assertFalse(status.getValues());
//            assertFalse(status.errors().isEmpty());
//            assertTrue(status.errors().get(0).exceptionName().endsWith("AuthorizationException"));
//            assertSent(server, "POST", "/pluginManager/installNecessaryPlugins");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//}
