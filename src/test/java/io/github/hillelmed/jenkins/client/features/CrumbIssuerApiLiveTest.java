package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.crumb.*;


import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class CrumbIssuerApiLiveTest extends BaseJenkinsApiLiveTest {

    @Test
    public void testGetCrumb() {
        final Crumb crumb = api().crumb().getBody();
        assertNotNull(crumb);
        assertNotNull(crumb.getValue());
    }

    private CrumbIssuerApi api() {
        return api.crumbIssuerApi();
    }
}
