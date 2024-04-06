package io.github.hmedioni.jenkins.client.features;

import io.github.hmedioni.jenkins.client.*;
import io.github.hmedioni.jenkins.client.domain.statistics.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class StatisticsApiLiveTest extends BaseJenkinsApiLiveTest {
    @Test
    public void testOverallLoad() {
        OverallLoad load = api().overallLoad();
        assertNotNull(load);
    }

    private StatisticsApi api() {
        return api.statisticsApi();
    }
}
