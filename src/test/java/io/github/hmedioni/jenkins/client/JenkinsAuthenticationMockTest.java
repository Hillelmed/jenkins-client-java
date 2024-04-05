package io.github.hmedioni.jenkins.client;

import io.github.hmedioni.jenkins.client.auth.*;
import org.testng.*;
import org.testng.annotations.*;

import java.nio.charset.*;
import java.util.*;

import static org.testng.Assert.*;

public class JenkinsAuthenticationMockTest {

    @Test
    public void testAnonymousAuthentication() {
        JenkinsAuthentication ja = JenkinsAuthentication.builder().build();
        assertEquals(ja.getAuthType().getAuthName(), "Anonymous");
        Assert.assertEquals(ja.getAuthType(), AuthenticationType.ANONYMOUS);
        assertEquals(ja.getEncodedCred(), Base64.getEncoder().encodeToString("anonymous:".getBytes()));
    }

    @Test
    public void testUsernamePasswordAuthentication() {
        String encoded = Base64.getEncoder().encodeToString("user:password".getBytes());
        JenkinsAuthentication ja = JenkinsAuthentication.builder()
            .encodedCred(encoded)
            .build();
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        String decodedString = new String(decodedBytes);

        assertEquals(decodedString.split(":")[0], "user");
        Assert.assertEquals(ja.getAuthType(), AuthenticationType.USERNAME_PASSWORD);
        assertEquals(ja.getEncodedCred(), Base64.getEncoder().encodeToString("user:password".getBytes()));
    }

    @Test
    public void testUsernameApiTokenAuthentication() {
        JenkinsAuthentication ja = JenkinsAuthentication.builder()
            .authType(AuthenticationType.USERNAME_API_TOKEN)
            .encodedCred(Base64.getEncoder().encodeToString("user:token".getBytes()))
            .build();
        assertEquals(ja.getIdentity(), "user");
        Assert.assertEquals(ja.getAuthType(), AuthenticationType.USERNAME_API_TOKEN);
        assertEquals(ja.getEncodedCred(), Base64.getEncoder().encodeToString("user:token".getBytes()));
    }

    @Test
    public void testEncodedUsernamePasswordAuthentication() {
        String encoded = "user:password";
        JenkinsAuthentication ja = JenkinsAuthentication.builder()
            .credentials(encoded)
            .build();
        assertEquals(ja.getIdentity(), "user");
        Assert.assertEquals(ja.getAuthType(), AuthenticationType.USERNAME_PASSWORD);
        assertEquals(ja.getEncodedCred(), Base64.getEncoder().encodeToString(encoded.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testEncodedUsernameApiTokenAuthentication() {
        String encoded = "user:token";
        JenkinsAuthentication ja = JenkinsAuthentication.builder()
            .apiToken(encoded)
            .build();
        assertEquals(ja.getIdentity(), "user");
        Assert.assertEquals(ja.getAuthType(), AuthenticationType.USERNAME_API_TOKEN);
        assertEquals(ja.getEncodedCred(), Base64.getEncoder().encodeToString(encoded.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testEmptyUsernamePassword() {
        JenkinsAuthentication ja = JenkinsAuthentication.builder()
            .credentials(":")
            .build();
        assertEquals(ja.getIdentity(), "");
        Assert.assertEquals(ja.getAuthType(), AuthenticationType.USERNAME_PASSWORD);
        assertEquals(ja.getEncodedCred(), Base64.getEncoder().encodeToString(":".getBytes()));
    }

    @Test
    public void testEmptyUsernameApiToken() {
        JenkinsAuthentication ja = JenkinsAuthentication.builder()
            .apiToken(":")
            .build();
        assertEquals(ja.getIdentity(), "");
        Assert.assertEquals(ja.getAuthType(), AuthenticationType.USERNAME_API_TOKEN);
        assertEquals(ja.getEncodedCred(), Base64.getEncoder().encodeToString(":".getBytes()));
    }

    @Test
    public void testUndetectableCredential() {
        String invalid = Base64.getEncoder().encodeToString("no_colon_here".getBytes());
        try {
            JenkinsAuthentication.builder()
                .apiToken(invalid)
                .build();
        } catch (RuntimeException ex) {
            assertEquals(ex.getMessage(),
                "Unable to detect the identity being used in '" + invalid + "'. Supported types are a user:password, or a user:apiToken, or their base64 encoded value.");
        }
    }
}
