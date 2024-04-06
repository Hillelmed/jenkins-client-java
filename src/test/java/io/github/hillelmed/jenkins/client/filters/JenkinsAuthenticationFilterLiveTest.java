package io.github.hillelmed.jenkins.client.filters;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.auth.*;
import io.github.hillelmed.jenkins.client.domain.user.*;
import io.github.hillelmed.jenkins.client.features.*;




import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class JenkinsAuthenticationFilterLiveTest extends BaseJenkinsTest {


    @BeforeTest
    public void cleanJenkins() {
        JenkinsApi api = api(url, AuthenticationType.USERNAME_PASSWORD, usernamePassword);
        Objects.requireNonNull(api.jobsApi().jobList().getBody()).getJobs().forEach(job -> api.jobsApi().delete(job.getName()));
    }

    @Test
    public void testAnonymousNeedsCrumb() throws Exception {
        try (JenkinsApi jenkinsApi = api(url, AuthenticationType.ANONYMOUS, null)) {
            // Do something that needs POST so the crumb logic is exercised
            String config = payloadFromResource("/freestyle-project-no-params.xml");
            ResponseEntity<Void> success = jenkinsApi.jobsApi().create("DevTest", config);
            assertEquals(success.getStatusCode(), HttpStatus.OK);

            // Delete the job
            ResponseEntity<Void> success2 = jenkinsApi.jobsApi().delete("DevTest");
            assertNotNull(success2);
            assertEquals(success2.getStatusCode(), HttpStatus.FOUND);
            // Debugging note: Jenkins returns 302 after POSTing the delete, causing JClouds to follow the redirect and POST again
        }
    }

    @Test
    public void testUsernamePasswordNeedsCrumb() throws Exception {

        try (JenkinsApi jenkinsApi = api(url, AuthenticationType.USERNAME_PASSWORD, usernamePassword)) {
            // Do something that needs POST so the crumb logic is exercized
            String config = payloadFromResource("/freestyle-project-no-params.xml");
            ResponseEntity<Void> success = jenkinsApi.jobsApi().create("DevTest", config);
            assertEquals(success.getStatusCode(), HttpStatus.OK);

            // Delete the job
            ResponseEntity<Void> success2 = jenkinsApi.jobsApi().delete("DevTest");
            assertNotNull(success2);
            assertEquals(success2.getStatusCode(), HttpStatus.FOUND);
            // Debugging note: Jenkins returns 302 after POSTing the delete, causing JClouds to follow the redirect and POST again
        }
    }

    @Test
    public void testUsernameApiTokenNeedsNoCrumb() throws Exception {
        // Generate an API Token
        final ApiToken apiToken = generateNewApiToken(usernamePassword);

        // Create a Jenkins Client using the API Token
        System.out.println("Okay, we have the token: " + apiToken.getData().getTokenValue());
        final String usernameApiToken = usernamePassword.split(":")[0] + ":" + apiToken.getData().getTokenValue();

        try (JenkinsApi jenkinsApi = api(url, AuthenticationType.USERNAME_API_TOKEN, usernameApiToken)) {
            // Do something that needs POST so the crumb logic is exercized
            String config = payloadFromResource("/freestyle-project-no-params.xml");
            ResponseEntity<Void> success = jenkinsApi.jobsApi().create("DevTest", config);
            assertEquals(success.getStatusCode(), HttpStatus.OK);

            // Delete the job
            ResponseEntity<Void> success2 = jenkinsApi.jobsApi().delete("DevTest");
            assertNotNull(success2);
            assertEquals(success2.getStatusCode(), HttpStatus.FOUND);
            // Debugging note: Jenkins returns 302 after POSTing the delete, causing JClouds to follow the redirect and POST again
        } finally {
            revokeApiToken(usernameApiToken, apiToken);
        }
    }

    private ApiToken generateNewApiToken(final String credStr) throws Exception {
        UserApi user;
        try (JenkinsApi api = api(url, AuthenticationType.USERNAME_PASSWORD, credStr)) {
            user = api.userApi();
        }
        return user.generateNewToken(usernamePassword.split(":")[0], "filter-test-token").getBody();
    }

    private void revokeApiToken(final String credStr, final ApiToken apiToken) throws Exception {
        UserApi user;
        try (JenkinsApi api = api(url, AuthenticationType.USERNAME_API_TOKEN, credStr)) {
            user = api.userApi();
        }
        user.revoke(usernamePassword.split(":")[0], apiToken.getData().getTokenUuid());
    }

}
