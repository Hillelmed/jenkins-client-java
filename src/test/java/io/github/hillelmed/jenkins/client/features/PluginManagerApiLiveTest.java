package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.plugins.*;


import org.springframework.http.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class PluginManagerApiLiveTest extends BaseJenkinsApiLiveTest {

    @Test
    public void testGetPlugins() {
        final Plugins plugins = api().plugins(3);
        assertNotNull(plugins);
        assertFalse(plugins.getPlugins().isEmpty());
        assertNotNull(plugins.getPlugins().get(0).getShortName());
    }

    @Test
    public void testInstallNecessaryPlugins() {
        String pluginXml = JenkinsUtils.buildPluginXmlRequest("artifactory@2.2.1");
        final ResponseEntity<Void> status = api().installNecessaryPlugins(pluginXml);
        assertNotNull(status);
        assertEquals(status.getStatusCode(), HttpStatus.FOUND);
    }

    private PluginManagerApi api() {
        return api.pluginManagerApi();
    }
}
