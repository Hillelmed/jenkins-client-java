package io.github.hmedioni.jenkins.client.features;

import io.github.hmedioni.jenkins.client.*;
import io.github.hmedioni.jenkins.client.domain.user.*;
import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Mock tests for the {@link JobsApi} class.
 */
@Test(groups = "unit")
public class UserApiMockTest extends BaseJenkinsMockTest {

    @Test
    public void testGetUser() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/user.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        //JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort())) {
            UserApi api = jenkinsApi.userApi();
            User output = api.get(user).getBody();
            assertNotNull(output);
            assertNotNull(output.getAbsoluteUrl());
            assertEquals(output.getAbsoluteUrl(), "http://localhost:8080/user/admin");
            assertNull(output.getDescription());
            assertNotNull(output.getFullName());
            assertEquals(output.getFullName(), "Administrator");
            assertNotNull(output.getId());
            assertEquals(output.getId(), "admin");
            assertSent(server, "GET", "/user/admin/api/json");
        } finally {
            server.shutdown();
        }
    }

    @Test
    public void testGenerateNewApiToken() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/api-token.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        try (JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort())) {
            UserApi api = jenkinsApi.userApi();
            ApiToken output = api.generateNewToken(user, "random").getBody();
            assertNotNull(output);
            assertNotNull(output.getStatus());
            assertEquals(output.getStatus(), "ok");
            assertNotNull(output.getData());
            assertNotNull(output.getData().getTokenName());
            assertEquals(output.getData().getTokenName(), "kb-token");
            assertNotNull(output.getData().getTokenUuid());
            assertEquals(output.getData().getTokenUuid(), "8c42630b-4be5-4f51-b4e9-f17a8ac07521");
            assertNotNull(output.getData().getTokenValue());
            assertEquals(output.getData().getTokenValue(), "112fe6e9b1b94eb1ee58f0ea4f5a1ac7bf");
            assertSentWithFormData(server, "POST",
                "/user/admin/descriptorByName/jenkins.security.ApiTokenProperty/generateNewToken",
                "newTokenName=random");
        } finally {
            server.shutdown();
        }
    }

    // TODO: testRevokeApiToken
    // TODO: testRevokeApiTokenWithEmptyUuid
}
