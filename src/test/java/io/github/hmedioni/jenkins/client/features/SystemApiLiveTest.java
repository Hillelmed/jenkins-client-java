package io.github.hmedioni.jenkins.client.features;

import io.github.hmedioni.jenkins.client.*;
import io.github.hmedioni.jenkins.client.domain.system.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
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
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testQuietDown")
    public void testAlreadyQuietDown() {
        ResponseEntity<Void> success = api().quietDown();
        assertNotNull(success);
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testAlreadyQuietDown")
    public void testCancelQuietDown() {
        ResponseEntity<Void> success = api().cancelQuietDown();
        assertNotNull(success);
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testCancelQuietDown")
    public void testAlreadyCanceledQuietDown() {
        ResponseEntity<Void> success = api().cancelQuietDown();
        assertNotNull(success);
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    private SystemApi api() {
        return api.systemApi();
    }
}
