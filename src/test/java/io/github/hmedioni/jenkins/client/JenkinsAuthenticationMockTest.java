//
//
//package io.github.hmedioni.jenkins.client;
//
//import io.github.hmedioni.jenkins.client.auth.*;
//import io.github.hmedioni.jenkins.client.exception.*;
//import org.testng.*;
//import org.testng.annotations.*;
//
//import static org.testng.Assert.*;
//
//public class JenkinsAuthenticationMockTest {
//
//    @Test
//    public void testAnonymousAuthentication() {
//        JenkinsAuthentication ja = JenkinsAuthentication.builder().build();
//        assertEquals(ja.identity, "anonymous");
//        Assert.assertEquals(ja.authType(), AuthenticationType.Anonymous);
//        assertEquals(ja.authValue(), base64().encode("anonymous:".getBytes()));
//        assertEquals(ja.credential, ja.authValue());
//    }
//
//    @Test
//    public void testUsernamePasswordAuthentication() {
//        JenkinsAuthentication ja = JenkinsAuthentication.builder()
//            .credentials("user:password")
//            .build();
//        assertEquals(ja.identity, "user");
//        Assert.assertEquals(ja.authType(), AuthenticationType.UsernamePassword);
//        assertEquals(ja.authValue(), base64().encode("user:password".getBytes()));
//        assertEquals(ja.credential, ja.authValue());
//    }
//
//    @Test
//    public void testUsernameApiTokenAuthentication() {
//        JenkinsAuthentication ja = JenkinsAuthentication.builder()
//            .apiToken("user:token")
//            .build();
//        assertEquals(ja.identity, "user");
//        Assert.assertEquals(ja.authType(), AuthenticationType.UsernameApiToken);
//        assertEquals(ja.authValue(), base64().encode("user:token".getBytes()));
//        assertEquals(ja.credential, ja.authValue());
//    }
//
//    @Test
//    public void testEncodedUsernamePasswordAuthentication() {
//        String encoded = base64().encode("user:password".getBytes());
//        JenkinsAuthentication ja = JenkinsAuthentication.builder()
//            .credentials(encoded)
//            .build();
//        assertEquals(ja.identity, "user");
//        Assert.assertEquals(ja.authType(), AuthenticationType.UsernamePassword);
//        assertEquals(ja.authValue(), encoded);
//        assertEquals(ja.credential, ja.authValue());
//    }
//
//    @Test
//    public void testEncodedUsernameApiTokenAuthentication() {
//        String encoded = base64().encode("user:token".getBytes());
//        JenkinsAuthentication ja = JenkinsAuthentication.builder()
//            .apiToken(encoded)
//            .build();
//        assertEquals(ja.identity, "user");
//        Assert.assertEquals(ja.authType(), AuthenticationType.UsernameApiToken);
//        assertEquals(ja.authValue(), encoded);
//        assertEquals(ja.credential, ja.authValue());
//    }
//
//    @Test
//    public void testEmptyUsernamePassword() {
//        JenkinsAuthentication ja = JenkinsAuthentication.builder()
//            .credentials(":")
//            .build();
//        assertEquals(ja.identity, "");
//        Assert.assertEquals(ja.authType(), AuthenticationType.UsernamePassword);
//        assertEquals(ja.authValue(), base64().encode(":".getBytes()));
//        assertEquals(ja.credential, ja.authValue());
//    }
//
//    @Test
//    public void testEmptyUsernameApiToken() {
//        JenkinsAuthentication ja = JenkinsAuthentication.builder()
//            .apiToken(":")
//            .build();
//        assertEquals(ja.identity, "");
//        Assert.assertEquals(ja.authType(), AuthenticationType.UsernameApiToken);
//        assertEquals(ja.authValue(), base64().encode(":".getBytes()));
//        assertEquals(ja.credential, ja.authValue());
//    }
//
//    @Test
//    public void testUndetectableCredential() {
//        String invalid = base64().encode("no_colon_here".getBytes());
//        try {
//            JenkinsAuthentication.builder()
//                .apiToken(invalid)
//                .build();
//        } catch (UndetectableIdentityException ex) {
//            assertEquals(ex.getMessage(),
//                "Unable to detect the identity being used in '" + invalid + "'. Supported types are a user:password, or a user:apiToken, or their base64 encoded value.");
//        }
//    }
//}
