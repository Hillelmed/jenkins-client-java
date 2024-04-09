package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.auth.*;
import io.github.hillelmed.jenkins.client.domain.crumb.*;
import io.github.hillelmed.jenkins.client.exception.*;
import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Mock tests for the {@link CrumbIssuerApi} class.
 */
@Test(groups = "unit")
public class CrumbIssuerApiMockTest extends BaseJenkinsMockTest {

    public void testGetCrumbIssuer() throws Exception {
        MockWebServer server = mockWebServer(false);

        final String value = "eafb798e91a90591b7fb2b779b32dcef9311cfdee8de09158f23a16a11aafa22";
        final String body = "{\"_class\":\"hudson.security.csrf.DefaultCrumbIssuer\",\"crumb\":\"" + value + "\",\"crumbRequestField\":\"Jenkins-Crumb\"}";
        server.enqueue(new MockResponse()
            .setBody(body).
            setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            CrumbIssuerApi api = jenkinsApi.crumbIssuerApi();
            final Crumb instance = api.crumb().getBody();
            assertNotNull(instance);
            assertEquals(instance.getValue(), value);
            assertSentAccept(server, "GET", "/crumbIssuer/api/json", MediaType.APPLICATION_JSON_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testGetCrumbIssuerFail() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setResponseCode(500));
        try (JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort(), AuthenticationType.USERNAME_PASSWORD, usernamePassword)) {
            jenkinsApi.jobsApi().enable("blala");
        } catch (JenkinsAppException e) {
            assertEquals(e.getClass(), CrumbMissing.class);
            assertSentAccept(server, "GET", "/crumbIssuer/api/json", MediaType.ALL_VALUE);
        } finally {
            server.shutdown();
        }
    }
}
