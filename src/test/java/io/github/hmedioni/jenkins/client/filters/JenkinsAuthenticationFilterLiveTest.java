/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.hmedioni.jenkins.client.filters;

import io.github.hmedioni.jenkins.client.*;
import io.github.hmedioni.jenkins.client.auth.*;
import io.github.hmedioni.jenkins.client.domain.user.*;
import io.github.hmedioni.jenkins.client.features.*;
import org.springframework.http.*;
import org.springframework.util.*;
import org.testng.annotations.*;

import java.net.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class JenkinsAuthenticationFilterLiveTest extends BaseJenkinsTest {
    final String usernamePassword = "admin:admin";
    private final String endPoint = "http://localhost:8080";

    @Test
    public void testAnonymousNeedsCrumb() throws Exception {
        try (JenkinsApi jenkinsApi = api(new URL(endPoint), AuthenticationType.ANONYMOUS, null)) {
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

        try (JenkinsApi jenkinsApi = api(new URL(endPoint), AuthenticationType.USERNAME_PASSWORD, usernamePassword)) {
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

        try (JenkinsApi jenkinsApi = api(new URL(endPoint), AuthenticationType.USERNAME_API_TOKEN, usernameApiToken)) {
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
        try (JenkinsApi api = api(new URL(endPoint), AuthenticationType.USERNAME_PASSWORD, credStr)) {
            user = api.userApi();
        }
        return user.generateNewToken(usernamePassword.split(":")[0], "filter-test-token").getBody();
    }

    private void revokeApiToken(final String credStr, final ApiToken apiToken) throws Exception {
        UserApi user;
        try (JenkinsApi api = api(new URL(endPoint), AuthenticationType.USERNAME_API_TOKEN, credStr)) {
            user = api.userApi();
        }
        user.revoke(usernamePassword.split(":")[0], apiToken.getData().getTokenUuid());
    }

}
