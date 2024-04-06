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
