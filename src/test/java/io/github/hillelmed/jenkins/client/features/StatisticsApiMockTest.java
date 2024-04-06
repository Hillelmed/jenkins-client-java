package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.statistics.*;


import okhttp3.mockwebserver.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Mock tests for the {@link StatisticsApi}
 * class.
 */
@Test(groups = "unit")
public class StatisticsApiMockTest extends BaseJenkinsMockTest {

    public void testOverallLoad() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setBody(payloadFromResource("/overall-load.json")).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        StatisticsApi api = jenkinsApi.statisticsApi();
        try {
            OverallLoad load = api.overallLoad();
            assertNotNull(load);
            assertSent(server, "GET", "/overallLoad/api/json");
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }
}
