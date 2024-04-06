package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.plugins.*;
import io.github.hillelmed.jenkins.client.exception.*;



import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.Assert.*;

/**
 * Mock tests for the {@link PluginManagerApi} class.
 */
@Test(groups = "unit")
public class PluginManagerApiMockTest extends BaseJenkinsMockTest {

    public void testGetPlugins() throws Exception {
        final MockWebServer server = mockWebServer(false);
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setBody(payloadFromResource("/plugins.json")).setResponseCode(200));

        final JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            final PluginManagerApi api = jenkinsApi.pluginManagerApi();
            final Plugins plugins = api.plugins(3);
            assertNotNull(plugins);
            assertFalse(plugins.getPlugins().isEmpty());
            assertNotNull(plugins.getPlugins().get(0).getShortName());
            final Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("depth", 3);
            assertSent(server, "GET", "/pluginManager/api/json", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetPluginsOnAuthException() throws Exception {
        final MockWebServer server = mockWebServer(false);
        server.enqueue(new MockResponse().setResponseCode(401));

        final JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            final PluginManagerApi api = jenkinsApi.pluginManagerApi();
            api.plugins(3);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertFalse(e.errors().isEmpty());
            final Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("depth", 3);
            assertSent(server, "GET", "/pluginManager/api/json", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testInstallNecessaryPlugins() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200));

        final JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            final PluginManagerApi api = jenkinsApi.pluginManagerApi();
            final ResponseEntity<Void> status = api.installNecessaryPlugins("artifactory@2.2.1");
            assertNotNull(status);
            assertSent(server, "POST", "/pluginManager/installNecessaryPlugins");
        } finally {
            server.shutdown();
        }
    }

    public void testInstallNecessaryPluginsOnAuthException() throws Exception {
        final MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setResponseCode(401));

        final JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            final PluginManagerApi api = jenkinsApi.pluginManagerApi();
            api.installNecessaryPlugins("artifactory@2.2.1");
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertFalse(e.errors().isEmpty());
            assertTrue(e.errors().get(0).getExceptionName().endsWith("AuthorizationException"));
            assertSent(server, "POST", "/pluginManager/installNecessaryPlugins");
        } finally {
            server.shutdown();
        }
    }
}
