package io.github.hmedioni.jenkins.client.features;

import io.github.hmedioni.jenkins.client.*;
import io.github.hmedioni.jenkins.client.domain.common.*;
import io.github.hmedioni.jenkins.client.domain.job.*;
import io.github.hmedioni.jenkins.client.domain.plugins.*;
import io.github.hmedioni.jenkins.client.domain.queue.*;
import io.github.hmedioni.jenkins.client.exception.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.Assert.*;

@Test(groups = "live", singleThreaded = true)
public class JobsApiLiveTest extends BaseJenkinsApiLiveTest {

    private static final String FOLDER_PLUGIN_NAME = "cloudbees-folder";
    private static final String FOLDER_PLUGIN_VERSION = "latest";
    private static final String FREESTYLE_JOB_NAME = "FreeStyleSleep";
    private static final String PIPELINE_JOB_NAME = "PipelineSleep";
    private static final String PIPELINE_WITH_ACTION_JOB_NAME = "PipelineAction";
    private IntegerResponse queueId;
    private IntegerResponse queueIdForAnotherJob;
    private ResponseEntity<String> buildNumber;

    @BeforeTest
    public void cleanJenkins() {
        Objects.requireNonNull(api.jobsApi().jobList().getBody()).getJobs().forEach(job -> api().delete(job.getName()));
    }

    @Test
    public void testCreateJob() {
        String config = payloadFromResource("/freestyle-project-no-params.xml");
        ResponseEntity<Void> success = api().create("DevTest", config);
        assertTrue(success.getStatusCode().is2xxSuccessful());
    }

    // The next 3 tests must run one after the other as they use the same Job
    @Test
    public void testStopFreeStyleBuild() throws InterruptedException {
        String config = payloadFromResource("/freestyle-project-sleep-10-task.xml");
        ResponseEntity<Void> createStatus = api().create(FREESTYLE_JOB_NAME, config);
        assertTrue(createStatus.getStatusCode().is2xxSuccessful());
        ResponseEntity<Void> qIdResponse = api().build(FREESTYLE_JOB_NAME);
        IntegerResponse qId = JenkinsUtils.getQueueItemIntegerResponse(qIdResponse.getHeaders());
        assertNotNull(qId);
        assertTrue(Objects.requireNonNull(qId.getValues() > 0));
        QueueItem queueItem = getRunningQueueItem(qId.getValues());
        assertNotNull(queueItem);
        assertNotNull(queueItem.getExecutable());
        assertNotNull(queueItem.getExecutable().getNumber());
        ResponseEntity<Void> stopStatus = api().stop(FREESTYLE_JOB_NAME, queueItem.getExecutable().getNumber());
        assertTrue(stopStatus.getStatusCode().is3xxRedirection());
        BuildInfo buildInfo = getCompletedBuild(FREESTYLE_JOB_NAME, queueItem);
        assertEquals(buildInfo.getResult(), "ABORTED");
    }

    @Test(dependsOnMethods = "testStopFreeStyleBuild")
    public void testTermFreeStyleBuild() throws InterruptedException {
        ResponseEntity<Void> responseEntity = api().build(FREESTYLE_JOB_NAME);
        IntegerResponse qId = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());
        assertNotNull(qId);
        assertTrue(qId.getValues() > 0);
        QueueItem queueItem = getRunningQueueItem(qId.getValues());
        assertNotNull(queueItem);
        assertNotNull(queueItem.getExecutable());
        assertNotNull(queueItem.getExecutable().getNumber());
        //Falling error
        try {
            api().term(FREESTYLE_JOB_NAME, queueItem.getExecutable().getNumber());
            // Strangely, term does not work on FreeStyleBuild
        } catch (JenkinsAppException e) {
            assertEquals(e.errors().get(0).getExceptionName(), "com.cdancy.jenkins.rest.exception.RedirectTo404Exception");
            assertEquals(e.errors().get(0).getMessage(), "The term operation does not exist for " + url + "/job/" + FREESTYLE_JOB_NAME + "/" + queueItem.getExecutable().getNumber() + "/term/, try stop instead.");
        }
        api().stop(FREESTYLE_JOB_NAME, queueItem.getExecutable().getNumber());
        BuildInfo buildInfoStop = getCompletedBuild(FREESTYLE_JOB_NAME, queueItem);
        assertEquals(buildInfoStop.getResult(), "ABORTED");
    }

    @Test(dependsOnMethods = "testTermFreeStyleBuild")
    public void testKillFreeStyleBuild() throws InterruptedException {
        ResponseEntity<Void> responseEntity = api().build(FREESTYLE_JOB_NAME);
        IntegerResponse qId = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());

        assertNotNull(qId);
        assertTrue(qId.getValues() > 0);
        QueueItem queueItem = getRunningQueueItem(qId.getValues());
        assertNotNull(queueItem);
        assertNotNull(queueItem.getExecutable());
        assertNotNull(queueItem.getExecutable().getNumber());
        try {
            ResponseEntity<Void> killStatus = api().kill(FREESTYLE_JOB_NAME, queueItem.getExecutable().getNumber());
            // Strangely, term does not work on FreeStyleBuild
        } catch (JenkinsAppException e) {
            assertEquals(e.errors().get(0).getMessage(), "The kill operation does not exist for " + url + "/job/" + FREESTYLE_JOB_NAME + "/" + queueItem.getExecutable().getNumber() + "/kill/, try stop instead.");
            assertEquals(e.errors().get(0).getExceptionName(), "com.cdancy.jenkins.rest.exception.RedirectTo404Exception");
        }
        // Strangely, kill does not work on FreeStyleBuild
        api().stop(FREESTYLE_JOB_NAME, queueItem.getExecutable().getNumber());
        BuildInfo buildInfoStop = getCompletedBuild(FREESTYLE_JOB_NAME, queueItem);
        assertEquals(buildInfoStop.getResult(), "ABORTED");

        // Delete the job, it's no longer needed
        ResponseEntity<Void> success = api().delete(FREESTYLE_JOB_NAME);
        assertNotNull(success);
        assertTrue(success.getStatusCode().is3xxRedirection());
    }

    // The next 3 tests must run one after the other as they use the same Job
    @Test
    public void testStopPipelineBuild() throws InterruptedException {
        String config = payloadFromResource("/pipeline.xml");
        ResponseEntity<Void> createStatus = api().create(PIPELINE_JOB_NAME, config);
        assertTrue(createStatus.getStatusCode().is2xxSuccessful());
        ResponseEntity<Void> responseEntity = api().build(PIPELINE_JOB_NAME);
        IntegerResponse qId = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());

        assertNotNull(qId);
        assertTrue(qId.getValues() > 0);
        QueueItem queueItem = getRunningQueueItem(qId.getValues());
        assertNotNull(queueItem);
        assertNotNull(queueItem.getExecutable());
        assertNotNull(queueItem.getExecutable().getNumber());
        ResponseEntity<Void> stopStatus = api().stop(PIPELINE_JOB_NAME, queueItem.getExecutable().getNumber());
        assertEquals(stopStatus.getStatusCode(), HttpStatus.FOUND);
        BuildInfo buildInfo = getCompletedBuild(PIPELINE_JOB_NAME, queueItem);
        assertEquals(buildInfo.getResult(), "ABORTED");
    }

    @Test(dependsOnMethods = "testStopPipelineBuild")
    public void testTermPipelineBuild() throws InterruptedException {
        ResponseEntity<Void> responseEntity = api().build(PIPELINE_JOB_NAME);
        IntegerResponse qId = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());

        assertNotNull(qId);
        assertTrue(qId.getValues() > 0);
        QueueItem queueItem = getRunningQueueItem(qId.getValues());
        assertNotNull(queueItem);
        assertNotNull(queueItem.getExecutable());
        assertNotNull(queueItem.getExecutable().getNumber());
        ResponseEntity<Void> termStatus = api().term(PIPELINE_JOB_NAME, queueItem.getExecutable().getNumber());
        assertTrue(termStatus.getStatusCode().is3xxRedirection());
        BuildInfo buildInfo = getCompletedBuild(PIPELINE_JOB_NAME, queueItem);
        assertEquals(buildInfo.getResult(), "ABORTED");
    }

    @Test(dependsOnMethods = "testTermPipelineBuild")
    public void testKillPipelineBuild() throws InterruptedException {
        ResponseEntity<Void> responseEntity = api().build(PIPELINE_JOB_NAME);
        IntegerResponse qId = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());

        assertNotNull(qId);
        assertTrue(qId.getValues() > 0);
        QueueItem queueItem = getRunningQueueItem(qId.getValues());
        assertNotNull(queueItem);
        assertNotNull(queueItem.getExecutable());
        assertNotNull(queueItem.getExecutable().getNumber());
        ResponseEntity<Void> killStatus = api().kill(PIPELINE_JOB_NAME, queueItem.getExecutable().getNumber());
        assertTrue(killStatus.getStatusCode().is3xxRedirection());
        BuildInfo buildInfo = getCompletedBuild(PIPELINE_JOB_NAME, queueItem);
        assertEquals(buildInfo.getResult(), "ABORTED");

        // The Job is no longer needed, delete it.
        ResponseEntity<Void> success = api().delete(PIPELINE_JOB_NAME);
        assertNotNull(success);
        assertTrue(success.getStatusCode().is3xxRedirection());
    }

    @Test(dependsOnMethods = {"testCreateJob", "testCreateJobForEmptyAndNullParams", "testKillPipelineBuild", "testKillFreeStyleBuild", "testDeleteFolders"})
    public void testGetJobListFromRoot() {
        JobList output = api().jobList().getBody();
        assertNotNull(output);
        assertFalse(output.getJobs().isEmpty());
        assertEquals(output.getJobs().size(), 2);
    }

    @Test(dependsOnMethods = "testCreateJob")
    public void testGetJobInfo() {
        JobInfo output = api().jobInfo("DevTest").getBody();
        assertNotNull(output);
        assertEquals(output.getName(), "DevTest");
        assertNull(output.getLastBuild());
        assertNull(output.getFirstBuild());
        assertTrue(output.getBuilds().isEmpty());
    }

    @Test(dependsOnMethods = "testGetJobInfo")
    public void testLastBuildNumberOnJobWithNoBuilds() {
        try {
            api().lastBuildNumber("DevTest");
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test(dependsOnMethods = "testLastBuildNumberOnJobWithNoBuilds")
    public void testLastBuildTimestampOnJobWithNoBuilds() {
        try {
            api().lastBuildTimestamp("DevTest");
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test(dependsOnMethods = "testLastBuildTimestampOnJobWithNoBuilds")
    public void testBuildJob() throws InterruptedException {
        ResponseEntity<Void> responseEntity = api().build("DevTest");
        queueId = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());
        assertNotNull(queueId);
        assertTrue(queueId.getValues() > 0);
        // Before we exit the test, wait until the job runs
        QueueItem queueItem = getRunningQueueItem(queueId.getValues());
        getCompletedBuild("DevTest", queueItem);
    }

    @Test(dependsOnMethods = "testBuildJob")
    public void testLastBuildNumberOnJob() {
        buildNumber = api().lastBuildNumber("DevTest");
        assertNotNull(buildNumber.getBody());
        assertEquals(Integer.valueOf(buildNumber.getBody()), 1);
    }

    @Test(dependsOnMethods = "testLastBuildNumberOnJob")
    public void testLastBuildTimestamp() {
        String output = api().lastBuildTimestamp("DevTest");
        assertNotNull(output);
    }

    @Test(dependsOnMethods = "testLastBuildTimestamp")
    public void testLastBuildGetProgressiveText() {
        ResponseEntity<String> output = api().progressiveText("DevTest", 0);
        assertNotNull(output);
        assertTrue(output.getBody().length() > 0);
    }

    @Test(dependsOnMethods = "testLastBuildGetProgressiveText")
    public void testGetBuildInfo() {
        BuildInfo output = api().buildInfo("DevTest", Integer.parseInt(buildNumber.getBody())).getBody();
        assertNotNull(output);
        assertEquals("DevTest #" + buildNumber.getBody(), output.getFullDisplayName());
        assertEquals((int) queueId.getValues(), output.getQueueId());
    }

    @Test(dependsOnMethods = "testGetBuildInfo")
    public void testGetBuildParametersOfLastJob() {
        List<Parameter> parameters = api().buildInfo("DevTest", 1).getBody().getActions().get(0).getParameters();
        assertNull(parameters);
    }

    @Test
    public void testBuildInfoActions() throws InterruptedException {
        String config = payloadFromResource("/pipeline-with-action.xml");
        ResponseEntity<Void> createStatus = api().create(PIPELINE_WITH_ACTION_JOB_NAME, config);
        assertTrue(createStatus.getStatusCode().is2xxSuccessful());
        ResponseEntity<Void> responseEntity = api().build(PIPELINE_WITH_ACTION_JOB_NAME);
        IntegerResponse qId = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());

        assertNotNull(qId);
        assertTrue(qId.getValues() > 0);
        QueueItem queueItem = getRunningQueueItem(qId.getValues());
        assertNotNull(queueItem);
        assertNotNull(queueItem.getExecutable());
        assertNotNull(queueItem.getExecutable().getNumber());
        BuildInfo buildInfo = getCompletedBuild(PIPELINE_WITH_ACTION_JOB_NAME, queueItem);
        assertEquals(buildInfo.getResult(), "SUCCESS");
        System.out.println(buildInfo);
        boolean found = false;
        for (int idx = 0; idx < buildInfo.getActions().size(); idx++) {
            if (buildInfo.getActions().get(idx).getText() != null) {
                if (buildInfo.getActions().get(idx).getText().equals("Hudson, we have a problem.") && buildInfo.getActions().get(idx).getIconPath().equals("error.svg") && buildInfo.getActions().get(idx).getClazz().equals("com.jenkinsci.plugins.badge.action.BadgeSummaryAction")) {
                    found = true;
                }
            }
        }
        assertTrue(found);

        // The Job is no longer needed, delete it.
        ResponseEntity<Void> success = api().delete(PIPELINE_WITH_ACTION_JOB_NAME);
        assertNotNull(success);
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testGetBuildParametersOfLastJob")
    public void testCreateJobThatAlreadyExists() {
        String config = payloadFromResource("/freestyle-project.xml");
        try {
            api().create("DevTest", config);
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.BAD_REQUEST);
        }
    }

    @Test(dependsOnMethods = "testCreateJobThatAlreadyExists")
    public void testSetDescription() {
        ResponseEntity<Void> success = api().pushDescription("DevTest", "RandomDescription");
        assertEquals(success.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test(dependsOnMethods = "testSetDescription")
    public void testGetDescription() {
        String output = api().description("DevTest").getBody();
        assertEquals(output, "RandomDescription");
    }

    @Test(dependsOnMethods = "testGetDescription")
    public void testGetConfig() {
        String output = api().config("DevTest");
        assertNotNull(output);
    }

    @Test(dependsOnMethods = "testGetConfig")
    public void testUpdateConfig() {
        String config = payloadFromResource("/freestyle-project.xml");
        ResponseEntity<Void> success = api().pushConfig("DevTest", config);
        assertEquals(success.getStatusCode(), HttpStatus.OK);
    }

    @Test(dependsOnMethods = "testUpdateConfig")
    public void testBuildJobWithParameters() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("SomeKey", List.of("SomeVeryNewValue"));
        ResponseEntity<Void> output = api().buildWithParameters("DevTest", params);
        assertNotNull(output);
        assertTrue(output.getStatusCode().is2xxSuccessful());
    }

    @Test(dependsOnMethods = "testBuildJobWithParameters")
    public void testBuildJobWithNullParametersMap() {
        ResponseEntity<Void> output = api().buildWithParameters("DevTest");
        IntegerResponse qId = JenkinsUtils.getQueueItemIntegerResponse(output.getHeaders());

        assertNotNull(output);
        assertTrue(qId.getValues() > 0);
    }

    @Test(dependsOnMethods = "testBuildJobWithNullParametersMap")
    public void testBuildJobWithEmptyParametersMap() {
        try {
            api().buildWithParameters("DevTest", new HashMap<>());
        } catch (JenkinsAppException e) {
            assertEquals(e.errors().size(), 1);
        }
    }

    @Test(dependsOnMethods = "testBuildJobWithEmptyParametersMap")
    public void testDisableJob() {
        ResponseEntity<Void> success = api().disable("DevTest");
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testDisableJob")
    public void testDisableJobAlreadyDisabled() {
        ResponseEntity<Void> success = api().disable("DevTest");
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testDisableJobAlreadyDisabled")
    public void testEnableJob() {
        ResponseEntity<Void> success = api().enable("DevTest");
        assertTrue(success.getStatusCode().is3xxRedirection());
    }

    @Test(dependsOnMethods = "testEnableJob")
    public void testEnableJobAlreadyEnabled() {
        ResponseEntity<Void> success = api().enable("DevTest");
        assertTrue(success.getStatusCode().is3xxRedirection());
    }

    @Test(dependsOnMethods = "testEnableJobAlreadyEnabled")
    public void testRenameJob() {
        ResponseEntity<Boolean> success = api().rename("DevTest", "NewDevTest");
        assertTrue(success.getStatusCode().is3xxRedirection());
    }

    @Test(dependsOnMethods = "testRenameJob")
    public void testRenameJobNotExist() {
        try {
            api().rename("JobNotExist", "NewDevTest");
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test(dependsOnMethods = "testRenameJobNotExist")
    public void testDeleteJob() {
        ResponseEntity<Void> success = api().delete("NewDevTest");
        assertNotNull(success);
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    //
    // check for the presence of folder-plugin
    // If not present, attempt to install it.
    //
    @Test
    public void testInstallFolderPlugin() throws Exception {
        long endTime = 0;
        long maxWaitTime = 5 * 60 * 1000;
        if (!isFolderPluginInstalled()) {
            ResponseEntity<Void> status = api.pluginManagerApi().installNecessaryPlugins(FOLDER_PLUGIN_NAME + "@" + FOLDER_PLUGIN_VERSION);
            assertTrue(status.getStatusCode().is2xxSuccessful());
            while (endTime <= maxWaitTime) {
                if (!isFolderPluginInstalled()) {
                    Thread.sleep(10000);
                    endTime += 10000;
                } else {
                    break;
                }
            }
        }
        assertTrue(isFolderPluginInstalled());
    }

    @Test(dependsOnMethods = "testInstallFolderPlugin")
    public void testCreateFoldersInJenkins() {
        String config = payloadFromResource("/folder-config.xml");
        ResponseEntity<Void> success1 = api().create("test-folder", config);
        assertTrue(success1.getStatusCode().is2xxSuccessful());
        ResponseEntity<Void> success2 = api().create("test-folder", "test-folder-1", config);
        assertTrue(success2.getStatusCode().is2xxSuccessful());
    }

    @Test(dependsOnMethods = "testCreateFoldersInJenkins")
    public void testCreateJobInFolder() {
        String config = payloadFromResource("/freestyle-project-no-params.xml");
        ResponseEntity<Void> success = api().create("test-folder/job/test-folder-1", "JobInFolder", config);
        assertTrue(success.getStatusCode().is2xxSuccessful());
    }

    @Test(dependsOnMethods = "testCreateFoldersInJenkins")
    public void testCreateJobWithIncorrectFolderPath() {
        String config = payloadFromResource("/folder-config.xml");
        try {
            api().create("/test-folder//test-folder-1/", "Job", config);
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test(dependsOnMethods = "testCreateJobInFolder")
    public void testGetJobListInFolder() {
        JobList output = api().jobList("test-folder/job/test-folder-1").getBody();
        assertNotNull(output);
        assertFalse(output.getJobs().isEmpty());
        assertEquals(output.getJobs().size(), 1);
        assertEquals(output.getJobs().get(0), new Job("hudson.model.FreeStyleProject", "JobInFolder", url + "/job/test-folder/job/test-folder-1/job/JobInFolder/", "notbuilt"));
    }

    @Test(dependsOnMethods = "testCreateJobInFolder")
    public void testUpdateJobConfigInFolder() {
        String config = payloadFromResource("/freestyle-project.xml");
        ResponseEntity<Void> success = api().pushConfig("test-folder/job/test-folder-1", "JobInFolder", config);
        assertEquals(success.getStatusCode(), HttpStatus.OK);
    }

    @Test(dependsOnMethods = "testUpdateJobConfigInFolder")
    public void testDisableJobInFolder() {
        ResponseEntity<Void> success = api().disable("test-folder/job/test-folder-1", "JobInFolder");
        assertTrue(success.getStatusCode().is3xxRedirection());
    }

    @Test(dependsOnMethods = "testDisableJobInFolder")
    public void testEnableJobInFolder() {
        ResponseEntity<Void> success = api().enable("test-folder/job/test-folder-1", "JobInFolder");
        assertTrue(success.getStatusCode().is3xxRedirection());
    }

    @Test(dependsOnMethods = "testEnableJobInFolder")
    public void testSetDescriptionOfJobInFolder() {
        ResponseEntity<Void> success = api().pushDescription("test-folder/job/test-folder-1", "JobInFolder", "RandomDescription");
        assertEquals(success.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test(dependsOnMethods = "testSetDescriptionOfJobInFolder")
    public void testGetDescriptionOfJobInFolder() {
        ResponseEntity<String> output = api().description("test-folder/job/test-folder-1", "JobInFolder");
        assertEquals(output.getBody(), "RandomDescription");
    }

    @Test(dependsOnMethods = "testGetDescriptionOfJobInFolder")
    public void testGetJobInfoInFolder() {
        JobInfo output = api().jobInfo("test-folder/job/test-folder-1", "JobInFolder").getBody();
        assertNotNull(output);
        assertEquals(output.getName(), "JobInFolder");
        assertTrue(output.getBuilds().isEmpty());
    }

    @Test(dependsOnMethods = "testGetJobInfoInFolder")
    public void testBuildWithParameters() throws InterruptedException {
        Map<String, List<String>> params = new HashMap<>();
        params.put("SomeKey", List.of("SomeVeryNewValue"));
        ResponseEntity<Void> responseEntity = api().buildWithParameters("test-folder/job/test-folder-1", "JobInFolder", params);
        queueIdForAnotherJob = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());
        assertNotNull(queueIdForAnotherJob);
        assertTrue(queueIdForAnotherJob.getValues() > 0);
        QueueItem queueItem = getRunningQueueItem(queueIdForAnotherJob.getValues());
        assertNotNull(queueItem);
    }

    @Test(dependsOnMethods = "testBuildWithParameters")
    public void testLastBuildTimestampOfJobInFolder() {
        String output = api().lastBuildTimestamp("test-folder/job/test-folder-1", "JobInFolder");
        assertNotNull(output);
    }

    @Test(dependsOnMethods = "testLastBuildTimestampOfJobInFolder")
    public void testGetProgressiveText() {
        ResponseEntity<String> output = api().progressiveText("test-folder/job/test-folder-1", "JobInFolder", 0);
        assertNotNull(output);
        assertFalse(Objects.requireNonNull(output.getBody()).isEmpty());
    }

    @Test(dependsOnMethods = "testGetProgressiveText")
    public void testGetBuildInfoOfJobInFolder() {
        BuildInfo output = api().buildInfo("test-folder/job/test-folder-1", "JobInFolder", 1).getBody();
        assertNotNull(output);
        assertTrue(output.getFullDisplayName().contains("JobInFolder #1"));
        assertEquals((int) queueIdForAnotherJob.getValues(), output.getQueueId());
    }

    @Test(dependsOnMethods = "testGetProgressiveText")
    public void testGetBuildParametersofJob() {
        List<Parameter> parameters = api().buildInfo("test-folder/job/test-folder-1", "JobInFolder", 1).getBody().getActions().get(0).getParameters();
        assertNotNull(parameters);
        assertEquals(parameters.get(0).getName(), "SomeKey");
        assertEquals(parameters.get(0).getValue(), "SomeVeryNewValue");
    }

    @Test(dependsOnMethods = "testGetProgressiveText")
    public void testGetBuildCausesOfJob() {
        List<Cause> causes = api().buildInfo("test-folder/job/test-folder-1", "JobInFolder", 1).getBody().getActions().get(1).getCauses();
        assertNotNull(causes);
        assertTrue(causes.size() > 0);
        assertNotNull(causes.get(0).getShortDescription());
        assertNotNull(causes.get(0).getUserId());
        assertNotNull(causes.get(0).getUserName());
    }

    @Test(dependsOnMethods = "testGetProgressiveText")
    public void testGetProgressiveTextOfBuildNumber() {
        ResponseEntity<String> output = api().progressiveText("test-folder/job/test-folder-1", "JobInFolder", 1, 0);
        assertNotNull(output);
        assertFalse(Objects.requireNonNull(output.getBody()).isEmpty());
    }

    @Test
    public void testCreateJobForEmptyAndNullParams() {
        String config = payloadFromResource("/freestyle-project-empty-and-null-params.xml");
        ResponseEntity<Void> success = api().create("JobForEmptyAndNullParams", config);
        assertTrue(success.getStatusCode().is2xxSuccessful());
    }

    @Test(dependsOnMethods = "testCreateJobForEmptyAndNullParams")
    public void testBuildWithParametersOfJobForEmptyAndNullParams() throws InterruptedException {
        Map<String, List<String>> params = new HashMap<>();
        params.put("SomeKey1", List.of(""));
        params.put("SomeKey2", null);
        ResponseEntity<Void> responseEntity = api.jobsApi().buildWithParameters("JobForEmptyAndNullParams", params);
        IntegerResponse job1 = JenkinsUtils.getQueueItemIntegerResponse(responseEntity.getHeaders());
        assertNotNull(job1);
        assertTrue(job1.getValues() > 0);
        QueueItem queueItem = getRunningQueueItem(job1.getValues());
        assertNotNull(queueItem);
    }

    @Test(dependsOnMethods = "testBuildWithParametersOfJobForEmptyAndNullParams")
    public void testGetBuildParametersOfJobForEmptyAndNullParams() {
        List<Parameter> parameters = api().buildInfo("JobForEmptyAndNullParams", 1).getBody().getActions().get(0).getParameters();
        assertNotNull(parameters);
        assertEquals(parameters.get(0).getName(), "SomeKey1");
        assertTrue(parameters.get(0).getValue().isEmpty());
        assertEquals(parameters.get(1).getName(), "SomeKey2");
        assertTrue(parameters.get(1).getValue().isEmpty());
    }

    @Test(dependsOnMethods = {"testGetBuildParametersOfJobForEmptyAndNullParams", "testGetJobListFromRoot"})
    public void testDeleteJobForEmptyAndNullParams() {
        ResponseEntity<Void> success = api().delete("JobForEmptyAndNullParams");
        assertTrue(success.getStatusCode().is3xxRedirection());
    }

    @Test(dependsOnMethods = "testCreateFoldersInJenkins")
    public void testCreateJobWithLeadingAndTrailingForwardSlashes() {
        String config = payloadFromResource("/freestyle-project-no-params.xml");
        ResponseEntity<Void> success = api().create("/test-folder/job/test-folder-1/", "Job", config);
        assertTrue(success.getStatusCode().is2xxSuccessful());
    }

    @Test(dependsOnMethods = "testCreateJobWithLeadingAndTrailingForwardSlashes")
    public void testDeleteJobWithLeadingAndTrailingForwardSlashes() {
        ResponseEntity<Void> success = api().delete("/test-folder/job/test-folder-1/", "Job");
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testGetBuildInfoOfJobInFolder")
    public void testRenameJobInFolder() {
        ResponseEntity<Boolean> success = api().rename("test-folder/job/test-folder-1", "JobInFolder", "NewJobInFolder");
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testRenameJobInFolder")
    public void testDeleteJobInFolder() {
        ResponseEntity<Void> success = api().delete("test-folder/job/test-folder-1", "NewJobInFolder");
        assertEquals(success.getStatusCode(), HttpStatus.FOUND);
    }

    @Test(dependsOnMethods = "testDeleteJobInFolder")
    public void testDeleteFolders() {
        ResponseEntity<Void> success1 = api().delete("test-folder", "test-folder-1");
        assertTrue(success1.getStatusCode().is3xxRedirection());
        ResponseEntity<Void> success2 = api().delete("test-folder");
        assertTrue(success2.getStatusCode().is3xxRedirection());
    }

    @Test
    public void testGetJobInfoNonExistentJob() {
        try {
            api().jobInfo(randomString()).getBody();
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testDeleteJobNonExistent() {
        try {
            api().delete(randomString());
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testGetConfigNonExistentJob() {
        try {
            api().config(randomString());
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testSetDescriptionNonExistentJob() {
        try {
            api().pushDescription(randomString(), "RandomDescription");
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testGetDescriptionNonExistentJob() {
        try {
            api().description(randomString());
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testBuildNonExistentJob() {
        try {
            api().build(randomString());
        } catch (JenkinsAppException e) {
            assertEquals(e.errors().size(), 1);
        }
    }

    @Test
    public void testGetBuildInfoNonExistentJob() {
        try {
            api().buildInfo(randomString(), 123).getBody();
        } catch (JenkinsAppException e) {
            assertEquals(e.code(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testBuildNonExistentJobWithParams() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("SomeKey", List.of("SomeVeryNewValue"));
        try {
            api().buildWithParameters(randomString(), params);
        } catch (JenkinsAppException e) {
            assertNotNull(e);
            assertTrue(e.errors().size() > 0);
        }
    }

    private boolean isFolderPluginInstalled() {
        boolean installed = false;
        Plugins plugins = api.pluginManagerApi().plugins(3);
        for (Plugin plugin : plugins.getPlugins()) {
            if (plugin.getShortName().equals(FOLDER_PLUGIN_NAME)) {
                installed = true;
                break;
            }
        }
        return installed;
    }

    private JobsApi api() {
        return api.jobsApi();
    }
}
