package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.system.*;
import io.github.hillelmed.jenkins.client.exception.*;
import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Mock tests for the {@link SystemApi} class.
 */
@Test(groups = "unit")
public class SystemApiMockTest extends BaseJenkinsMockTest {

    public void testGetSystemInfo() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/system-info.json");

        server.enqueue(
            new MockResponse().setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            SystemApi api = jenkinsApi.systemApi();
            final SystemInfo version = api.systemInfo().getBody();
            assertNotNull(version);
            assertTrue(version.getDescription().equalsIgnoreCase("Jenkins REST API Configuration as code test"));
            assertSent(server, "GET", "/api/json/systemInfo");
        } finally {
            server.shutdown();
        }
    }

    public void testGetSystemInfoOnError() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(
            new MockResponse().setBody("Not Authorized").setResponseCode(401));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            SystemApi api = jenkinsApi.systemApi();
            final SystemInfo version = api.systemInfo().getBody();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertFalse(e.errors().isEmpty());
            assertSent(server, "GET", "/api/json/systemInfo");
        } finally {
            server.shutdown();
        }
    }

    public void testQuietDown() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            SystemApi api = jenkinsApi.systemApi();
            ResponseEntity<Void> success = api.quietDown();
            assertNotNull(success);
            assertSentAccept(server, "POST", "/quietDown", MediaType.ALL_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testQuietDownOnAuthException() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(401));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            SystemApi api = jenkinsApi.systemApi();
            ResponseEntity<Void> status = api.quietDown();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertFalse(e.errors().isEmpty());
            assertTrue(e.errors().get(0).getExceptionName().endsWith("AuthorizationException"));
            assertSentAccept(server, "POST", "/quietDown", MediaType.TEXT_HTML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testCancelQuietDown() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            SystemApi api = jenkinsApi.systemApi();
            ResponseEntity<Void> success = api.cancelQuietDown();
            assertNotNull(success);
            assertSentAccept(server, "POST", "/cancelQuietDown", MediaType.ALL_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testCancelQuietDownOnAuthException() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(401));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            SystemApi api = jenkinsApi.systemApi();
            ResponseEntity<Void> status = api.cancelQuietDown();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertFalse(e.errors().isEmpty());
            assertTrue(e.errors().get(0).getExceptionName().endsWith("AuthorizationException"));
            assertSentAccept(server, "POST", "/cancelQuietDown", MediaType.TEXT_HTML_VALUE);
        } finally {
            server.shutdown();
        }
    }
}
