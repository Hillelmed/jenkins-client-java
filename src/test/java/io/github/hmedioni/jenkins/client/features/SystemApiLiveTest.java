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
import io.github.hmedioni.jenkins.client.domain.system.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "live", testName = "SystemApiLiveTest", singleThreaded = true)
public class SystemApiLiveTest extends BaseJenkinsApiLiveTest {

    @Test
    public void testGetSystemInfo() {
        final SystemInfo version = api().systemInfo().getBody();
        assertNotNull(version);
        assertNotNull(version.getMode());
    }

    @Test
    public void testQuietDown() {
        ResponseEntity<Void> success = api().quietDown();
        assertNotNull(success);
        assertEquals(success.getStatusCode(),HttpStatus.OK);
    }

    @Test(dependsOnMethods = "testQuietDown")
    public void testAlreadyQuietDown() {
        ResponseEntity<Void> success = api().quietDown();
        assertNotNull(success);
        assertEquals(success.getStatusCode(),HttpStatus.OK);
    }

    @Test(dependsOnMethods = "testAlreadyQuietDown")
    public void testCancelQuietDown() {
        ResponseEntity<Void> success = api().cancelQuietDown();
        assertNotNull(success);
        assertEquals(success.getStatusCode(),HttpStatus.OK);
    }

    @Test(dependsOnMethods = "testCancelQuietDown")
    public void testAlreadyCanceledQuietDown() {
        ResponseEntity<Void> success = api().cancelQuietDown();
        assertNotNull(success);
        assertEquals(success.getStatusCode(),HttpStatus.OK);
    }

    private SystemApi api() {
        return api.systemApi();
    }
}
