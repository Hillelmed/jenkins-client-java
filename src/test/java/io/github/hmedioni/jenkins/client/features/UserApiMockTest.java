
//package io.github.hmedioni.jenkins.client.features;
//
//import io.github.hmedioni.jenkins.client.*;
//import io.github.hmedioni.jenkins.client.domain.user.*;
//import okhttp3.mockwebserver.*;
//import org.testng.annotations.*;
//
//import static org.testng.Assert.*;
//
///**
// * Mock tests for the {@link JobsApi} class.
// */
//@Test(groups = "unit", testName = "UserApiMockTest")
//public class UserApiMockTest extends BaseJenkinsMockTest {
//
//    @Test
//    public void testGetUser() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/user.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        //JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        try (JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort())) {
//            UserApi api = jenkinsApi.userApi();
//            User output = api.get();
//            assertNotNull(output);
//            assertNotNull(output.absoluteUrl());
//            assertEquals(output.absoluteUrl(), "http://localhost:8080/user/admin");
//            assertNull(output.description());
//            assertNotNull(output.fullName());
//            assertEquals(output.fullName(), "Administrator");
//            assertNotNull(output.id());
//            assertEquals(output.id(), "admin");
//            assertSent(server, "GET", "/user/user/api/json");
//        } finally {
//            server.shutdown();
//        }
//    }
//
//    @Test
//    public void testGenerateNewApiToken() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/api-token.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        try (JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort())) {
//            UserApi api = jenkinsApi.userApi();
//            ApiToken output = api.generateNewToken("random");
//            assertNotNull(output);
//            assertNotNull(output.status());
//            assertEquals(output.status(), "ok");
//            assertNotNull(output.data());
//            assertNotNull(output.data().tokenName());
//            assertEquals(output.data().tokenName(), "kb-token");
//            assertNotNull(output.data().tokenUuid());
//            assertEquals(output.data().tokenUuid(), "8c42630b-4be5-4f51-b4e9-f17a8ac07521");
//            assertNotNull(output.data().tokenValue());
//            assertEquals(output.data().tokenValue(), "112fe6e9b1b94eb1ee58f0ea4f5a1ac7bf");
//            assertSentWithFormData(server, "POST", "/user/user/descriptorByName/jenkins.security.ApiTokenProperty/generateNewToken", "newTokenName=random");
//        } finally {
//            server.shutdown();
//        }
//    }
//
//    // TODO: testRevokeApiToken
//    // TODO: testRevokeApiTokenWithEmptyUuid
//}
