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
package io.github.hmedioni.jenkins.client.features;


import io.github.hmedioni.jenkins.client.*;
import io.github.hmedioni.jenkins.client.domain.common.*;
import io.github.hmedioni.jenkins.client.domain.user.*;
import io.github.hmedioni.jenkins.client.exception.*;
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
        token = api().generateNewToken(user,"user-api-test-token").getBody();
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
        ResponseEntity<Void> status = api().revoke(user,token.getData().getTokenUuid());
        // Jenkins returns 200 whether the tokenUuid is correct or not.
        assertTrue(status.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testRevokeApiTokenWithEmptyUuid() {
        try {
            api().revoke("","");
        } catch (JenkinsAppException e) {
            assertNotNull(e.errors());
        }
        // TODO: Deal with the HTML response from Jenkins Stapler
    }

    private UserApi api() {
        return api.userApi();
    }
}
