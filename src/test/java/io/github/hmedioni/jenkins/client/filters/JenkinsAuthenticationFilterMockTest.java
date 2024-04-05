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
//            assertTrue(headers.containsEntry("Authorization", creds.getAuthType().getAuthScheme() + " " + creds.getEncodedCred()));
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
//            assertTrue(headers.containsEntry("Authorization", creds.getAuthType().getAuthScheme() + " " + creds.getEncodedCred()));
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
//            assertTrue(headers.containsEntry("Authorization", creds.getAuthType().getAuthScheme() + " " + creds.getEncodedCred()));
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//}
