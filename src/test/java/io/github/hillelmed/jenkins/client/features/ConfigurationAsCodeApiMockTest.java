package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.exception.*;


import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Mock tests for the {@link ConfigurationAsCodeApi} class.
 */
@Test(groups = "unit")
public class ConfigurationAsCodeApiMockTest extends BaseJenkinsMockTest {

    public void testCascCheck() throws Exception {
        try (MockWebServer server = mockWebServer();
             JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort())) {
            server.enqueue(new MockResponse().setResponseCode(200));

            ConfigurationAsCodeApi api = jenkinsApi.configurationAsCodeApi();
            ResponseEntity<Void> requestStatus = api.check("random");
            assertNotNull(requestStatus);
            assertTrue(requestStatus.getStatusCode().is2xxSuccessful());
        }
    }

    public void testCascApply() throws Exception {
        try (MockWebServer server = mockWebServer();
             JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort())) {
            server.enqueue(new MockResponse().setResponseCode(200));

            ConfigurationAsCodeApi api = jenkinsApi.configurationAsCodeApi();
            ResponseEntity<Void> requestStatus = api.apply("random");
            assertNotNull(requestStatus);
            assertTrue(requestStatus.getStatusCode().is2xxSuccessful());
        }
    }

    public void testBadCascCheck() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(500));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        ConfigurationAsCodeApi api = jenkinsApi.configurationAsCodeApi();
        try {
            ResponseEntity<Void> requestStatus = api.check("random");
        } catch (JenkinsAppException e) {
            assertNotNull(e);
            assertEquals(e.errors().size(), 1);
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

    public void testBadCascApply() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(500));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        ConfigurationAsCodeApi api = jenkinsApi.configurationAsCodeApi();
        try {
            ResponseEntity<Void> requestStatus = api.apply("random");
            assertNotNull(requestStatus);
        } catch (JenkinsAppException e) {
            assertNotNull(e);
            assertEquals(e.errors().size(), 1);

        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }
}
