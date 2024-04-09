package io.github.hillelmed.jenkins.client.features;

import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.common.*;
import io.github.hillelmed.jenkins.client.domain.queue.*;
import io.github.hillelmed.jenkins.client.exception.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class QueueApiLiveTest extends BaseJenkinsApiLiveTest {

    @BeforeTest
    public void cleanJenkinsJob() {
        Objects.requireNonNull(api.jobsApi().jobList().getBody()).getJobs().forEach(job -> api.jobsApi().delete(job.getName()));
    }

    @BeforeTest(dependsOnMethods = "cleanJenkinsJob")
    public void init() {
        String config = payloadFromResource("/freestyle-project-sleep-task.xml");
        ResponseEntity<Void> success = api.jobsApi().create("QueueTest", config);
        assertTrue(success.getStatusCode().is2xxSuccessful());

        config = payloadFromResource("/freestyle-project.xml");
        success = api.jobsApi().create("QueueTestSingleParam", config);
        assertTrue(success.getStatusCode().is2xxSuccessful());

        config = payloadFromResource("/freestyle-project-sleep-task-multiple-params.xml");
        success = api.jobsApi().create("QueueTestMultipleParams", config);
        assertTrue(success.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testGetQueue() {
        ResponseEntity<Void> job1 = api.jobsApi().build("QueueTest");
        assertNotNull(job1);
        assertEquals(job1.getStatusCode(), HttpStatus.CREATED);
        ResponseEntity<Void> job2 = api.jobsApi().build("QueueTest");
        assertNotNull(job2);
        assertEquals(job2.getStatusCode(), HttpStatus.CREATED);
        QueueItemsArray queueItems = api().queue().getBody();
        assertFalse(queueItems.getItems().isEmpty());
        IntegerResponse job2Value = JenkinsUtils.getQueueItemIntegerResponse(job2.getHeaders());
        boolean foundLastKickedJob = false;
        for (QueueItem item : queueItems.getItems()) {
            if (Objects.equals(item.getId(), job2Value.getValues())) {
                foundLastKickedJob = true;
                break;
            }
        }
        assertTrue(foundLastKickedJob);
    }

    @Test
    public void testGetPendingQueueItem() {
        ResponseEntity<Void> job1 = api.jobsApi().build("QueueTest");
        assertNotNull(job1);
        ResponseEntity<Void> job2 = api.jobsApi().build("QueueTest");
        assertNotNull(job2);
        IntegerResponse job2Value = JenkinsUtils.getQueueItemIntegerResponse(job2.getHeaders());
        // job2 is queue after job1, so while job1 runs, job2 is pending in the queue
        QueueItem queueItem = api().queueItem(job2Value.getValues());
        assertFalse(queueItem.isCancelled());
        assertNotNull(queueItem.getWhy());
        assertNull(queueItem.getExecutable());
    }

    @Test
    public void testGetRunningQueueItem() throws InterruptedException {
        ResponseEntity<Void> job1 = api.jobsApi().build("QueueTest");
        assertNotNull(job1);
        ResponseEntity<Void> job2 = api.jobsApi().build("QueueTest");
        assertNotNull(job2);

        // job1 runs first, so we get its queueItem
        IntegerResponse job1Value = JenkinsUtils.getQueueItemIntegerResponse(job1.getHeaders());

        QueueItem queueItem = getRunningQueueItem(job1Value.getValues());

        // If  it means the queueItem has been cancelled, which would not be normal in this test
        assertNotNull(queueItem);
        assertFalse(queueItem.isCancelled());

        //  We exepect this build to run, consequently:
        //  * the why field should now be null
        //  * the executable field should NOT be null
        //  * the build number should be set to an integer
        //  * the url for the build should be set to a string
        assertNull(queueItem.getWhy());
        assertNotNull(queueItem.getExecutable());
    }

    @Test
    public void testQueueItemSingleParameters() throws InterruptedException {
        Map<String, List<String>> params = new HashMap<>();
        params.put("SomeKey", List.of("SomeVeryNewValue1"));
        ResponseEntity<Void> job1 = api.jobsApi().buildWithParameters("QueueTestSingleParam", params);
        assertNotNull(job1);
        IntegerResponse job1Value = JenkinsUtils.getQueueItemIntegerResponse(job1.getHeaders());

        assertTrue(job1Value.getValues() > 0);

        // Jenkins will reject two consecutive build requests when the build parameter values are the same
        // So we must set some different parameter values
        params = new HashMap<>();
        params.put("SomeKey", List.of("SomeVeryNewValue2"));
        ResponseEntity<Void> job2 = api.jobsApi().buildWithParameters("QueueTestSingleParam", params);
        assertNotNull(job2);
        IntegerResponse job2Value = JenkinsUtils.getQueueItemIntegerResponse(job2.getHeaders());

        assertTrue(job2Value.getValues() > 0);

        QueueItem queueItem = getRunningQueueItem(job1Value.getValues());
        assertNotNull(queueItem);
        assertFalse(queueItem.isCancelled());

        Map<String, String> map = new HashMap<>();
        map.put("SomeKey", "SomeVeryNewValue1");
        assertEquals(queueItem.getActions().get(0).getClazz(), "hudson.model.ParametersAction");
        List<QueueItem.Parameter> parameters = queueItem.getActions().get(0).getParameters();
        assertTrue(map.containsKey(parameters.get(0).getName()));
        assertEquals(map.get(parameters.get(0).getName()), parameters.get(0).getValue());
    }

    @Test
    public void testQueueItemMultipleParameters() throws InterruptedException {
        Map<String, List<String>> params = new HashMap<>();
        params.put("SomeKey1", List.of("SomeVeryNewValue1"));
        ResponseEntity<Void> job1 = api.jobsApi().buildWithParameters("QueueTestMultipleParams", params);
        assertNotNull(job1);
        IntegerResponse job1Value = JenkinsUtils.getQueueItemIntegerResponse(job1.getHeaders());

        assertTrue(job1Value.getValues() > 0);

        // Jenkins will reject two consecutive build requests when the build parameter values are the same
        // So we must set some different parameter values
        params = new HashMap<>();
        params.put("SomeKey1", List.of("SomeVeryNewValue2"));
        ResponseEntity<Void> job2 = api.jobsApi().buildWithParameters("QueueTestMultipleParams", params);
        assertNotNull(job2);
        IntegerResponse job2Value = JenkinsUtils.getQueueItemIntegerResponse(job2.getHeaders());

        assertTrue(job2Value.getValues() > 0);

        QueueItem queueItem = getRunningQueueItem(job1Value.getValues());
        assertNotNull(queueItem);
        assertFalse(queueItem.isCancelled());

        Map<String, String> map = new HashMap<>();
        map.put("SomeKey1", "SomeVeryNewValue1");
        map.put("SomeKey2", "SomeValue2");
        map.put("SomeKey3", "SomeValue3");
        assertEquals(queueItem.getActions().get(0).getClazz(), "hudson.model.ParametersAction");
        List<QueueItem.Parameter> parameters = queueItem.getActions().get(0).getParameters();
        assertTrue(map.containsKey(parameters.get(0).getName()));
        assertTrue(map.containsKey(parameters.get(1).getName()));
        assertTrue(map.containsKey(parameters.get(2).getName()));
        assertEquals(map.get(parameters.get(0).getName()), parameters.get(0).getValue());
        assertEquals(map.get(parameters.get(1).getName()), parameters.get(1).getValue());
        assertEquals(map.get(parameters.get(2).getName()), parameters.get(2).getValue());

    }

    @Test
    public void testQueueItemEmptyParameterValue() throws InterruptedException {
        Map<String, List<String>> params = new HashMap<>();
        params.put("SomeKey1", List.of(""));
        ResponseEntity<Void> job1 = api.jobsApi().buildWithParameters("QueueTestMultipleParams", params);
        assertNotNull(job1);
        IntegerResponse job1Value = JenkinsUtils.getQueueItemIntegerResponse(job1.getHeaders());

        assertTrue(job1Value.getValues() > 0);

        QueueItem queueItem = getRunningQueueItem(job1Value.getValues());
        assertNotNull(queueItem);

        Map<String, String> map = new HashMap<>();
        map.put("SomeKey1", "");
        map.put("SomeKey2", "SomeValue2");
        map.put("SomeKey3", "SomeValue3");
        assertEquals(queueItem.getActions().get(0).getClazz(), "hudson.model.ParametersAction");
        List<QueueItem.Parameter> parameters = queueItem.getActions().get(0).getParameters();
        assertTrue(map.containsKey(parameters.get(0).getName()));
        assertTrue(map.containsKey(parameters.get(1).getName()));
        assertTrue(map.containsKey(parameters.get(2).getName()));
        assertEquals(map.get(parameters.get(0).getName()), parameters.get(0).getValue());
        assertEquals(map.get(parameters.get(1).getName()), parameters.get(1).getValue());
        assertEquals(map.get(parameters.get(2).getName()), parameters.get(2).getValue());
    }

    @Test
    public void testGetCancelledQueueItem() {
        ResponseEntity<Void> job1 = api.jobsApi().build("QueueTest");
        assertNotNull(job1);
        ResponseEntity<Void> job2 = api.jobsApi().build("QueueTest");
        assertNotNull(job2);
        IntegerResponse job2Value = JenkinsUtils.getQueueItemIntegerResponse(job2.getHeaders());

        ResponseEntity<Void> success = api().cancel(job2Value.getValues());
        assertNotNull(success);
        assertEquals(success.getStatusCode(), HttpStatus.NO_CONTENT);

        QueueItem queueItem = api().queueItem(job2Value.getValues());
        assertTrue(queueItem.isCancelled());
        assertNull(queueItem.getWhy());
        assertNull(queueItem.getExecutable());
    }

    @Test
    public void testCancelNonExistentQueueItem() {
        try {
            api().cancel(123456789);
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    private QueueApi api() {
        return api.queueApi();
    }
}
