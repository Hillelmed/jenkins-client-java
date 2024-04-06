package io.github.hmedioni.jenkins.client;

import io.github.hmedioni.jenkins.client.auth.*;
import io.github.hmedioni.jenkins.client.config.*;
import io.github.hmedioni.jenkins.client.domain.job.*;
import io.github.hmedioni.jenkins.client.domain.queue.*;
import org.testng.annotations.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;


@Test(groups = "live")
public class BaseJenkinsApiLiveTest extends BaseJenkinsTest {

    protected final JenkinsAuthentication jenkinsAuthentication;
    protected JenkinsProperties jenkinsProperties = JenkinsProperties.builder().url(url)
        .jenkinsAuthentication(JenkinsAuthentication.builder().authType(AuthenticationType.USERNAME_PASSWORD)
            .credentials(usernamePassword).build()).build();
    protected JenkinsClient jenkinsClient = JenkinsClient.create(jenkinsProperties);
    protected JenkinsApi api = jenkinsClient.api();

    public BaseJenkinsApiLiveTest() {
        this.jenkinsAuthentication = TestUtilities.inferTestAuthentication();
    }

    protected String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String payloadFromResource(final String resource) {
        try {
            return new String((getClass().getResourceAsStream(resource)).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return a queue item that is being built.
     * If the queue item is canceled before the build is launched, null is returned.
     * To prevent the test from hanging, this method times out after 10 attempts and the queue item is returned the way it is.
     *
     * @param queueId The queue id returned when asking Jenkins to run a build.
     * @return Null if the queue item has been canceled before it has had a chance to run,
     * otherwise the QueueItem element is returned, but this does not guarantee that the build runs.
     * The caller has to check the value of queueItem.executable, and if it is  the queue item is still pending.
     */
    protected QueueItem getRunningQueueItem(int queueId) throws InterruptedException {
        int max = 10;
        QueueItem queueItem = api.queueApi().queueItem(queueId);
        while (max > 0) {
            if (queueItem.isCancelled()) return null;
            if (queueItem.getExecutable() != null) {
                return queueItem;
            }
            Thread.sleep(2000);
            queueItem = api.queueApi().queueItem(queueId);
            max = max - 1;
        }
        return queueItem;
    }

    protected BuildInfo getCompletedBuild(String jobName, QueueItem queueItem) throws InterruptedException {
        int max = 10;
        BuildInfo buildInfo = api.jobsApi().buildInfo(jobName, queueItem.getExecutable().getNumber()).getBody();
        while (buildInfo.getResult() == null) {
            Thread.sleep(2000);
            buildInfo = api.jobsApi().buildInfo(jobName, queueItem.getExecutable().getNumber()).getBody();
        }
        return buildInfo;
    }

}
