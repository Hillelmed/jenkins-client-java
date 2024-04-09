package io.github.hillelmed.jenkins.client.features;


import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.user.*;
import io.github.hillelmed.jenkins.client.exception.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class UserApiLiveTest extends BaseJenkinsApiLiveTest {

    private ApiToken token;

    @Test
    public void testGetUser() {
        User userObj = api().get(user).getBody();
        assertNotNull(userObj);
        assertNotNull(userObj.getAbsoluteUrl());
        assertEquals(userObj.getAbsoluteUrl(), url + "/user/admin");
        assertTrue(userObj.getDescription() == null || userObj.getDescription().equals(""));
        assertNotNull(userObj.getFullName());
        assertEquals(userObj.getFullName(), "admin");
        assertNotNull(userObj.getId());
        assertEquals(userObj.getId(), "admin");
    }

    @Test
    public void testGenerateNewToken() {
        token = api().generateNewToken(user, "user-api-test-token").getBody();
        assertNotNull(token);
        assertEquals(token.getStatus(), "ok");
        assertNotNull(token.getData());
        assertNotNull(token.getData().getTokenName());
        assertEquals(token.getData().getTokenName(), "user-api-test-token");
        assertNotNull(token.getData().getTokenUuid());
        assertNotNull(token.getData().getTokenValue());
    }

    @Test(dependsOnMethods = "testGenerateNewToken")
    public void testRevokeApiToken() {
        ResponseEntity<Void> status = api().revoke(user, token.getData().getTokenUuid());
        // Jenkins returns 200 whether the tokenUuid is correct or not.
        assertTrue(status.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testRevokeApiTokenWithEmptyUuid() {
        try {
            api().revoke("", "");
        } catch (JenkinsAppException e) {
            assertNotNull(e.errors());
        }
        // TODO: Deal with the HTML response from Jenkins Stapler
    }

    private UserApi api() {
        return api.userApi();
    }
}
