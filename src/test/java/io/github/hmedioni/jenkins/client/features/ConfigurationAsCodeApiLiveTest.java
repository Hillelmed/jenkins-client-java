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
import io.github.hmedioni.jenkins.client.exception.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class ConfigurationAsCodeApiLiveTest extends BaseJenkinsApiLiveTest {

    @Test
    public void testCascCheck() {
        String config = payloadFromResource("/casc.yml");
        ResponseEntity<Void> success = api().check(config);
        assertEquals(success.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testCascApply() {
        String config = payloadFromResource("/casc.yml");
        ResponseEntity<Void> success = api().apply(config);
        assertEquals(success.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testBadCascCheck() {
        String config = payloadFromResource("/casc-bad.yml");
        try {
            api().check(config);
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    public void testBadCascApply() {
        String config = payloadFromResource("/casc-bad.yml");
        try {
            api().apply(config);
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ConfigurationAsCodeApi api() {
        return api.configurationAsCodeApi();
    }
}
