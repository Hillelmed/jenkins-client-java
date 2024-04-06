package io.github.hillelmed.jenkins.client.filters;


import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.auth.*;
import io.github.hillelmed.jenkins.client.domain.crumb.*;



import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class JenkinsAuthenticationFilterMockTest extends BaseJenkinsMockTest {

    @Test
    public void testAnonymousNeedsCrumb() throws Exception {
        MockWebServer server = mockWebServer(false);

        final String value = "{\"_class\":\"hudson.security.csrf.DefaultCrumbIssuer\",\"crumb\":\"eafb798e91a90591b7fb2b779b32dcef9311cfdee8de09158f23a16a11aafa22\",\"crumbRequestField\":\"Jenkins-Crumb\"}";
        final String cookieValue = "JSESSIONID.b747c9b2=node04qcr5em5lt8l10mueszes38ac1.node0; Path=/; HttpOnly";
        server.enqueue(new MockResponse()
            .setBody(value)
            .setHeader(HttpHeaders.SET_COOKIE, cookieValue)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setResponseCode(200));

        JenkinsApi jenkinsApi = anonymousAuthApi("http://localhost:" + server.getPort());
        try {
            ResponseEntity<Crumb> crumbResponseEntity = jenkinsApi.crumbIssuerApi().crumb();
            assertSentAccept(server, "GET", "/crumbIssuer/api/json", MediaType.APPLICATION_JSON_VALUE);
            assertEquals(crumbResponseEntity.getHeaders().size(), 3);
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

    @Test
    public void testUsernamePasswordNeedsCrumb() throws Exception {
        MockWebServer server = mockWebServer(false);

        final String value = "{\"_class\":\"hudson.security.csrf.DefaultCrumbIssuer\",\"crumb\":\"eafb798e91a90591b7fb2b779b32dcef9311cfdee8de09158f23a16a11aafa22\",\"crumbRequestField\":\"Jenkins-Crumb\"}";
        final String cookieValue = "JSESSIONID.b747c9b2=node04qcr5em5lt8l10mueszes38ac1.node0; Path=/; HttpOnly";
        server.enqueue(new MockResponse()
            .setBody(value)
            .setHeader(HttpHeaders.SET_COOKIE, cookieValue)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort(), AuthenticationType.USERNAME_API_TOKEN, usernamePassword);

        try {
            ResponseEntity<Crumb> crumbResponseEntity = jenkinsApi.crumbIssuerApi().crumb();
            assertSentAccept(server, "GET", "/crumbIssuer/api/json", MediaType.APPLICATION_JSON_VALUE);
            assertNotNull(crumbResponseEntity.getBody().getValue());
            assertNotNull(crumbResponseEntity.getBody().getCrumbRequestField());
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }


}
